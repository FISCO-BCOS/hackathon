package com.find.fiscoshard2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineNode {

    private String nodeId;
    private String ip;
    private String state;
    private String address;
    private String repValue;


}
