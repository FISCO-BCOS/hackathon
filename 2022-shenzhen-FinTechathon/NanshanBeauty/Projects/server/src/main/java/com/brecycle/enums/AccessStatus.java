package com.brecycle.enums;

import lombok.Getter;

/**
 * 准入状态枚举类
 */
@Getter
public enum AccessStatus {

    /**
     * 无需准入
     */
    DEFAULT("0"),
    /**
     * 等待准入申请
     */
    WAIT_APPLY("1"),
    /**
     * 审批中
     */
    AUDIT("2"),
    /**
     * 审批拒绝
     */
    REJECT("3"),
    /**
     * 审批通过
     */
    PASS("4");

    /**
     * value
     */
    private final String value;

    AccessStatus(String value) {
        this.value = value;
    }
}
