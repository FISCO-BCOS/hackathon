package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电池回收检测数据
 *
 * @author cmgun
 */
@Data
public class BatteryRecycleParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;

    /**
     * 回收商账户
     */
    private String recycleUserName;

    /**
     * 电池充放电次数
     */
    private Integer chargeTimes;

}
