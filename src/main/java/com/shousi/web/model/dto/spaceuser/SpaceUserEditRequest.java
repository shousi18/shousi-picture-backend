package com.shousi.web.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceUserEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 空间角色：viewer/editor/admin
     */
    private String spaceRole;

    /**
     * 邀请状态
     */
    private Integer inviteStatus;

    /**
     * 空间创建人id
     */
    private Long createUserId;

    private static final long serialVersionUID = 1L;
}

