package com.briup.cms.web.controller;

import com.briup.cms.bean.User;
import com.briup.cms.service.IUserService;
import com.briup.cms.util.MD5Utils;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Api(tags = "登录模块")
@Slf4j
@RestController // @ResponseBody + @Controller
public class LoginController {
    @Autowired
    IUserService iUserService;

    @ApiOperation("用户登录接口")
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user) {
//        System.out.println(MD5Utils.MD5("123456"));
        String token = iUserService.login(user.getUsername(), MD5Utils.MD5(user.getPassword()));

        return Result.success(token);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logout")
    public Result logout() {//后端什么都不用做，前端收到响应后会删掉token
        return Result.success();
    }
}
