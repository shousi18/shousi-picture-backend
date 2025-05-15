package com.shousi.web.model.eums;

import lombok.Getter;

/**
 * Lua脚本状态枚举
 */
@Getter
public enum LuaStatusEnum {
    // 成功
    SUCCESS(1L),
    // 失败
    FAIL(-1L),
    ;

    private final long value;

    LuaStatusEnum(long value) {
        this.value = value;
    }

}

