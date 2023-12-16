package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ahy231
 * {@code @date} 2023/11/17 10:46
 * {@code @description} esg项目（包含id）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ESGProjectWithID {
    private BigInteger id;
    private String description;
    private List<String> fileUrl;
}
