package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 交易关联的电池
 *
 * @author cmgun
 */
@Data
@TableName("trade_battery")
public class TradeBattery {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private Long tradeId;

    @TableField
    private String batteryId;
}
