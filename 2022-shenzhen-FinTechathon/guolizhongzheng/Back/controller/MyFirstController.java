package com.zgxt.springbootdemo.controller;

import com.zgxt.springbootdemo.bean.UserBean;
import com.zgxt.springbootdemo.utils.CommonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Binge
 * @desc 处理用户请求的页面控制器类
 * @date 2022/9/19 11:10
 */
@RestController //表示该类是页面控制器类，可以处理用户的请求
public class MyFirstController {
    @RequestMapping("/hello") //用户请求的 URL 地址
    public String hello() { //处理用户请求的方法
        return "hello,springboot";
    }

    @RequestMapping("/users")
    public CommonResponse getUsers() {
        List<UserBean> users = new ArrayList<>();
        users.add(new UserBean("张三",23,"男","13541992345","zs@163.com"));
        users.add(new UserBean("李四",24,"男","13441992345","lisi@163.com"));
        users.add(new UserBean("王五",26,"女","13741992345","wangwu@163.com"));
        users.add(new UserBean("赵六",25,"男","13941992345","zl@163.com"));
        return CommonResponse.ok(users);
    }

    @RequestMapping("/register")
    public CommonResponse register(@RequestBody UserBean user) {
        System.out.println(user);
        return CommonResponse.ok("ok");
    }

    @RequestMapping("/userinfo")
    public CommonResponse getUserInfo(@RequestBody UserBean user) {
        System.out.println(user);
        return CommonResponse.ok("ok");
    }
}
