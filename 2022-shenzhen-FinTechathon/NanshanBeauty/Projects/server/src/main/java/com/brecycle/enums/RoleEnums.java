package com.brecycle.enums;

import lombok.Getter;

/**
 * 角色类型
 */
@Getter
public enum RoleEnums {

    /**
     * 消费者
     */
    CUSTOMER("customer", 1),
    /**
     * 企业
     */
    CAR("car", 2),
    /**
     * 电池生产商
     */
    PRODUCTOR("productor", 3),
    /**
     * 电池租赁商
     */
    RENT("rent", 4),
    /**
     * 回收商
     */
    RECYCLE("recycle", 5),
    /**
     * 储能企业
     */
    STORED("stored", 6),
    /**
     * 安全评估
     */
    SAFE("safe", 7),
    /**
     * 监管机构
     */
    SUPERVISION("supervision", 8),
    /**
     * 平台管理员
     */
    ADMIN("admin", 9),
    /**
     * 原料商
     */
    MATERIAL("material", 10),
    /**
     * 企业审批中角色
     */
    AUDIT("audit", 11);


    /**
     * value
     */
    private final String value;

    /**
     * key
     */
    private final Integer key;

    RoleEnums(String value, Integer key) {
        this.value = value;
        this.key = key;
    }
}
