package com.brecycle.enums;

import lombok.Getter;

/**
 * 电池状态枚举类
 */
@Getter
public enum BatteryStatus {

    /**
     * 待安全审查
     */
    SAFE_CHECK("1"),
    /**
     * 正常
     */
    NORMAL("2"),
    /**
     * 待回收
     */
    WAIT_RECYCLE("3"),
    /**
     * 回收中
     */
    RECYCLE("4"),
    /**
     * 梯次利用
     */
    SECOND_USED("5"),
    /**
     * 拆解
     */
    END("6"),
    /**
     * 回收交易中，不可再进行交易绑定
     */
    RECYCLE_TRADING("7"),
    /**
     * 梯次利用交易中，不可再进行交易绑定
     */
    SECOND_USED_TRADING("8");

    /**
     * value
     */
    private final String value;

    BatteryStatus(String value) {
        this.value = value;
    }
}
