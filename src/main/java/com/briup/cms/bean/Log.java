package com.briup.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("cms_log")
@ApiModel(value = "Log对象", description = "")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("访问用户账号")
    private String username;

    @ApiModelProperty("接口描述信息")
    private String businessName;

    @ApiModelProperty("请求的地址")
    private String requestUrl;

    @ApiModelProperty("请求的方式，get post delete put")
    private String requestMethod;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("ip来源")
    private String source;

    @ApiModelProperty("请求接口耗时")
    private Long spendTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("请求的参数")
    private String paramsJson;

    @ApiModelProperty("响应参数")
    private String resultJson;
}
