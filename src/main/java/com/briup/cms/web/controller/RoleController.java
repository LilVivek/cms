package com.briup.cms.web.controller;

import com.briup.cms.bean.Role;
import com.briup.cms.service.IRoleService;
import com.briup.cms.util.Result;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;

    @ApiOperation("获取所有角色信息")
    @GetMapping("/getAllRole")
    public Result getAll(){
        List<Role> list = iRoleService.list();
        return Result.success(list);
    }
}
