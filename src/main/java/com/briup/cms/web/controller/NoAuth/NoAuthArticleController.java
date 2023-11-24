package com.briup.cms.web.controller.NoAuth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Article;
import com.briup.cms.bean.Extend.ArticleExtend;
import com.briup.cms.bean.Extend.ArticlePage;
import com.briup.cms.service.IArticleService;
import com.briup.cms.util.AOP.MyLog;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Api(tags = "游客咨询模块")
@RestController
@RequestMapping("/articles")
public class NoAuthArticleController {
    @Autowired//转换器->监听器->IOC容器
            IArticleService iArticleService;

    @ApiOperation("新增或修改文章")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Article article) {
        iArticleService.insertOrUpdate(article);
        return Result.success("新增或修改成功");
    }

    @ApiOperation(value = "审核文章", notes = "此接口用于审核文章，文章id必须有效，status: 审核通过、审核未通过")
    @PostMapping("/reviewArticle")
    public Result reviewArticle(Long id, String status) {
        iArticleService.reviewArticle(id, status);
        return Result.success("审核完成");
    }


    @ApiOperation(value = "删除文章", notes = "至少有一条id存在，就算删除成功")
    @DeleteMapping("/deleteById/{ids}")
    public Result deleteByBatch(@PathVariable("ids") List<Long> ids) {
        iArticleService.deleteByBatch(ids);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "分页+条件查询文章", notes = "")
    @PostMapping("/query")
    public Result queryById(@RequestBody ArticlePage articlePage) {
        IPage<ArticleExtend> page = iArticleService.queryByPage(articlePage);
        return Result.success(page);
    }

    @ApiOperation(value = "查询指定文章", notes = "此接口用于查询指定文章，并且带有三条一级评论")
    @GetMapping("/{id}")
    public Result queryById(@PathVariable("id") Long id) {
        ArticleExtend articleExtend = iArticleService.queryById(id);
        return Result.success(articleExtend);
    }
}
