package com.find.component;

//import com.find.service.MqService;
import com.find.NodeSecurService;
import com.find.service.FlService;
import com.find.util.Constant;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import com.rabbitmq.client.Channel;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description 消息接收者
 * @Author Su
 * @Date 2021/9/23 14:27
 * @Version 1.0
 */
@Component
public class RabbitMqReceiver {
    public static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqReceiver.class);

    //轮数
    public static int round = 0;

    @Autowired
    FlService flService;

    @Reference
    NodeSecurService nodeSecurService;

    @RabbitListener(queues = "fisco.server.download")
    public void receiveFromServer(Message message, Channel channel) {
        System.out.println("进入");
        String msg = new String(message.getBody());
        LOGGER.info("当前时间:{},收到Python服务端信息{}", new Date(), msg);

        int[][] intervals = null;

        try {
            flService.sendModel2Cli2(msg);
            //手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            flService.sendModel2Server(Constant.TrainList);
        } catch (PythonExecutionException | IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "fisco.client.upload")
    public void receiveFromClient(Message message,Channel channel) throws IOException {
        String msg = new String(message.getBody());
        LOGGER.info("当前时间:{},收到Python客户端端信息{}", new Date(), msg);
        //上链
        nodeSecurService.Upload_data(msg);
        channel.basicAck(1,true);
    }

    @RabbitListener(queues = "fl.online.collect.queue")
    public void receiveFromOnlineNode(Message message,Channel channel) {
        String msg = new String(message.getBody());
        LOGGER.info("当前时间:{},收到测试队列信息{}", new Date(), msg);
        //上链
        //flService.countOnlineNode(msg);
    }
}
