package com.brecycle.enums;

import lombok.Getter;

/**
 * 用户类型枚举类
 */
@Getter
public enum UserType {

    /**
     * 消费者
     */
    CUSTOMER("1"),
    /**
     * 企业
     */
    ENTERPRISE("2");

    /**
     * value
     */
    private final String value;

    UserType(String value) {
        this.value = value;
    }
}
