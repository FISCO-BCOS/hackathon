package com.brecycle.enums;

import lombok.Getter;

/**
 * 交易类型
 */
@Getter
public enum TradeType {

    /**
     * 回收委托交易
     */
    RECYCLE("1"),
    /**
     * 梯次利用委托交易
     */
    SECOND_USED("2"),
    /**
     * 积分交易
     */
    POINT("3");

    /**
     * value
     */
    private final String value;

    TradeType(String value) {
        this.value = value;
    }
}
