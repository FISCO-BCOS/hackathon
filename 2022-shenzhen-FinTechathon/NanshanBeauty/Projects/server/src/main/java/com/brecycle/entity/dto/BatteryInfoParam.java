package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电池信息
 *
 * @author cmgun
 */
@Data
public class BatteryInfoParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;

    /**
     * 批次号
     */
    private String batchNo;

    private String info;

    private String code;

    private String type;

    private String vdc;

    private String kah;

    private Integer chargeTimes;

    /**
     * 生产商账户名
     */
    private String productorUserName;
}
