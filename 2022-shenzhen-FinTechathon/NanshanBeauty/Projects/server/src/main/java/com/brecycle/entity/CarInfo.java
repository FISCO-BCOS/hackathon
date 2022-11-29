package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 汽车信息
 *
 * @author cmgun
 */
@Data
@TableName("car_info")
public class CarInfo {

    /**
     * 主键
     */
    @TableId
    private String id;

    @TableField
    private Long creatorId;

    @TableField
    private Date createTime;

    @TableField
    private String kah;
}
