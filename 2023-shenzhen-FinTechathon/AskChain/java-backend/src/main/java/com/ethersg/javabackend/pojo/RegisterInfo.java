package com.ethersg.javabackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ahy231
 * @date 2023/11/21 10:12
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInfo {
    private String accountType;
    private String organizationCode;
    private String location;
    private String field;
    private String name;
    private String password;
}
