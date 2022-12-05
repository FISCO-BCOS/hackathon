package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 梯次利用申请提交
 *
 * @author cmgun
 */
@Data
public class SecUsedApplyParam implements Serializable {

    private static final long serialVersionUID = 1L;

    // 电池编号
    private String batteryId;

    /**
     * 是否有指定梯次利用商
     */
    private boolean hasTarget;

    /**
     * 指定梯次利用的名称
     */
    private String name;

    /**
     * 最低交易金额
     */
    private BigDecimal lowestAmt;

    /**
     * 期望金额
     */
    private BigDecimal expectAmt;

    /**
     * 竞价天数
     */
    private Integer bidDays;
}
