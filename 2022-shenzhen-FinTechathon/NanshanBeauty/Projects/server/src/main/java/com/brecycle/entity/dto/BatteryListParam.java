package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cmgun
 */
@Data
public class BatteryListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize;

    private int pageNo;
    /**
     * 电池编号
     */
    private String id;
}
