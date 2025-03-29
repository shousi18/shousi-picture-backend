package com.shousi.web.model.dto.category;

import lombok.Data;

@Data
public class CategoryQueryRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String categoryName;
}
