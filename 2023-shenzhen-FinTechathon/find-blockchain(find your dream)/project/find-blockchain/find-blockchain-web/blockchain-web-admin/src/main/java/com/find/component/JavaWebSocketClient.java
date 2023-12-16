package com.find.component;
import java.net.URI;

import com.find.config.QueueEnum;
import com.find.service.FlService;
import com.find.service.impl.FlServiceImpl;
import com.find.util.Constant;
import lombok.SneakyThrows;
import org.glassfish.tyrus.client.ClientManager;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import sun.rmi.runtime.Log;
import org.springframework.amqp.core.Message;

import javax.annotation.PostConstruct;
import javax.websocket.WebSocketContainer;

/**
 * @Author Su
 * @Date 2023/7/8 15:35
 * @Version 1.0 （版本号）
 */

public class JavaWebSocketClient extends WebSocketClient {

//    private static Logger logger = LoggerFactory.getLogger(JavaWebSocketClient.class);

    public JavaWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println(">>>>>>>>>>>websocket open");
    }

    @SneakyThrows
    @Override
    public void onMessage(String message) {
        System.out.println(">>>>>>>>>> websocket message");
        //处理收到消息后的逻辑
//        System.out.println(message);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        System.out.println(jsondata);
        // 判断 time 属性是否为空
        System.out.println(jsonObject);
//        JSONObject id = jsonObject.getJSONObject("id");
//        System.out.println(id);
        if (jsonObject.isNull("times")) {
            //没有times属性，是查询在线节点
            if (message != null){
                Constant.MessageList.add(message);
            }
//            System.out.println("nodeId 属性为空");
        } else {
            //todo有times属性，是发起联邦学习训练
            if (message != null && !jsonObject.get("nodeId").toString().equals("server0")){
                System.out.println(jsonObject.get("nodeId").toString().equals("server0"));
                //发到mq队列里面,更改触发发到mq代码的标识
                Constant.CLIENT_DATA_LIST.add(message);
                Constant.TrainList.add(message);
            }
//            System.out.println("time 属性不为空");
        }
//
        //断开连接
        close();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println(">>>>>>>>>>>websocket close");
    }

    @Override
    public void onError(Exception e) {
        System.out.println(">>>>>>>>>websocket error {}" + e);
    }


}

