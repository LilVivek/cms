package com.briup.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("cms_slideshow")
@ApiModel(value = "Slideshow对象", description = "")
public class Slideshow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("轮播图编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("轮播图片url")
    private String url;

    @ApiModelProperty("图片状态")
    private String status;

    @ApiModelProperty("删除状态")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;
}
