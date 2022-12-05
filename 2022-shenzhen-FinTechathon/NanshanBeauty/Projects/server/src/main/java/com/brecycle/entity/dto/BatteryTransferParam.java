package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电池流转
 *
 * @author cmgun
 */
@Data
public class BatteryTransferParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;

    /**
     * 原始账户
     */
    private String originUserName;

    /**
     * 目标转移账户
     */
    private String toUserName;

    /**
     * 转移行为备注
     */
    private String remark;
}
