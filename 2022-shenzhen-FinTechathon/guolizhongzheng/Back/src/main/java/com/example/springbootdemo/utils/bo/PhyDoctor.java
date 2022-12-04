package com.example.springbootdemo.utils.bo;
//录入体检报告:

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhyDoctor {
    private String reportadd;//体检报告地址
    private String docAdd;//医生地址
    private String bloodpress;//血压
    private String hemoglobin;//血红蛋白
    private String urinalysis;//尿检
    private String bmi;//
    private String hemameba;//白细胞
    private int value;//价值
    private int scarity;//稀缺程度
}
