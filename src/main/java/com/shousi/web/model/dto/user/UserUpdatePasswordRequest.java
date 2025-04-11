package com.shousi.web.model.dto.user;

import lombok.Data;

@Data
public class UserUpdatePasswordRequest {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
