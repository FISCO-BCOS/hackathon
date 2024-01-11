package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ahy231
 * @date 2023/11/21 12:06
 * @description ESG评分查询
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ESGScoreList {
    private BigInteger id;
    private String organization;
    private String companyName;
    private String property;
    private String esgName;
    private String esgDescription;
    private List<String> fileUrl;
    private BigInteger score;
    private String companyAddress;
    private String standard;

    public ESGScoreList(ESGScoreList esgScoreList) {
        this(
                esgScoreList.getId(),
                esgScoreList.getOrganization(),
                esgScoreList.getCompanyName(),
                esgScoreList.getProperty(),
                esgScoreList.getEsgName(),
                esgScoreList.getEsgDescription(),
                esgScoreList.getFileUrl(),
                esgScoreList.getScore(),
                esgScoreList.getCompanyAddress(),
                esgScoreList.getStandard()
        );
    }
}
