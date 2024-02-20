package com.find.fiscoshard2;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@EnableDubbo  //会扫描所有的包，从中找出dubbo的@Service标注的类
@DubboComponentScan
@SpringBootApplication
@MapperScan("com.find.fiscoshard2.dao")
public class FiscoShard2Application {

    public static void main(String[] args) {
        SpringApplication.run(FiscoShard2Application.class, args);

    }
}
