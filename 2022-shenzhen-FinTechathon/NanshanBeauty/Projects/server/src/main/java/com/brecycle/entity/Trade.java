package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易类实体
 *
 * @author cmgun
 */
@Data
@TableName("trade")
public class Trade {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private Long sellerId;

    @TableField
    private Long buyerId;

    @TableField
    private BigDecimal lowestAmt;

    @TableField
    private BigDecimal expectAmt;

    @TableField
    private BigDecimal tradeAmt;

    @TableField
    private Integer bidDays;

    @TableField
    private Date createTime;

    @TableField
    private String tradeType;

    @TableField
    private String status;

    @TableField
    private String addr;

    @TableField
    private String info;
}
