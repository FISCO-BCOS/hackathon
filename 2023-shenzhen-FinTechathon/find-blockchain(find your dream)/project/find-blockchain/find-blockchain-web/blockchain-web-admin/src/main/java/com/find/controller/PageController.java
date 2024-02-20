package com.find.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

//    @RequestMapping("/index")
//    public String index() {
//        return "index";
//    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/taskrelease")
    public String taskrelease() {
        return "taskrelease";
    }

    @RequestMapping("/systemmonitor")
    public String systemmonitor() {
        return "systemmonitor";
    }

    @RequestMapping("/transactiontest")
    public String transactiontest() {
        return "transactiontest";
    }

    @RequestMapping("/gradienttest")
    public String gradienttest() {
        return "gradienttest";
    }

    @RequestMapping("/clientmanage")
    public String clientmanage() {
        return "clientmanage";
    }

    @RequestMapping("/servermanage")
    public String servermanage() {
        return "servermanage";
    }

    @RequestMapping("/nodemanager")
    public String nodemanager() {
        return "nodemanager";
    }

    @RequestMapping("/usertaskdata")
    public String usertaskdata() {
        return "usertaskdata";
    }

    @RequestMapping("/trainDataByOne")
    public String trainDataByOne() {
        return "trainDataByOne";
    }


}
