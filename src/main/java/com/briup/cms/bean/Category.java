package com.briup.cms.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.briup.cms.util.excel.DeletedConverter;
import com.briup.cms.util.excel.ParentIdConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Accessors(chain = true)//不能有链式调用,和easyExcel冲突
@TableName("cms_category")
@ApiModel(value = "Category对象", description = "")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("栏目编号")
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @ApiModelProperty("栏目名称")
    @ExcelProperty("栏目名称")
    private String name;

    @ApiModelProperty("栏目描述")
    @ExcelProperty("栏目描述")
    private String description;

    @ApiModelProperty("栏目序号")
    @ExcelProperty(value = "栏目序号")
    private Integer orderNum;

    @JsonIgnore
    @ApiModelProperty("栏目删除状态")
    @TableLogic
    @ExcelProperty(value = "栏目删除状态", converter = DeletedConverter.class)
    private Integer deleted;

    @ApiModelProperty("父栏目id")
    @ExcelProperty(value = "父栏目", converter = ParentIdConverter.class)
    private Integer parentId;
}
