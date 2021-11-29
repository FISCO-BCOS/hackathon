package com.media.controller;

import com.media.pojo.CommonResult;
import com.media.service.SmsSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YZR
 * @date 2020/11/6 10:13
 */

@Controller
@RequestMapping
public class SmsController {

    @Autowired
    SmsSendService iSmsSend;

    @PostMapping("/getPhoneCode")
    @ResponseBody
    public CommonResult sendPhoneCode(@RequestParam(name = "phoneNumber") BigInteger phoneNumber) {
        int code = (int) (Math.random() * 8999 + 1000);
        boolean isSend = iSmsSend.sendCode(phoneNumber, "SMS_205435390", code);
        if (isSend) {
            return new CommonResult(true, "发送成功", code);
        } else {
            return new CommonResult(false, "发送成功", null);
        }
    }
}
