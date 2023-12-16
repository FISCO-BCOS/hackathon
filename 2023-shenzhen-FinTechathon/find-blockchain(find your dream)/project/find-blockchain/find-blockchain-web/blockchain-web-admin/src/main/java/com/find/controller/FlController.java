package com.find.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.find.NodeSecurService;
import com.find.annotation.ClearAndReloadCache;
import com.find.annotation.VerifyParam;
import com.find.component.RabbitMqReceiver;
import com.find.component.WebSocketServer;
import com.find.enums.VerifyRegexEnum;
import com.find.pojo.FlNode;
import com.find.pojo.OnlineNode;
import com.find.pojo.OnlineNodeContext;
import com.find.service.FlService;
import com.find.util.Constant;
import com.find.util.HttpUtil;
import org.apache.dubbo.config.annotation.Reference;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/fl")
@Controller
public class FlController {

    @Autowired
    FlService flService;

    @Autowired
    OnlineNodeContext onlineNodeContext;

    @Autowired
    WebSocketServer webSocketServer;

    public static String sessionId = "111";

    public int i = 1;

    @Reference(mock = "com.find.service.impl.MockNodeSecurServiceImpl",loadbalance = "roundrobin")
    NodeSecurService nodeSecurService;

    //安全节点认证-可信接入测试接口
    @GetMapping("/querySecurity")
    @ClearAndReloadCache(name = "update")
    public String querySecurity(){

//        WebSocketServer server = new WebSocketServer();
        webSocketServer.sendInfo("1","client1_1");

        //执行可信节点验证
        ArrayList<String> nodeList = new ArrayList<>();
        System.out.println("进入查询接口");
        //获得可信节点的id列表
        List<String> securityNodeId = flService.querySecurity(nodeList);
        flService.UpdateNode(securityNodeId);
        return "redirect:/nodemanager";
    }

    //节点抢占FL训练任务接口
    @GetMapping("/requestTask")
    public String requestTask(@RequestParam("nodeId")  @VerifyParam(required = true,NotNull = true) String nodeId){
        nodeId = nodeId + i;
        i++;
//        System.out.println(nodeId);
        System.out.println("进入抢占FL训练任务接口");
        flService.requestTask(nodeId);
        return "redirect:/nodemanager";
    }

    //查询安全节点接口——查询redis
    @RequestMapping("/querySecurityNode")
    @ResponseBody
    public String querySecurityNode(){

//        webSocketServer.sendInfo("1","client1_1");
        //执行可信节点验证
        System.out.println("进入查询接口");
        //获得可信节点的id列表，返回的全是可信的节点，todo:返回类型改成List
        String securityNodeId = flService.querySecurityNode();
//        System.out.println(securityNodeId.toString());
        return securityNodeId;
    }

    //更新安全节点列表——定时任务
    @RequestMapping("/updateSecurityNode")
    @ResponseBody
    public String updateSecurityNode(){

        //更新安全节点列表
        System.out.println("进入更新安全节点列表");
        //获得可信节点的id列表，返回的全是可信的节点
        String securityNodeId = flService.updateSecurityNode();
//        System.out.println(securityNodeId.toString());
        return securityNodeId;
    }

    //发起车联网联邦学习训练
    @RequestMapping("/startTraining")
    public String startTraining(@RequestParam("rounds") Integer rounds,  @RequestParam("nodeNums") Integer nodeNums,
                                @RequestParam("reward")  @VerifyParam(required = true,regex = VerifyRegexEnum.MONEY)Double reward,
                                @RequestParam("topology") String topology){
        //todo:节点数不够任务需要，直接弹窗说明节点数量设置超过在线的节点数
        if(onlineNodeContext.getAllNode().size() < nodeNums){
            return "redirect:/taskrelease";
        }else{
            flService.startTraining(nodeNums,rounds,reward);
            //更新round值
            RabbitMqReceiver.round = rounds;
            return "redirect:/systemmonitor";
        }
    }

    //发起查询在线终端
    @RequestMapping("/queryOnline")
    public String queryOnline(HttpServletRequest request){

        //清空消息收集列表
        Constant.MessageList.clear();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
//                System.out.println(cookie.getComment());
                String name = cookie.getName();
                String value = cookie.getValue();
                sessionId = name+ "=" +value;
//                System.out.println(name + ":" + value);
            }
        }
        flService.queryOnlineNode();
        return "redirect:/nodemanager";
    }

}
