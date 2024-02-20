package com.find.controller;

import com.find.pojo.TrainInfo;
import com.find.pojo.TrainInfoByOneContext;
import com.find.service.TrainDataService;
import com.find.service.impl.TrainDataServiceImpl;
import com.find.service.testServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: Su
 * Date: 2023/10/30
 */

@Controller
@RequestMapping("/res")
public class testController {

    @Autowired
    testServiceImpl testService;

    @Autowired
    TrainDataServiceImpl trainDataService;

    @Autowired
    TrainInfoByOneContext trainInfoByOneContext;

    @Autowired
    RocketMQTemplate rocketmqTemplate;

    private int i = 0;

    @GetMapping("/res1")
    public String res1(){
        return "res1===>";
    }


    @GetMapping("/res2")
    public  String res2(){
        return "res2===>";
    }

    @GetMapping("/res3")
    public  String res3(){
        return testService.res3();
    }

    @GetMapping("/list")
    public String listJobs(){
        System.out.println("接收到请求...");
        return "展示所有任务";
    }

    //测试查询用户某一次任务的客户端训练情况
    @GetMapping("/getTrainDataByUser")
    public String getTrainDataByUser(@RequestParam String username, @RequestParam String timestamp,
                                     @RequestParam String times,@RequestParam int work_num){
        System.out.println(username+timestamp+times+work_num);
        System.out.println("接收到请求...");
        List<TrainInfo> trainDataByUser = trainDataService.getTrainDataByUser(username, timestamp, work_num*Integer.parseInt(times));
//        return "0";
        System.out.println("列表情况:" + trainDataByUser.toString());
        for (TrainInfo trainInfo : trainDataByUser) {

            trainInfoByOneContext.putNode(trainInfo.getTimestamp()+trainInfo.getNodeId(),trainInfo);
        }
        //todo:
        return "redirect:/trainDataByOne";
    }

    //测试查询用户所有任务的发起训练与结果信息
    @PostMapping("/getUserTaskInfo")
    public String selectUserTaskInfo(@RequestParam String username){
        System.out.println("接收到请求...");
        List<TrainInfo> trainDataByUser = trainDataService.selectUserTaskInfo(username);
        return trainDataByUser.toString();
    }

    //通过PermissionService自定义授权实现
    @PostMapping("/create")
    public String createJob(){
        return "创建一个新任务";
    }

    //通过SpringSecurity配合用户角色(role字段)实现权限管理
    @DeleteMapping("/delete")
    public String deleteJob(){
        return "删除一个任务";
    }

    //通过SpringSecurity配合用户角色(role字段)实现权限管理
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        i++;
//        Date date = new Date();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
//        System.out.println(dateFormat.format(date));
        //todo:测试mq
        rocketmqTemplate.convertAndSend("client2Web:FL",i);
        return "test";
    }

}
