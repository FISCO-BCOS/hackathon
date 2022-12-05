package com.itheima.controller;


import com.itheima.controller.utils.R;
import com.itheima.domain.CreditAgent;
import com.itheima.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @PostMapping
    public R sendCredit(@RequestBody CreditAgent creditAgent) throws Exception {
        boolean flag = creditService.sendCredit(creditAgent);
        return new R(flag);
    }

    @GetMapping
    public R getBalance() throws Exception{
        try {
            Integer balance = creditService.getBalance();
            return new R(true,balance);
        }catch (Exception e){
            return new R(false);
        }

    }












}
