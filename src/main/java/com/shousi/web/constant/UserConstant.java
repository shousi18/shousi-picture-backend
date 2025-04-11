package com.shousi.web.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";
    
    // endregion

    /**
     * 邮箱验证码
     */
    String EMAIL_CODE_KEY = "email:code:%s:%s";

    /**
     * 图形验证码
     */
    String CAPTCHA_KEY = "captcha:";

    /**
     * 密码加盐值
     */
    String DEFAULT_SALT = "shousi";
}
