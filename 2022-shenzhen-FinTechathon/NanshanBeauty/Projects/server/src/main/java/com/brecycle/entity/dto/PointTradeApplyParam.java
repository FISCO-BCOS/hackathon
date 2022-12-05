package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 积分交易申请提交
 *
 * @author cmgun
 */
@Data
public class PointTradeApplyParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易积分数量
     */
    private BigDecimal point;

    /**
     * 是否有指定买方
     */
    private boolean hasTarget;

    /**
     * 指定 买方的名称
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
