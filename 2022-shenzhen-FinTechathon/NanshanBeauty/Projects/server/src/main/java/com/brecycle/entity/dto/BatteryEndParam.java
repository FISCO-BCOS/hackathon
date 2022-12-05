package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 电池拆解
 *
 * @author cmgun
 */
@Data
public class BatteryEndParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;

    /**
     * 原始账户，即梯次利用企业或回收商
     * 当originUserName=toUserName时，不进行电池流转
     */
    private String originUserName;

    /**
     * 回收商账户
     */
    private String toUserName;

    /**
     * 是否第二阶段回收
     * 如果是，则默认原始账户为梯次利用企业，需要对其进行积分派发
     * 否，则不对原始账户进行积分派发
     */
    private Boolean secondUsed;

    /**
     * 电池充放电次数
     */
    private Long chargeTimes;

    /**
     * 锂回收率
     */
    private BigDecimal LiRatio;

    /**
     * 锰回收率
     */
    private BigDecimal MnRatio;

    /**
     * 钴回收率
     */
    private BigDecimal CoRatio;

    /**
     * 镍回收率
     */
    private BigDecimal NiRatio;

    /**
     * 其他元素回收率
     */
    private BigDecimal otherRatio;

    /**
     * 拆解时间 yyyy-MM-dd HH:mm:ss
     */
    private String endTime;
}
