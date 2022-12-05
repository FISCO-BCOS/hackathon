package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业列表分页查询
 *
 * @author cmgun
 */
@Data
public class EntListParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize;

    private int pageNo;

    /**
     * 账户名称
     */
    private String userName;

    /**
     * 企业名称
     */
    private String entName;

    /**
     * 准入状态
     */
    private String accessStatus;

}
