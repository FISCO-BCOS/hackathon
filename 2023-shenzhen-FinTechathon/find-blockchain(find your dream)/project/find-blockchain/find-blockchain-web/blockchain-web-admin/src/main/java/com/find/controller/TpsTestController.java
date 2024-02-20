package com.find.controller;


import com.find.*;

import com.find.component.RabbitMqSender;
import com.find.service.FlService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/test")
public class TpsTestController {

    @Autowired
    RabbitMqSender rabbitMqSender; // rabbitMQ 发送服务

    @Autowired
    FlService flService;

    private Long startTimestamp;
    private static AtomicBoolean firstRequest;
    AtomicInteger totalReq ;
    Integer totalCount;
    Integer totalErrorCount;

    @PostConstruct
    public void init() {
        startTimestamp = System.currentTimeMillis();
        firstRequest = new AtomicBoolean(false);;
        totalReq = new AtomicInteger(0);
        totalCount = 0;
        totalErrorCount = 0;
    }

    private void resetCounter(){
        firstRequest.set(false);
        totalReq.set(0);
        totalCount = 0;
        totalErrorCount = 0;
    }


//    @Reference(loadbalance="roundrobin")
//    SyncContractService syncContractService;
//
//
//    @Reference(loadbalance="roundrobin")
//    AsyncContractService asyncContractService;
//
//
//    @Reference(loadbalance="roundrobin")
//    ParallelContractService parallelContractService;

//    @Reference
//    NodeManaging nodeManaging;

//    @RequestMapping("/syncInsertTest")
//    public String syncInsertTest(){
//
//        if (firstRequest.compareAndSet(false , true)){
//            startTimestamp = System.currentTimeMillis();
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(startTimestamp))));      // 时间戳转换成时间
//            System.out.println("开始请求时间格式化结果：" + sd);
//        }
//
//        totalReq.incrementAndGet();
//
//        syncContractService.createData();
//
//        return "done";
//    }
//
//    @RequestMapping("/asyncInsertTest")
//    public String asyncInsertTest(){
//
//        if (firstRequest.compareAndSet(false , true)){
//            startTimestamp = System.currentTimeMillis();
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(startTimestamp))));      // 时间戳转换成时间
//            System.out.println("开始请求时间格式化结果：" + sd);
//        }
//
//        totalReq.incrementAndGet();
//
//        asyncContractService.createData();
//
//        return "done";
//    }
//
//    @RequestMapping("/parallelInsertTest")
//    public String parallelInsertTest(){
//
//        if (firstRequest.compareAndSet(false , true)){
//            startTimestamp = System.currentTimeMillis();
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(startTimestamp))));      // 时间戳转换成时间
//            System.out.println("开始请求时间格式化结果：" + sd);
//        }
//
//        totalReq.incrementAndGet();
//
//        parallelContractService.createData();
//
//        return "done";
//    }
//
//    @RequestMapping("/controllerTest")
//    public String controllerTest(){
//        return "controllerTest success";
//    }
//
//    @RequestMapping("/tpsCount")
//    public String tpsCount(@RequestParam("shardCount") String shardCount, @RequestParam("errorCount") String errorCount){
//
//        totalCount += Integer.parseInt(shardCount);
//        totalErrorCount += Integer.parseInt(errorCount);
//
//        if (totalCount >= totalReq.get()){
//            Long endTimestamp = System.currentTimeMillis();
//
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(endTimestamp))));      // 时间戳转换成时间
//            System.out.println("结束请求时间格式化结果：" + sd);
//
//
//
//            Long totalTime = endTimestamp - startTimestamp;
//
//            System.out.println("Total transactions:  " + String.valueOf(totalCount));
//            System.out.println("Total time: " + String.valueOf(totalTime) + "ms");
//            System.out.println(
//                    "TPS(include error requests): "
//                            + String.valueOf(totalCount / ((double) totalTime / 1000)));
//            System.out.println(
//                    "TPS(exclude error requests): "
//                            + String.valueOf(
//                            (double) (totalCount - totalErrorCount) / ((double) totalTime / 1000)));
//
//
//            System.out.println("totalReq is :" + String.valueOf(totalReq.get()) );
//
//
//            this.resetCounter();
//        }
//
//
//        return "tpsCount success";
//    }

//    @RequestMapping("/observerRegister")
//    public String observerRegister(@RequestParam("nodeId") String nodeId){
//
//        try {
//            nodeManaging.ObserverRegister(nodeId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "done";
//    }
//
//
//    @RequestMapping("/getOberverList")
//    public String getOberverList(){
//
//
//
//        return nodeManaging.getObserverList();
//    }



    @RequestMapping("/testdownload")
    public String testdownload(){

//        rabbitMqSender.sendModel2Cli("testdownload");
        return "done";
    }

    @RequestMapping("/startTraining")
    public String startTraining(){
        flService.startTraining(1,3,1d);

        return "done";
    }


}
