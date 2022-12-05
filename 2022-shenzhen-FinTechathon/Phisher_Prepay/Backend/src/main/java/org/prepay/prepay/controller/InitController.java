package org.prepay.prepay.controller;

import io.netty.handler.codec.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class InitController {

    @RequestMapping("/init")
    @ResponseBody
    public String init(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return "hello world";
    }
}
