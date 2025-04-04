package com.shousi.web.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditByBatchRequest implements Serializable {

    /**
     * 图片 id 列表
     */
    private List<Long> pictureIdList;

    /**
     * 空间 id
     */
    private Long spaceId;

    /**
     * 命名规则
     */
    private String nameRule;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 标签
     */
    private List<Long> tagIds;

    private static final long serialVersionUID = 1L;
}
