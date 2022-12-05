package com.example.springbootdemo.utils.bo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public  class  ShareReport {
    private String patAdd;//患者地址
    private String repAdd;//电子体检记录的地址
    private String add;//医生的地址
    private int levle;//等级
}
