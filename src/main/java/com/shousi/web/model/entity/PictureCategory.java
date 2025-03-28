package com.shousi.web.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 图片分类关联表
 * @TableName picture_category
 */
@TableName(value ="picture_category")
@Data
public class PictureCategory implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 图片id
     */
    private Long pictureId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}