package com.briup.cms.web.controller;

import com.briup.cms.bean.Comment;
import com.briup.cms.service.ICommentService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Api(tags = "评论模块")
@RestController
@RequestMapping("/auth/comment")
public class CommentController {
    @Autowired
    ICommentService iCommentService;

    @ApiOperation(value = "新增一级评论", notes = "一级评论直接对文章进行评论")
    @PostMapping("/saveComment")
    public Result saveComment(@RequestBody Comment comment) {
        iCommentService.saveComment(comment);
        return Result.success("新增成功");
    }
}
