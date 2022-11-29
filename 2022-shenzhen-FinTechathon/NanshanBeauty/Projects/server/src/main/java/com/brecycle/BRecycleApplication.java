package com.brecycle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动入口
 *
 * @author cmgun
 */
@EnableScheduling
@MapperScan("com.brecycle.mapper")
@SpringBootApplication
public class BRecycleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BRecycleApplication.class);
    }
}
