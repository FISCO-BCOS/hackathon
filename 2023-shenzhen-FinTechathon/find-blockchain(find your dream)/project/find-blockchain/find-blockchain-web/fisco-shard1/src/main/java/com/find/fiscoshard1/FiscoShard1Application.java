package com.find.fiscoshard1;


import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@EnableDubbo  //会扫描所有的包，从中找出dubbo的@Service标注的类
@DubboComponentScan
@SpringBootApplication
public class FiscoShard1Application {



    public static void main(String[] args) {

        SpringApplication.run(FiscoShard1Application.class, args);


    }

}
