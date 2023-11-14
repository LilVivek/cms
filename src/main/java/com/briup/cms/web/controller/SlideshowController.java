package com.briup.cms.web.controller;

import com.briup.cms.bean.Slideshow;
import com.briup.cms.service.ISlideshowService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
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
}
