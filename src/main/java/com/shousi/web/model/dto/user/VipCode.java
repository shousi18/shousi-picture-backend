package com.shousi.web.model.dto.user;

import lombok.Data;

/**
 * 兑换码
 */
@Data
public class VipCode {

    /**
     * 兑换码
     */
    private String code;

    /**
     * 是否已经使用
     */
    private boolean hasUsed;
}