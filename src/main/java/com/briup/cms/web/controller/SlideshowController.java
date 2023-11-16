package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.service.ISlideshowService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Api(tags = "轮播图模块")
@Slf4j
@RestController // @ResponseBody + @Controller
@RequestMapping("/slideshow")
public class SlideshowController {
    @Autowired
    ISlideshowService iSlideshowService;

    @ApiOperation("查询启用轮播图")
    @GetMapping("/queryAllEnable")
    public Result queryAll() {
        List<Slideshow> data = iSlideshowService.queryAllEnable();
        return Result.success(data);
    }

    @ApiOperation("分页查询")
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize,
                        @RequestParam(required = false) String desc, @RequestParam(required = false) String status) {
        Page<Slideshow> data = iSlideshowService.query(pageNum, pageSize, desc, status);
        return Result.success(data);
    }

    @ApiOperation("根据id查询轮播图")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable Integer id) {
        Slideshow data = iSlideshowService.queryById(id);
        return Result.success(data);
    }

    @ApiOperation("新增或修改")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Slideshow slideshow) {
        iSlideshowService.insertOrUpdate(slideshow);
        return Result.success("操作成功");
    }

    @ApiOperation("刪除或者批量刪除")
    @DeleteMapping("/deleteByBatch/{ids}")
    public Result deleteByBatch(@PathVariable List<Integer> ids) {
        iSlideshowService.deleteByBatch(ids);
        return Result.success();
    }
}
