package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 交易列表分页查询
 *
 * @author cmgun
 */
@Data
public class TradeListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize;

    private int pageNo;

    /**
     * 交易类型
     */
    private String tradeType;

    private String status;

    /**
     * 查询自身
     */
    private Long myId;
}
