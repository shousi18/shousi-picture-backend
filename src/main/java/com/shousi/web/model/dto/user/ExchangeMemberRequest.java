package com.shousi.web.model.dto.user;

import lombok.Data;

@Data
public class ExchangeMemberRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 会员码
     */
    private String code;
}
