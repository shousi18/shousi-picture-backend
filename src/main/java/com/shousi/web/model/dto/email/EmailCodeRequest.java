package com.shousi.web.model.dto.email;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailCodeRequest implements Serializable {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱类型
     */
    private String type;

    private static final long serialVersionUID = -6945564482659139823L;
}
