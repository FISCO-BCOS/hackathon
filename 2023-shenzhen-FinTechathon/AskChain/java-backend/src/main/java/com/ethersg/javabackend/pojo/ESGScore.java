package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author ahy231
 * @date 2023/11/19 22:12
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESGScore {
    private String companyAddress;
    private BigInteger projectId;
    private BigInteger score;
    private String standard;
}
