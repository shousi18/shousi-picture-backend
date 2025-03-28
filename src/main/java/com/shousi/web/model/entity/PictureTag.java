package com.shousi.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 图片标签关联表
 * @TableName picture_tag
 */
@TableName(value ="picture_tag")
@Data
public class PictureTag implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 图片id
     */
    private Long pictureId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}