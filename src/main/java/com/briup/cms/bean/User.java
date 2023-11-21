package com.briup.cms.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cms_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using =ToStringSerializer.class)//在java对象的属性序列化成Json数据时，指定转换成String类型
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户电话")
    private String phone;

    @ApiModelProperty("注册时间")
    private LocalDateTime registerTime;

    @ApiModelProperty("用户状态")
    private String status;

    @ApiModelProperty("生日")
    private LocalDate birthday;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("是否为会员")
    private Integer isVip;

    @ApiModelProperty("会员到期时间")
    private LocalDateTime expiresTime;

    @ApiModelProperty("用户删除状态")
    @TableLogic
    private Integer deleted;
}
