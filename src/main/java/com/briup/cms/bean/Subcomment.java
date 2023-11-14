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
@TableName("cms_subcomment")
@ApiModel(value = "Subcomment对象", description = "")
public class Subcomment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String content;

    private LocalDateTime publishTime;

    private Long userId;

    @ApiModelProperty("一级评论id")
    private Long parentId;

    @ApiModelProperty("回复评论id")
    private Long replyId;

    @TableLogic
    private Integer deleted;
}
