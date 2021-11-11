package org.example.demo.controller;

import org.example.demo.model.bo.*;
import org.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("Individual")
public class IndividualController {

    @Autowired
    private IndividualService individual;
    @Autowired
    private ProgramService program;
    @Autowired
    private IntegralService integral;

    @GetMapping("addprogram")
    public void addprogram(
            @RequestParam("totalneed")  BigInteger totalneed,
            @RequestParam("title") String title,
            @RequestParam("programType")  String programType,
            @RequestParam("description")  String description
    ) throws Exception{
        if(title==""){
            System.out.println("Empty title!");
            return;
        } else if(description == "") {
            System.out.println(("Empty description!"));
            return;
        }
        IndividualAddprogramInputBO individualAddprogramInputBO = new IndividualAddprogramInputBO(
                totalneed,
                title,
                programType,
                description);
        individual.addprogram(individualAddprogramInputBO);
    }


    @GetMapping("changevaule")
    public void changevalue(
            @RequestParam("id")  BigInteger id,
            @RequestParam("totalneed") BigInteger totalnned
    ) throws Exception{
        IndividualChangevalueInputBO individualChangevalueInputBO = new IndividualChangevalueInputBO(id, totalnned);
        individual.changevalue(individualChangevalueInputBO);
    }

    @GetMapping("dealPoints")
    public boolean dealPoints(
            @RequestParam("address")  String addr,
            @RequestParam("value") BigInteger value
    ) throws Exception {
        if (addr == "") {
            System.out.println("Empty address!");
            return false;
        } else {
            IndividualDealPointsInputBO individualDealPointsInputBO = new IndividualDealPointsInputBO(addr, value);
            individual.dealPoints(individualDealPointsInputBO);
            return true;
        }
    }

    @GetMapping("devote")
    public String devote(
            @RequestParam("id")  BigInteger id,
            @RequestParam("value") BigInteger value,
            @RequestParam("programtype") String programType,
            @RequestParam("message") String message
    ) throws Exception{
        IndividualDevoteInputBO individualDevoteInputBO = new IndividualDevoteInputBO(id, value, programType, message);
        return individual.devote(individualDevoteInputBO).getValues();
    }

    @GetMapping("getinfor")
    public String getinfo(
            @RequestParam("id")  BigInteger id
    ) throws Exception{
        IndividualGetinforInputBO individualGetinforInputBO = new IndividualGetinforInputBO(id);
        return individual.getinfor(individualGetinforInputBO).getValues();
    }

    @GetMapping("getstring")
    public String getstring(
            @RequestParam("id")  BigInteger id
    ) throws Exception{
        IndividualGetstringinforInputBO individualGetstringinforInputBO = new IndividualGetstringinforInputBO(id);
        return individual.getstringinfor(individualGetstringinforInputBO).getValues();
    }

    @GetMapping("getTxinfor")
    public String getTxinfor(
            @RequestParam("id")  byte[] id
    ) throws Exception{
        IndividualGetTxInforInputBO individualGetTxInforInputBO = new IndividualGetTxInforInputBO(id);
        return individual.getTxInfor(individualGetTxInforInputBO).getValues();
    }

    @GetMapping("implement")
    public String implement(
            @RequestParam("id")  BigInteger id,
            @RequestParam("describe")  String describe
    ) throws Exception{
        IndividualImplementProgramInputBO individualImplementProgramInputBO = new IndividualImplementProgramInputBO(id, describe);
        return individual.implementProgram(individualImplementProgramInputBO).getValues();
    }
}

