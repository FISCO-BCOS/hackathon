package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cmgun
 */
@Data
public class EntRegistParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String name;

    private String phone;

    /**
     * 统一社会信用码
     */
    private String idno;

    /**
     * 地址
     */
    private String address;

    /**
     * 企业类型，见RoleEnums.key
     */
    private String type;

    /**
     * 其他信息
     * 电池生产商，需要batteryProductRegist
     * 车企，需要carProductRegist
     */
    private String info;
}
