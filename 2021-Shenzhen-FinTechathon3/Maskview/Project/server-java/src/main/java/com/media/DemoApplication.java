package com.media;

import com.media.bcos.client.PointControllerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static PointControllerClient pointControllerClient = new PointControllerClient();

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        try {
            pointControllerClient.initialize();
            System.out.println("区块链SDK初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("区块链SDK初始化失败");
        }
        System.out.println("java-service启动成功！！！");
    }

}
