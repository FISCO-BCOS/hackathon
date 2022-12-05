package com.brecycle.enums;

import lombok.Getter;

/**
 * 用户状态枚举类
 */
@Getter
public enum UserStatus {

    /**
     * 初始化
     */
    DEFAULT("0"),
    /**
     * 正常
     */
    NORMAL("1"),
    /**
     * 禁用
     */
    FREEZE("2");

    /**
     * value
     */
    private final String value;

    UserStatus(String value) {
        this.value = value;
    }
}
