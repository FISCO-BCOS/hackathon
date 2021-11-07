package com.media.controller;

import com.media.pojo.CommonResult;
import com.media.pojo.TradeHistory;
import com.media.service.TradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tradeHistory")
public class TradeHistoryController {

    @Autowired
    public TradeHistoryService iTradeHistoryService;

    @RequestMapping("/addOne")
    @ResponseBody
    public CommonResult addOne(@RequestBody TradeHistory tradeHistory) {
        iTradeHistoryService.save(tradeHistory);
        return new CommonResult(true, "成功");
    }
}
