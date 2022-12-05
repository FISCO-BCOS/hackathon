package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 汽车关联的电池
 *
 * @author cmgun
 */
@Data
@TableName("car_battery")
public class CarBattery {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField
    private String batteryId;

    @TableField
    private String carId;
}
