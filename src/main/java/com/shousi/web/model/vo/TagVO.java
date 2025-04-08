package com.shousi.web.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TagVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签使用次数
     */
    private Integer totalNum;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
