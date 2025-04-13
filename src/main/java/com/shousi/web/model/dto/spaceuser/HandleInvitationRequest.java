package com.shousi.web.model.dto.spaceuser;

import lombok.Data;

@Data
public class HandleInvitationRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 处理状态
     */
    private Integer status;
}
