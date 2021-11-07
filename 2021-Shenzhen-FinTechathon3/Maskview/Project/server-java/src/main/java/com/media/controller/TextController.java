package com.media.controller;

import com.media.pojo.CommonResult;
import com.media.pojo.User;
import com.media.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Android-Retrofit注解练习-服务器接口
 */

@Controller
@RequestMapping("/welcome")
public class TextController {

    @Autowired
    UserService userService;

    @GetMapping("/get")
    @ResponseBody
    public CommonResult text() {
        User user = userService.getUserInfoByUid(BigInteger.valueOf(1));
        return new CommonResult(true, "查询成功", user);
    }

    @GetMapping("/getWithParams")
    @ResponseBody
    public CommonResult text1(@RequestParam(value = "uid") Integer uid) {
        User user = userService.getUserInfoByUid(BigInteger.valueOf(uid));
        return new CommonResult(true, "查询成功", user);
    }

    @PostMapping("/post")
    @ResponseBody
    public CommonResult text2() {
        User user = userService.getUserInfoByUid(BigInteger.valueOf(1));
        return new CommonResult(true, "查询成功", user);
    }

    @PostMapping("/postWithParams")
    @ResponseBody
    public CommonResult text3(@RequestParam(value = "uid") Integer uid) {
        User user = userService.getUserInfoByUid(BigInteger.valueOf(uid));
        return new CommonResult(true, "查询成功", user);
    }

}
