package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电池溯源信息
 *
 * @author cmgun
 */
@Data
public class TraceInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作账户
     */
    private String address;

    /**
     * 操作备注
     */
    private String remark;

    /**
     * 操作时间
     */
    private String optTime;
}
