package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 车企组装电池进汽车的参数
 *
 * @author cmgun
 */
@Data
public class BatteryCarInfoParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 汽车生产编号
     */
    private String carBatchNo;

    /**
     * 千安时
     */
    private String kah;

    /**
     * 生产时间 yyyy-MM-dd HH:mm:ss
     */
    private String createTime;

    /**
     * 车企账户
     */
    private String carEntUserName;

    /**
     * 关联电池编号
     */
    private List<String> batteryIds;
}
