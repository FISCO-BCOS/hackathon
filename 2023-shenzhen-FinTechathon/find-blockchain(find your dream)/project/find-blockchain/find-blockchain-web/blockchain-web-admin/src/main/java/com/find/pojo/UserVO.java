package com.find.pojo;

import lombok.Data;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/30
 */

@Data
public class UserVO {
    private String username;
    private String password;
    private Integer rememberMe;
}
