package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 竞价交易
 *
 * @author cmgun
 */
@Data
public class TradeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tradeId;

    private BigDecimal bidAmt;

    private String buyerUserName;
}
