package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ahy231
 * @date 2023/11/21 11:32
 * @description ESG详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESGDetail {
    private BigInteger id;
    private String property;
    private String esgName;
    private String esgDescription;
    private BigInteger score;
    private List<String> files;
}
