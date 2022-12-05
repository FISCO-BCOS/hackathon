package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 电池安全认证
 *
 * @author cmgun
 */
@Data
public class BatterySafeCheckParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电池编号
     */
    private String id;

    private String remark;

    /**
     * 安全认证商账户名
     */
    private String safeCheckUserName;

}
