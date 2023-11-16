package com.briup.cms.web.controller;

import com.briup.cms.bean.User;
import com.briup.cms.service.IUserService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Api(tags = "用户模块")
@RestController
@Slf4j
@RequestMapping("/auth/user")
public class UserController {
    @Autowired
    IUserService iUserService;

    @ApiOperation("获取用户个人信息")
    @GetMapping("/info")
    public Result getInfo(@RequestAttribute("userId") Long id) {//根据请求里面的Attribute来传入
        User user = iUserService.queryById(id);
        return Result.success(user);
    }


}
