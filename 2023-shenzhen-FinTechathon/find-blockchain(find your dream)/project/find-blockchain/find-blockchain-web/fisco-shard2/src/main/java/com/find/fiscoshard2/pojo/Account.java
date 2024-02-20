package com.find.fiscoshard2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    //联邦学习节点id
    private String nodeId;
    //账户地址
    private String address;
    //信誉值
    private Float repValue;

}
