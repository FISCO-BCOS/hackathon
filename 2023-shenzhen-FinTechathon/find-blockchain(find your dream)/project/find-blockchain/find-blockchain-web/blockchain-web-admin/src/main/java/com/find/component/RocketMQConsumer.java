package com.find.component;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.find.NodeSecurService;
import com.find.dao.AccountDao;
import com.find.pojo.SharedNode;
import com.find.util.Constant;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class RocketMQConsumer {

    @Reference
    NodeSecurService nodeSecurService;

    @Autowired
    AccountDao accountDao;

    @RocketMQMessageListener(topic = "client2Web", consumerGroup = "groupA",selectorExpression = "FL")
    @Service
    public class TopicAListener implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

        @Override
        public void onMessage(String message) {
            // 处理 topicA 的消息
//            Date date = new Date();
//            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
//            System.out.println(message + ":" + dateFormat.format(date));
            //上链
            nodeSecurService.Upload_data(message);
            //解析终端上传的数据到实体类
            SharedNode sharedNode = JSONObject.parseObject(message, SharedNode.class);
            //保存到数据库
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
            accountDao.insertTrainData(sharedNode.getNodeId(),sharedNode.getTimes(),"user1",dateFormat.format(date), Constant.REWARD, sharedNode.getAccuracy());
        }

        @Override
        public void prepareStart(DefaultMQPushConsumer consumer) {
            // 每次拉取的间隔，单位为毫秒
            consumer.setPullInterval(20);
            // 设置每次从队列中拉取的消息数为32
            consumer.setPullBatchSize(32);
        }
    }
}
