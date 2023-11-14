package com.briup.cms.bean;

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
@TableName("cms_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("文章发表时间")
    private LocalDateTime publishTime;
}
