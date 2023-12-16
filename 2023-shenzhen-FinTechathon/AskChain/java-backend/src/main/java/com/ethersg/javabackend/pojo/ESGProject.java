package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ESG项目介绍
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ESGProject {
    private String property; // E/S/G
    private String esgName;
    private String description;
    private List<String> fileUrl;
}
