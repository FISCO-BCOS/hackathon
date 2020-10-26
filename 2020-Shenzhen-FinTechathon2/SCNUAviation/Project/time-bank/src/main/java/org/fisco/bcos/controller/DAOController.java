package org.fisco.bcos.controller;

import org.fisco.bcos.service.CredentialsService;
import org.fisco.bcos.service.DAOService;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@Controller
public class DAOController {

    @Autowired private DAOService daoService;
    @Autowired private CredentialsService credentialsService;
    private String tokenTime="0xce7dd83792391857c510926572a76d5d5576b586";
    private int flag=0;


    @RequestMapping("/DAO_detail")
    public String publish_dao(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("fundingGoalInToken") int fundingGoalInToken,
            @RequestParam("durationInDays") String durationInDays,
            Model model) throws Exception {
        if (title != "") {
            System.out.println("DAOList!");
            BigInteger fundingGIT = BigInteger.valueOf(fundingGoalInToken);
            BigInteger durationID = BigInteger.valueOf(Integer.parseInt(durationInDays));
            //BigInteger durationID = BigInteger.valueOf(121);
            Credentials credentials = credentialsService.getCredentials(
                    "0x98333491efac02f8ce109b0c499074d47e7779a6.p12",
                    "123456");
            String daoContractAddress = daoService.deploy(
                    credentials,
                    title,
                    description,
                    fundingGIT,
                    durationID,
                    tokenTime);
            model.addAttribute("title", title);
            model.addAttribute("hash", daoContractAddress);
            model.addAttribute("type", type);
            model.addAttribute("description", description);
            model.addAttribute("fundingGoalInToken", fundingGoalInToken);
            model.addAttribute("durationInDays", durationInDays);
        }

        return "DAO_detail";
    }







}
