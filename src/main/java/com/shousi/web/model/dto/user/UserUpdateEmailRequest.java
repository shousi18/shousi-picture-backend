package com.shousi.web.model.dto.user;

import lombok.Data;

@Data
public class UserUpdateEmailRequest {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String newEmail;

    /**
     * 邮箱验证码
     */
    private String code;
}
