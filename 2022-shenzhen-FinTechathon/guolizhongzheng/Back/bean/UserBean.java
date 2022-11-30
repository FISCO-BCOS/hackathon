package com.zgxt.springbootdemo.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Binge
 * @desc
 * @date 2022/9/20 9:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBean {
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
}