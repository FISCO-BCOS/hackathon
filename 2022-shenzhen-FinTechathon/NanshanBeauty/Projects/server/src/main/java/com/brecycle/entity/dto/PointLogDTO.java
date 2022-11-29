package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 积分记录信息
 *
 * @author cmgun
 */
@Data
public class PointLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String addr;

    public String optTime;

    public BigDecimal beforePoint;

    public BigDecimal afterPoint;

    public String remark;
}
