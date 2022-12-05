package com.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@CrossOrigin

@SpringBootApplication
public class SpringbootdemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootdemoApplication.class, args);
    }

}
