package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易列表查询返回信息体
 *
 * @author cmgun
 */
@Data
public class TradeListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易编号
     */
    private Long id;

    /**
     * 交易内容
     */
    private String info;

    /**
     * 交易发起者
     */
    private String sellerName;

    private String status;

    private String buyerName;

    private BigDecimal lowestAmt;

    private BigDecimal tradeAmt;
}
