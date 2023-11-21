package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Extend.UserExtend;
import com.briup.cms.bean.User;
import com.briup.cms.service.IUserService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @ApiOperation("新增用户")
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        iUserService.insert(user);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "根据id查找用户")
    @GetMapping("/queryById/{id}")
    public Result queryById(@PathVariable Long id) {
        User user = iUserService.queryById(id);
        return Result.success(user);
    }

    @ApiOperation(value = "设置用户为Vip", notes = "如果已经是会员，不能再修改会员状态和会员过期时间")
    @PutMapping("/setVip/{id}")
    public Result setVip(@PathVariable Long id) {
        iUserService.setVip(id);
        return Result.success("更新成功");
    }

    @ApiOperation(value = "更新用户信息", notes = "id必须存在且有效，如果username存在则必须唯一")
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        iUserService.updateUser(user);
        return Result.success("更新成功");
    }

    @ApiOperation(value = "根据id删除用户", notes = "至少有一个id存在且有效")
    @DeleteMapping("/deleteByBatch/{ids}")
    public Result deleteByBatch(@PathVariable("ids") List<Long> ids) {
        iUserService.deleteByBatch(ids);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "分页+条件查询用户", notes = "查询的结果中应该包含角色信息")
    @GetMapping("/query")
    public Result query(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip) {
        IPage<UserExtend> page = iUserService.queryByPage(pageNum, pageSize, username, status, roleId, isVip);
        return Result.success(page);
    }
}
