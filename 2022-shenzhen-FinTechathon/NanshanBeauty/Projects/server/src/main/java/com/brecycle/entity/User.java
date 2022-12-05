package com.brecycle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户类实体
 *
 * @author cmgun
 */
@Data
@TableName("user")
public class User {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField
    private String userName;

    /**
     * 密码
     */
    @TableField
    private String password;

    /**
     * 私钥
     */
    @TableField
    private String privateKey;

    /**
     * weId
     */
    @TableField
    private String weId;

    /**
     * 账户状态：0-初始化，1-正常，2-审批中，3-禁用
     */
    @TableField
    private String status;

    /**
     * 账户地址
     */
    @TableField
    private String addr;

    /**
     * 身份证
     */
    @TableField
    private String idno;

    /**
     * 手机号
     */
    @TableField
    private String mobile;

    /**
     * 姓名
     */
    @TableField
    private String name;

    /**
     * 用户类型
     */
    @TableField
    private String userType;

    /**
     * 联系地址
     */
    @TableField
    private String address;
}
