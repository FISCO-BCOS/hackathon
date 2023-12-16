package com.find;

import com.find.component.WebSocketServer;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URL;

@EnableDubbo  //会扫描所有的包，从中找出dubbo的@Service标注的类
@DubboComponentScan
@SpringBootApplication
@EnableScheduling
public class BlockchainWebAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainWebAdminApplication.class, args);
//        WebSocketServer webSocketServer = new WebSocketServer();
    }

}
