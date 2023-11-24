package com.briup.cms.service.impl;

import com.briup.cms.bean.Article;
import com.briup.cms.bean.Comment;
import com.briup.cms.bean.User;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.mapper.ArticleMapper;
import com.briup.cms.mapper.CommentMapper;
import com.briup.cms.mapper.UserMapper;
import com.briup.cms.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void saveComment(Comment comment) {
        if (comment == null) {
            throw new ServiceException(ResultCode.COMMENT_NOT_EXIST);
        }
        Long articleId = comment.getArticleId();
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new ServiceException(ResultCode.ARTICLE_NOT_EXIST);
        }
        Long userId = comment.getUserId();
        User user = userMapper.selectById(userId);
        if (user == null || "禁用".equals(user.getStatus())) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        comment.setPublishTime(LocalDateTime.now());
        commentMapper.insert(comment);
    }
}
