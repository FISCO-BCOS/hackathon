package com.find.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 消息队列配置类
 * @Author Su
 * @Date 2021/9/23 13:47
 * @Version 1.0
 */
@Configuration
public class RabbitMqConfig {
    /**
     * Python客户端上传队列交换机(direct)
     */
    @Bean
    DirectExchange clientUploadExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_CLIENT_UPLOAD.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * Python客户端下载队列交换机(direct)
     */
    @Bean
    DirectExchange clientDownloadExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_CLIENT_DOWNLOAD.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * Python服务端上传队列交换机(direct)
     */
    @Bean
    DirectExchange serverUploadExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_SERVER_UPLOAD.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * Python服务端下载队列交换机(direct)
     */
    @Bean
    DirectExchange serverDownloadExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_SERVER_DOWNLOAD.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * 查询FL客户端是否在线交换机(direct)
     */
    @Bean
    DirectExchange flOnlineCollectExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_ONLINE_COLLECT.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * 启动训练交换机(direct)
     */
    @Bean
    DirectExchange flStartTrainingExchange() {
        return ExchangeBuilder.directExchange(QueueEnum.QUEUE_START_TRAINING.getExchangeName())
                .durable(true)
                .build();
    }

    /**
     * Python客户端上传队列
     */
    @Bean
    Queue clientUploadQueue() {
        return new Queue(QueueEnum.QUEUE_CLIENT_UPLOAD.getQueueName());
    }



    /**
     * Python服务端上传队列
     */
    @Bean
    Queue serverUploadQueue() {
        return new Queue(QueueEnum.QUEUE_SERVER_UPLOAD.getQueueName());
    }

    /**
     * Python客户端下载队列
     */
    @Bean
    Queue clientDownloadQueue() {
        return new Queue(QueueEnum.QUEUE_CLIENT_DOWNLOAD.getQueueName());
    }

    /**
     * Python服务端下载队列
     */
    @Bean
    Queue serverDownloadQueue() {
        return new Queue(QueueEnum.QUEUE_SERVER_DOWNLOAD.getQueueName());
    }

    /**
     * 查询fl节点是否在线测试队列
     */
    @Bean
    Queue flOnlineCollectQueue() {
        return new Queue(QueueEnum.QUEUE_ONLINE_COLLECT.getQueueName());
    }

    /**
     * 查询fl节点是否在线测试队列
     */
    @Bean
    Queue flStartTrainingQueue() {
        return new Queue(QueueEnum.QUEUE_START_TRAINING.getQueueName());
    }

    /**
     * 绑定Python客户端上传队列与交换机
     */
    @Bean
    Binding clientUploadBinding(DirectExchange clientUploadExchange, Queue clientUploadQueue){
        return BindingBuilder
                .bind(clientUploadQueue)
                .to(clientUploadExchange)
                .with(QueueEnum.QUEUE_CLIENT_UPLOAD.getRoutingKey());
    }


    /**
     * 绑定Python服务端上传队列与交换机
     */
    @Bean
    Binding serverUploadBinding(DirectExchange serverUploadExchange, Queue serverUploadQueue){
        return BindingBuilder
                .bind(serverUploadQueue)
                .to(serverUploadExchange)
                .with(QueueEnum.QUEUE_SERVER_UPLOAD.getRoutingKey());
    }

    /**
     * 绑定Python客户端下载队列与交换机
     */
    @Bean
    Binding clientDownloadBinding(DirectExchange clientDownloadExchange, Queue clientDownloadQueue){
        return BindingBuilder
                .bind(clientDownloadQueue)
                .to(clientDownloadExchange)
                .with(QueueEnum.QUEUE_CLIENT_DOWNLOAD.getRoutingKey());
    }

    /**
     * 绑定Python服务端下载队列与交换机
     */
    @Bean
    Binding serverDownloadBinding(DirectExchange serverDownloadExchange, Queue serverDownloadQueue){
        return BindingBuilder
                .bind(serverDownloadQueue)
                .to(serverDownloadExchange)
                .with(QueueEnum.QUEUE_SERVER_DOWNLOAD.getRoutingKey());
    }

    /**
     * 绑定查询fl节点是否在线队列与交换机
     */
    @Bean
    Binding tpsTestBinding(DirectExchange flOnlineCollectExchange, Queue flOnlineCollectQueue){
        return BindingBuilder
                .bind(flOnlineCollectQueue)
                .to(flOnlineCollectExchange)
                .with(QueueEnum.QUEUE_ONLINE_COLLECT.getRoutingKey());
    }

    @Bean
    Binding flStartTrainingBinding(DirectExchange flStartTrainingExchange, Queue flStartTrainingQueue){
        return BindingBuilder
                .bind(flStartTrainingQueue)
                .to(flStartTrainingExchange)
                .with(QueueEnum.QUEUE_START_TRAINING.getRoutingKey());
    }
}
