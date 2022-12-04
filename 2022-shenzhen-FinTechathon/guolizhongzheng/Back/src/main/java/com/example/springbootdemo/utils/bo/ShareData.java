package com.example.springbootdemo.utils.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareData {
    private String patAdd;//患者地址
    private String eviAdd;//电子病历的地址
    private String Add;//医生的地址
    private int levle;//等级

}
