package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户关联文件
 * @author cmgun
 */
@Data
@TableName("user_file")
public class UserFile {

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
     * 文件id
     */
    @TableField
    private String fileId;
}