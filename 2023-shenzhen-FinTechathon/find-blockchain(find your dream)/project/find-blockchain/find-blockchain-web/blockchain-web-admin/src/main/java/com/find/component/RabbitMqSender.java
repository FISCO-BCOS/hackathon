package com.find.component;

//import com.find.config.QueueEnum;
import com.find.config.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description 消息发送类
 * @Author Hasim
 * @Date 2021/9/23 14:20
 * @Version 1.0
 */
@Component
public class RabbitMqSender {
    public static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqSender.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    private String queryOnlineNodeExchange = "queryOnlineNodeExchange";

    public void sendUploadMsg(String message) {
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_SERVER_UPLOAD.getExchangeName(), QueueEnum.QUEUE_SERVER_UPLOAD.getRoutingKey(), message);
        LOGGER.info("当前时间:{}, 发送消息:{}到Python服务端", new Date(), message);
    }

    public void sendDownloadMsg(String message) {
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_CLIENT_DOWNLOAD.getExchangeName(), QueueEnum.QUEUE_CLIENT_DOWNLOAD.getRoutingKey(), message);
        LOGGER.info("当前时间:{}, 发送消息:{}到Python客户端", new Date(), message);
    }

    public void sendUploadClientMsg(String message) {
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_CLIENT_UPLOAD.getExchangeName(), QueueEnum.QUEUE_CLIENT_UPLOAD.getRoutingKey(), message);
        LOGGER.info("当前时间:{}, 发送消息:{}到Python客户端", new Date(), message);
    }

    public void queryOnline(){
        Message message = new Message("收到节点在线检测请求".getBytes(),new MessageProperties());
        rabbitTemplate.send(queryOnlineNodeExchange,"",message);
    }

    public void sendStartTraining(Integer rounds) {
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_START_TRAINING.getExchangeName(), QueueEnum.QUEUE_START_TRAINING.getRoutingKey(), String.valueOf(rounds));
        LOGGER.info("当前时间:{}, 发送消息:{}到Python服务端", new Date(),rounds);
    }
}
