package com.briup.cms.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
//@Accessors(chain = true)
@TableName("cms_log")
@ApiModel(value = "Log对象", description = "")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    @ApiModelProperty("访问用户账号")
    @ExcelProperty("访问用户账号")
    private String username;

    @ApiModelProperty("接口描述信息")
    @ExcelProperty("接口描述信息")
    private String businessName;

    @ApiModelProperty("请求的地址")
    @ExcelProperty("请求的地址")
    private String requestUrl;

    @ApiModelProperty("请求的方式，get post delete put")
    @ExcelProperty("请求的方式")
    private String requestMethod;

    @ApiModelProperty("ip")
    @ExcelProperty("ip")
    private String ip;

    @ApiModelProperty("ip来源")
    @ExcelProperty("ip来源")
    private String source;

    @ApiModelProperty("请求接口耗时")
    @ExcelProperty("请求接口耗时")
    private Long spendTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("请求的参数")
    @ExcelProperty("请求的参数")
    private String paramsJson;

    @ApiModelProperty("响应参数")
    @ExcelProperty("响应参数")
    private String resultJson;
}
