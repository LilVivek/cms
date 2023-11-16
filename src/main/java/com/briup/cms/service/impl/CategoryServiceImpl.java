package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Article;
import com.briup.cms.bean.Category;
import com.briup.cms.bean.User;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.ArticleMapper;
import com.briup.cms.mapper.CategoryMapper;
import com.briup.cms.mapper.UserMapper;
import com.briup.cms.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void insert(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        Category selectOne = categoryMapper.selectOne(wrapper);
        if (selectOne != null) {//栏目名不唯一
            throw new ServiceException(ResultCode.CATEGORYNAME_HAS_EXISTED);
        }

        Integer parentId = category.getParentId();
        if (parentId != null && categoryMapper.selectById(parentId) == null) {//如果为二级栏目其父栏目id必须有效
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
        int order_num = 1;//在没有数据时默认为1
        //在不加where判断条件下返回条数若不为0则代表数据库中有数据,order_num+1
        if (categoryMapper.selectCount(null) != 0) {
            LambdaQueryWrapper<Category> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.orderByDesc(Category::getOrderNum);
            List<Category> list = categoryMapper.selectList(wrapper1);//根据oderNum倒序查找
            Category maxOrderNumObj = list.get(0);//因为已经倒叙，0号下标就是orderNum最大的对象
            order_num = maxOrderNumObj.getOrderNum() + 1;
        }
        category.setOrderNum(order_num);
        categoryMapper.insert(category);

    }

    @Override
    public Category getCategoryById(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new ServiceException(ResultCode.CATEGORY_NOT_EXIST);
        }
        return category;
    }

    @Override
    public void update(Category category) {
        if (category == null) {
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);
        }
        Category oldCategory = categoryMapper.selectById(category.getId());//要修改的对象
        if (category.getId() == null || oldCategory == null) {//传入的id不能为空且DB有对应的数据,否则抛异常
            throw new ServiceException(ResultCode.CATEGORY_NOT_EXIST);
        }
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();

        //如果修改了栏目名且栏目名不为空
        if (category.getName() != null && !category.getName().equals(oldCategory.getName())) {
            wrapper.eq(Category::getName, category.getName());
            //如果返回值不为空说明不唯一
            if (categoryMapper.selectOne(wrapper) != null) {
                throw new ServiceException(ResultCode.CATEGORYNAME_HAS_EXISTED);
            }
        }
        //如果要修改的栏目没有父栏目，那么不能把它改成有父栏目(栏目级别不能改动)
        if (oldCategory.getParentId() == null && category.getParentId() != null) {
            throw new ServiceException(ResultCode.CATEGORY_LEVEL_SETTING_ERROR);
        }
        //如果要修改的栏目有父栏目，那么不能把它改成没有父栏目(栏目级别不能改动)
        if (oldCategory.getParentId() != null && category.getParentId() == null) {
            throw new ServiceException(ResultCode.CATEGORY_LEVEL_SETTING_ERROR);
        }
        //如果要修改的栏目有父栏目，且要修改他的父栏目(父栏目不为空且父栏目发生了修改)
        if (oldCategory.getParentId() != null
                && category.getParentId() != null && !category.getParentId().equals(oldCategory.getParentId())) {
            Category parentCategory = categoryMapper.selectById(category.getParentId());
            //新传入的parent_id必须有校且没有父栏目，否则报错
            if (parentCategory == null || parentCategory.getParentId() != null) {
                throw new ServiceException(ResultCode.PCATEGORY_IS_INVALID);
            }
        }
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteById(Integer id) {
        Category category = categoryMapper.selectById(id);//要删除的栏目
        if (category == null) {
            throw new ServiceException(ResultCode.CATEGORY_NOT_EXIST);
        }

        //不能删除的情况抛异常
        if (category.getParentId() == null) {//如果是一级栏目
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getParentId, id);
            //如果DB里有数据以传入的id为parentId,说明下面包含二级栏目
            if (categoryMapper.selectCount(wrapper) != 0) {
                throw new ServiceException(ResultCode.PARAM_IS_INVALID);
            }
        } else {//如果是二级栏目
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Article::getCategoryId, id);
            //该栏目下有文章
            if (articleMapper.selectCount(wrapper) != 0) {
                List<Article> articles = articleMapper.selectList(wrapper);//二级栏目下的所有文章
                List<Long> userIds = new ArrayList<>();//文章的所有作者id
                for (Article article : articles) {
                    userIds.add(article.getUserId());
                }
                List<User> users = userMapper.selectBatchIds(userIds);
                if (users.size() != 0) {//所有文章的作者没有全部注销
                    throw new ServiceException(ResultCode.PARAM_IS_INVALID);
                }
            }
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public void deleteByIdAll(List<Integer> ids) {
        int count = 0;
        for (Integer id : ids) {
            Category category = categoryMapper.selectById(id);//要删除的栏目
            if (category == null) {
                continue;
            }

            //不能删除的情况抛异常
            if (category.getParentId() == null) {//如果是一级栏目
                LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Category::getParentId, id);
                //如果DB里有数据以传入的id为parentId,说明下面包含二级栏目
                if (categoryMapper.selectCount(wrapper) != 0) {
                    continue;
                }
            } else {//如果是二级栏目
                LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Article::getCategoryId, id);
                //该栏目下有文章
                if (articleMapper.selectCount(wrapper) != 0) {
                    List<Article> articles = articleMapper.selectList(wrapper);//二级栏目下的所有文章
                    List<Long> userIds = new ArrayList<>();//文章的所有作者id
                    for (Article article : articles) {
                        userIds.add(article.getUserId());
                    }
                    List<User> users = userMapper.selectBatchIds(userIds);
                    if (users.size() != 0) {//所有文章的作者没有全部注销
                        continue;
                    }
                }
            }
            count += categoryMapper.deleteById(id);
        }
        if (count==0){
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
    }

    @Override
    public IPage<Category> query(Integer pageNum, Integer pageSize, Integer parentId) {
        if (pageNum == null || pageNum <= 0 || pageSize == null|| pageSize <= 0){
            throw new ServiceException(ResultCode.PARAM_IS_INVALID);
        }
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(parentId!=null,Category::getParentId,parentId);//如果有parentId,查找所有以它为父栏目的栏目
        wrapper.orderByAsc(Category::getParentId)
                .orderByAsc(Category::getOrderNum);
        Page<Category> list = categoryMapper.selectPage(page, wrapper);//这里一旦调用selectPage方法后返回值就会赋值给page
//        System.out.println(list+":"+page);//list和page指向同一地址,list和page完全相同
        if (list.getTotal()==0){
            throw new ServiceException(ResultCode.CATEGORY_NOT_EXIST);
        }
        return list;
    }
}
