package com.find.fiscoshard1.component;


import com.find.ContractService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class ShardCommandRunner implements CommandLineRunner {
//    @Autowired
//    ContractService contractService;





    @Override
    public void run(String... args) throws Exception {

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("初始化");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
