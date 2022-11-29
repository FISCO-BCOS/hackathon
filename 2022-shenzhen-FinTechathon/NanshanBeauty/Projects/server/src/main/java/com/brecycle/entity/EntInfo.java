package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 企业信息
 *
 * @author cmgun
 */
@Data
@TableName("ent_info")
public class EntInfo {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField
    private Long userId;

    /**
     * 准入合约地址
     */
    @TableField
    private String accessContractAddr;

    /**
     * 准入状态
     */
    @TableField
    private String accessStatus;

    /**
     * 备注
     */
    @TableField
    private String remark;

    /**
     * 扩展字段
     */
    @TableField
    private String info;
}
