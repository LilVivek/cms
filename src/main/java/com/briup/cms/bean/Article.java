package com.briup.cms.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
//@Accessors(chain = true)
@TableName("cms_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)//在java对象的属性序列化成Json数据时，指定转换成String类型
    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("文章审核状态")
    private String status;

    @ApiModelProperty("阅读量")
    private Integer readNum;

    @ApiModelProperty("点赞量")
    private Integer likeNum;

    @ApiModelProperty("拉踩量")
    private Integer dislikeNum;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("类别id")
    private Integer categoryId;

    @ApiModelProperty("是否收费，默认0不收费")
    private Integer charged;

    @ApiModelProperty("文章删除状态")
    @TableLogic
    private Integer deleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("文章发表时间")
    private LocalDateTime publishTime;
}
