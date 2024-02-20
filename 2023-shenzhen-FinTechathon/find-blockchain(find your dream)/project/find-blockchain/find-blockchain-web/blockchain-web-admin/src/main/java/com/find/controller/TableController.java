package com.find.controller;


import com.alibaba.fastjson.JSONObject;

import com.find.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/table")
public class TableController {

    @Autowired
    FlNodeContext nodeContext;

    @Autowired
    OnlineNodeContext onlineNodeContext;

    @Autowired
    FlOnlineNodeContext CarFlNodeContext; // 节点上下文

    @Autowired
    TrainInfoContext trainInfoContext;

    @Autowired
    TrainInfoByOneContext trainInfoByOneContext;

    @RequestMapping("/getAllNodes")
    public Object getAllNodes(int page,int limit){

        Table table = new Table(0, "", CarFlNodeContext.getSize(),CarFlNodeContext.getJsonNodeList(page,limit));
        return JSONObject.toJSON(table);

    }

    @RequestMapping("/getOnlineNodes")
    public Object getOnlineNodes(int page,int limit){
        System.out.println("page is "+ String.valueOf(page));
        System.out.println("limit is "+ String.valueOf(limit));
        Table table = new Table(0, "", onlineNodeContext.getSize(),onlineNodeContext.getJsonNodeList(page, limit));
        return JSONObject.toJSON(table);
    }


    @RequestMapping("/getUserTaskInfo")
    public Object getUserTaskInfo(int page,int limit){
        System.out.println("page is "+ String.valueOf(page));
        System.out.println("limit is "+ String.valueOf(limit));
        Table table = new Table(0, "", trainInfoContext.getSize(),trainInfoContext.getJsonNodeList(page, limit));
        return JSONObject.toJSON(table);
    }

    @RequestMapping("/gettrainDataByOne")
    public Object gettrainDataByOne(int page,int limit){
        System.out.println("page is "+ String.valueOf(page));
        System.out.println("limit is "+ String.valueOf(limit));
        Table table = new Table(0, "", trainInfoByOneContext.getSize(),trainInfoByOneContext.getJsonNodeList(page, limit));
        return JSONObject.toJSON(table);
    }

}
