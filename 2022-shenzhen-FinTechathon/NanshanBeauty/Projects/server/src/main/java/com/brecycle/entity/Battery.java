package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 电池信息
 *
 * @author cmgun
 */
@Data
@TableName("battery")
public class Battery {

    /**
     * 电池编号
     */
    @TableId
    private String id;

    /**
     * 批次号
     */
    @TableField
    private String batchNo;

    /**
     * 合约地址
     */
    @TableField
    private String address;

    @TableField
    private String info;

    @TableField
    private Date createTime;

    @TableField
    private String code;

    @TableField
    private String type;

    @TableField
    private String vdc;

    @TableField
    private String kah;

    @TableField
    private String status;

    @TableField
    private Integer chargeTimes;

    @TableField
    private Long ownerId;

    @TableField
    private Date endTime;
}
