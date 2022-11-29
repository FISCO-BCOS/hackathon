package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 企业列表查询返回信息体
 *
 * @author cmgun
 */
@Data
public class EntListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 账户名称
     */
    private String userName;

    private String entName;

    private String idno;

    private String accessStatus;

    private List<String> fileId;
}
