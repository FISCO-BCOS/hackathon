package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 资源
 *
 * @author cmgun
 */
@Data
@TableName("resource")
public class Resource {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源key
     */
    @TableField
    private String key;

    /**
     * 资源名称
     */
    @TableField
    private String name;
}
