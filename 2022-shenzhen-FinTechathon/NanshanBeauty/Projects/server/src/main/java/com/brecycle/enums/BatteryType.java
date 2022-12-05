package com.brecycle.enums;

import lombok.Getter;

/**
 * 电池类型枚举类
 * VRLA-铅酸动力电池；NCB-镍镉动力电池；Grap-石墨烯电池；Li-锂电池
 */
@Getter
public enum BatteryType {

    /**
     * 铅酸动力电池
     */
    VRLA("VRLA"),
    /**
     * 镍镉动力电池
     */
    NCB("NCB"),
    /**
     * 石墨烯电池
     */
    Grap("Grap"),
    /**
     * 锂电池
     */
    Li("Li");

    /**
     * value
     */
    private final String value;

    BatteryType(String value) {
        this.value = value;
    }
}
