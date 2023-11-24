package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Article;
import com.briup.cms.bean.Category;
import com.briup.cms.bean.Comment;
import com.briup.cms.bean.Extend.ArticleExtend;
import com.briup.cms.bean.Extend.ArticlePage;
import com.briup.cms.bean.Extend.CategoryExtend;
import com.briup.cms.bean.User;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.ArticleMapper;
import com.briup.cms.mapper.CategoryMapper;
import com.briup.cms.mapper.CommentMapper;
import com.briup.cms.mapper.UserMapper;
import com.briup.cms.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.JwtUtil;
import com.briup.cms.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //redis当中阅读量的key
    private final String REDIS_KEY = "Article_Read_Num";

    @Override
    public void insertOrUpdate(Article article) {
        if (article == null) {
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);
        }
        Long userId1 = Long.valueOf(JwtUtil.getUserId(getToken()));
        article.setUserId(userId1);

        Integer categoryId = article.getCategoryId();
        if (categoryId == null) {
            throw new ServiceException(ResultCode.PARAM_NOT_COMPLETE);
        } else {
            Category category = categoryMapper.selectById(categoryId);
            if (category == null || category.getParentId() == null) {//文章只能在二级栏目下方修改或新增，否则报错
                throw new ServiceException(ResultCode.CATEGORY_LEVEL_SETTING_ERROR);
            }
        }
        Long userId = article.getUserId();
        if (userId == null) {
            throw new ServiceException(ResultCode.PARAM_NOT_COMPLETE);
        } else {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new ServiceException(ResultCode.USER_NOT_EXIST);
            }
        }
        Long id = article.getId();
        if (id != null) {//存在则修改
            Article DBArticle = articleMapper.selectById(id);
            if (DBArticle == null) {//如果文章不存在，则报错
                throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);
            } else {
                articleMapper.updateById(article);
            }
        } else {//不存在为新增
            article.setPublishTime(LocalDateTime.now());
            articleMapper.insert(article);
        }
    }

    @Override
    public void reviewArticle(Long id, String status) {
        if (id == null || status == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);
        }
        article.setStatus(status);
        articleMapper.updateById(article);
    }

    @Override
    public void deleteByBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
        int count = 0;
        for (Long id : ids) {
            Article article = articleMapper.selectById(id);
            if (article != null) {
                count++;
            }
        }
        if (count > 0) {//至少有一个id有效且存在
            int i = articleMapper.deleteBatchIds(ids);
            if (i == 0) {
                throw new ServiceException(ResultCode.SYSTEM_INNER_ERROR);
            }
        } else {
            throw new ServiceException(ResultCode.DATA_NONE);
        }
    }

    @Override
    public IPage<ArticleExtend> queryByPage(ArticlePage articlePage) {
        Page<ArticleExtend> page = new Page<>(articlePage.getPageNum(), articlePage.getPageSize());
        System.out.println(articlePage);
        articleMapper.selectArticleExtendByPage(page, articlePage);
        return page;
    }

    @Override
    public ArticleExtend queryById(Long id) {
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);
        }
        ArticleExtend articleExtend = new ArticleExtend();
        BeanUtils.copyProperties(article, articleExtend);//把article中的属性放到articleExtend中
        LambdaQueryChainWrapper<Comment> wrapper = new LambdaQueryChainWrapper<>(commentMapper);
        List<Comment> comments = wrapper.eq(Comment::getArticleId, id)
                .orderByDesc(Comment::getPublishTime)
                .last("limit 0,3")
                .list();
//        BeanUtils.copyProperties(comments,articleExtend);//把Comment列表复制到articleExtend中
        articleExtend.setComments(comments);

        LambdaQueryChainWrapper<User> userWrapper = new LambdaQueryChainWrapper<>(userMapper);
        User one = userWrapper.eq(User::getId, article.getUserId()).one();
        if (one == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        one.setPassword(null);
        articleExtend.setAuthor(one);
        HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
        int redisReadNum = Math.toIntExact(ops.increment(REDIS_KEY, article.getId().toString(), 1));
        articleExtend.setReadNum(redisReadNum);//从redis中读取阅读量并加1，而不是从数据库中读取
        log.info("当前文章数据库阅读量：" + article.getReadNum() + "---redis中的阅读量" + redisReadNum);
        return articleExtend;
    }

    @Override
    public String getToken() {
        /* 在SpringWeb中RequestContextHolder 可以让你在任何地方获取当前请求的相关信息，而无需将请求对象作为参数传递或手动管理请求的传递。*/
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //这里一定要和前端的请求头里的key的取名相同，虽然随便取名在knife4j中全局配置里配置对应的名字也可以用，但是在前端里名字不一样就用不了
        String token = request.getHeader("Authorization");

//        String userId = (String) request.getAttribute("userId");//这个userId是在拦截器里放在请求的Attribute里的
//        System.out.println("Attribute里的userId："+userId);

//        String tokenUserId = JwtUtil.getUserId(token);//这个userId是在token的payLoad载荷中的Audience受众里的
//        System.out.println("token里的UserId："+tokenUserId);

        /*System.out.println(Thread.currentThread().getName()+"set userID:"+tokenUserId);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"get userID:"+tokenUserId);*/

        // 对token字符串进行验证
        if (token == null) {  //token不存在，抛出用户未登录异常
            throw new ServiceException(ResultCode.USER_NOT_LOGIN);
        }
        return token;

    }


}
