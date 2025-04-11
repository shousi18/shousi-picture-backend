package com.shousi.web.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号或者邮箱
     */
    private String userAccountOrEmail;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 验证码id
     */
    private String verifyCodeId;
}
