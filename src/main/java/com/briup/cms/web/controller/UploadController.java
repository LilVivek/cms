package com.briup.cms.web.controller;

import com.briup.cms.service.IUploadService;
import com.briup.cms.util.Result;
import com.briup.cms.util.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件上传模块")
@Slf4j
@RestController
public class UploadController {
    @Autowired
    IUploadService iUploadService;

    @ApiOperation("文件上传")
    @PostMapping("/auth/upload")
    @SneakyThrows//帮助处理 编译时异常
    // @RequestPart 注解可以与 MultipartFile 类型的参数一起使用，以处理上传的文件
    public Result upload(@RequestPart MultipartFile img) {
        log.info("开始进行文件上传:{}", img.getOriginalFilename());
        String url = iUploadService.upLoad(img);
        return Result.success(url);
    }
}