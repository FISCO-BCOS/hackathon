package com.find.component;

import com.alibaba.fastjson.JSONObject;
import com.find.dao.AccountDao;
import com.find.pojo.OnlineNode;
import com.find.pojo.OnlineNodeContext;
import com.find.service.FlService;
import com.find.util.Constant;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * websocket 相关业务，有冗余，部分方法暂时没用上，代码来自网络
 */
@ServerEndpoint("/webSocket_node/{nodeId}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static final ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    //concurrent包的线程安全Set，用来存放每个客户端对应的实体类对象。
    private static final ConcurrentHashMap<String, OnlineNode> ClientPools = new ConcurrentHashMap<>();

    private static OnlineNodeContext onlineNodeContext;

    private static AccountDao accountDao;

    private static FlService flService;

    private static RocketMQTemplate rocketmqTemplate;

    @Autowired
    public void setAccountDao(AccountDao accountDao){
        WebSocketServer.accountDao = accountDao;
    }

    @Autowired
    public void setOnlineNodeContext(OnlineNodeContext onlineNodeContext){
        WebSocketServer.onlineNodeContext = onlineNodeContext;
    }

    @Autowired
    public void setRocketmqTemplate(RocketMQTemplate rocketmqTemplate){
        WebSocketServer.rocketmqTemplate = rocketmqTemplate;
    }

    @Lazy
    @Autowired
    public void setFlService(FlService flService){
        WebSocketServer.flService = flService;
    }

    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if(session != null){
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    //获取连接池中的所有的用户nodeId
    public List<String> getUserId(){
        ArrayList<String> userIdList = new ArrayList<>();
        for (Map.Entry<String, Session> entry : sessionPools.entrySet()) {
            userIdList.add(entry.getKey());
        }
        return userIdList;
    }

    //获取连接池中的所有的用户信息
    public List<OnlineNode> getUser(){
        ArrayList<OnlineNode> users = new ArrayList<>();
        for (Map.Entry<String, OnlineNode> entry : ClientPools.entrySet()) {
            users.add(entry.getValue());
        }
        return users;
    }

    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        System.out.println("进入sendInfo");
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "nodeId") String userName){
        sessionPools.put(userName, session);
        addOnlineCount();
        System.out.println(userName + "加入webSocket_node！当前人数为" + onlineNum);
        try {
            sendMessage(session, "欢迎" + userName + "加入连接！");
            ClientPools.put(userName,new OnlineNode());
            Integer.compare(1,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "nodeId") String userName){
        sessionPools.remove(userName);
        ClientPools.remove(userName);
        subOnlineCount();
        //更新在线节点和训练中的联邦学习节点
        flService.offline(userName);
        System.out.println(userName + "断开webSocket连接！当前人数为" + onlineNum);
    }

    //收到客户端或者聚合服务端信息
    @OnMessage
    public void onMessage(String message){
        System.out.println("客户端：" + message + ",已收到");
        org.json.JSONObject jsonObject = null;
        try {
            jsonObject = new org.json.JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 判断 time 属性是否为空
        if (jsonObject.isNull("times")) {
            //没有times属性，是查询在线节点
            if (message != null){
                OnlineNode onlineNode = JSONObject.parseObject(message, OnlineNode.class);
                ClientPools.put(onlineNode.getNodeId(),onlineNode);
                onlineNodeContext.putNode(onlineNode.getNodeId(),onlineNode);
                accountDao.modifyonlineStateById(onlineNode.getNodeId(),"在线", onlineNode.getIp(), onlineNode.getLon(), onlineNode.getLat());
                System.out.println(onlineNodeContext.getAllNode().toString());
            }
//            System.out.println("nodeId 属性为空");
        } else {
            //todo有times属性，是发起联邦学习训练
            if (message != null && !jsonObject.get("nodeId").toString().equals("server0")){
                //客户端发来的数据，发到rocketmq队列里面
//                Constant.CLIENT_DATA_LIST.add(message);
                Constant.TrainList.add(message);
                rocketmqTemplate.convertAndSend("client2Web:FL",message);
            }
        }
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

    public void nodeInCall(String message) throws IOException {

        for (Session session : sessionPools.values()) {
            if (session != null) {
                synchronized (session) {
                    session.getBasicRemote().sendText(message);
                }
            }
        }
    }
}
