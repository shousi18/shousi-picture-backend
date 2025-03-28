package com.shousi.web.model.dto.tag;

import lombok.Data;

@Data
public class TagQueryRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;
}
