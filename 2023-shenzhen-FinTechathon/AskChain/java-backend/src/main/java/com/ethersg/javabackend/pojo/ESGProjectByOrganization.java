package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ahy231
 * @date 2023/11/21 11:47
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ESGProjectByOrganization {
    private BigInteger id;
    private String companyName;
    private String property;
    private String esgName;
    private String esgDescription;
    private List<String> fileUrl;
    private BigInteger score;
    private String companyAddress;
}
