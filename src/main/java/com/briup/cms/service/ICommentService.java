package com.briup.cms.service;

import com.briup.cms.bean.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
public interface ICommentService extends IService<Comment> {

    void saveComment(Comment comment);
}
