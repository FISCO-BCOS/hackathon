package com.find.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineNode {

    private String nodeId;
    private String state;
    private String name;
    private String ip;
    private double repValue;
    //所属的域
    private String domain;
    private String security;
    //经纬度信息
    private String lon;
    private String lat;

}
