package com.find.controller;

import com.find.pojo.TrainInfo;
import com.find.service.impl.TrainDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/6
 */

@RequestMapping("/train")
@Controller
public class TrainController {

    @Autowired
    TrainDataServiceImpl trainDataService;

    @PostMapping("/usertaskdata")
    public String usertaskdata(){
        trainDataService.selectUserTaskInfo("user1");
        return "redirect:/usertaskdata";
    }

    @GetMapping("/usertraindataByone")
    public String usertraindataByone(@RequestParam String username,@RequestParam String timestamp,@RequestParam String times,@RequestParam int work_num){
        trainDataService.getTrainDataByUser(username,timestamp,Integer.parseInt(times)*(work_num+1));
        System.out.println(times + "____" +  work_num + "___" + timestamp);
        return "redirect:/trainDataByOne";
    }
}
