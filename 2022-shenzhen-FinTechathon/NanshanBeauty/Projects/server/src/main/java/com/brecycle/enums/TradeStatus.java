package com.brecycle.enums;

import lombok.Getter;

/**
 * 交易状态
 */
@Getter
public enum TradeStatus {

    /**
     * 竞价中
     */
    BIDING("1"),
    /**
     * 交易成功
     */
    SUCCESS("2"),
    /**
     * 撤回
     */
    REJECT("3");

    /**
     * value
     */
    private final String value;

    TradeStatus(String value) {
        this.value = value;
    }
}
