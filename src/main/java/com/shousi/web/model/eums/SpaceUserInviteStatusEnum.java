package com.shousi.web.model.eums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum SpaceUserInviteStatusEnum {

    PENDING("待同意", 0),
    AGREE("同意", 1),
    REJECT("拒绝", 1);

    private final String text;

    private final int value;

    SpaceUserInviteStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static SpaceUserInviteStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (SpaceUserInviteStatusEnum spaceTypeEnum : SpaceUserInviteStatusEnum.values()) {
            if (spaceTypeEnum.value == value) {
                return spaceTypeEnum;
            }
        }
        return null;
    }
}
