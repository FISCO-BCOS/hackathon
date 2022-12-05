package com.example.springbootdemo.utils.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attend {
    private String eviAdd;
    private String symptom;//症状
    private String vital;//生命体征
    private String prescribe;//开药处方
    private String advice;//医嘱
    private String operator;//手术操作
    private String docAdd;//医生地址
    private int value;//电子病历价值
    private int scarity;
}

