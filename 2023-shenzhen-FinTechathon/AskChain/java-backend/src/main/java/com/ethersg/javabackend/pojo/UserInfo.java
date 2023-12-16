package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ahy231
 * @date 2023/11/21 10:45
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    private String username;
    private String identity;
}
