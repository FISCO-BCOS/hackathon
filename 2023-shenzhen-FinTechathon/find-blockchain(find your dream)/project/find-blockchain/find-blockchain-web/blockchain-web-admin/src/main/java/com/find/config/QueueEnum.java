package com.find.config;

/**
 * @Description 消息队列枚举配置
 * @Author Hasim
 * @Date 2021/9/23 13:36
 * @Version 1.0
 */
public enum QueueEnum {
    // socket_web生产，web后端消费
    QUEUE_CLIENT_UPLOAD("fisco.client.upload.direct", "fisco.client.upload", "fisco.client.upload.web"),
    // web后端生产，Python服务端消费
    QUEUE_SERVER_UPLOAD("fisco.server.upload.direct", "fisco.server.upload", "fisco.server.upload.server"),
    // web后端生产，socket_web客户端消费
    QUEUE_CLIENT_DOWNLOAD("fisco.client.download.direct", "fisco.client.download", "fisco.client.download.client"),
    // Python服务端生产，web后端消费
    QUEUE_SERVER_DOWNLOAD("fisco.server.download.direct", "fisco.server.download", "fisco.server.download.web"),
    // 测试队列
    QUEUE_ONLINE_COLLECT("fl.online.collect.direct", "fl.online.collect.queue", "fl.online.collect.key"),

    QUEUE_START_TRAINING("fl.strat.training.direct", "fl.start.training.queue", "fl.start.training.key");


    private String exchangeName; // 交换机名称
    private String queueName; // 队列名称
    private String routingKey; // 路由键

    QueueEnum(String exchangeName, String queueName, String routingKey) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
