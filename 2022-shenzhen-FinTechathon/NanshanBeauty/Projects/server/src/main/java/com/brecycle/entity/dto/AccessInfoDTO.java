package com.brecycle.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cmgun
 */
@Data
public class AccessInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String entName;

    private String accessContractAddr;

    private String approvalEntAddr;

    private String approvalEntName;

    private String remark;

    private String accessStatus;
}
