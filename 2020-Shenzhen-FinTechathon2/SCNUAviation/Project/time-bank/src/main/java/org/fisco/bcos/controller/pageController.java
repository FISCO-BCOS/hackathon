package org.fisco.bcos.controller;


import org.fisco.bcos.model.P2PEntry;
import org.fisco.bcos.model.P2PInfo;
import org.fisco.bcos.service.*;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

@Controller
public class pageController {

    @Autowired
    TokenTimeService tokenTimeService;
    @Autowired
    CredentialsService credentialsService;
    @Autowired
    DAOService daoService;
    @Autowired
    P2PTableService p2PTableService;
    @Autowired
    P2PTimeService p2PTimeService;
    @Autowired
    CommonService commonService;
    //TokenTime合约地址：0xce7dd83792391857c510926572a76d5d5576b586
    private String tokenTime="0xce7dd83792391857c510926572a76d5d5576b586";
    private String p2pContractAddress="";
    private String tableName="p2pContractTable";
    private String p2pTable="0xc2ac92298244511f985fd98987d07c14450a78b0";
    private String user1Address="0x98333491efac02f8ce109b0c499074d47e7779a6";
    private String[] userAddresses={user1Address};
    private String DAOaddress="0x56c94d20bfb743a75c580966c6c91d54796085e0";
    private String DAOtransHash="";


    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/")
    public String service_list1 (){
        return "service_list";
    }
    @GetMapping("/admin")
    public String login(){
        return "login";
    }

    @RequestMapping("/service_list.html")
    public String server_list(Model model) throws Exception {
        Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
        //tokenTimeService.deploy(credentials);
        //String hash=p2PTableService.deploy(credentials);
        //System.out.println("addr:"+hash);
        //String hash=p2PTableService.deploy(credentials);
        //tokenTimeService.load(credentials,tokenTime);
        //tokenTimeService.initialBalance(userAddresses);
        p2PTableService.load(credentials,p2pTable);
        //p2PTableService.create(tableName);
        ArrayList<P2PEntry> p2PEntries= p2PTableService.select("", "");
        String serviceList=commonService.GoodIn2Json(p2PEntries);
        System.out.println(serviceList);
        model.addAttribute("service_list",serviceList);
        return "service_list";
    }

    @GetMapping("/donate.html")
    public String donate()  {
        return "donate.html";
    }

    @GetMapping("/user.html")
    public String usr(){
        return "user.html";
    }

    @GetMapping("/publish_DAO")
    public String dao(){
        return "publish_DAO";
    }

    @GetMapping("/new-service.html")
    public ModelAndView publish() throws Exception {
        ModelAndView mv = new ModelAndView();
        Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
        //tokenTimeService.deploy(credentials);
        tokenTimeService.load(credentials,tokenTime);
        mv.setViewName("new-service");
        mv.addObject("balance", tokenTimeService.getBalance(user1Address));
        mv.addObject("balance2", (tokenTimeService.getBalance(user1Address)-9900));
        return mv;
    }

    @GetMapping("/service.html")
    public String service(){
        return "service.html";
    }

    @RequestMapping("/crowdfunding.html")
    public String crowdfunding(Model model) throws Exception {
        Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
        daoService.load(credentials,DAOaddress);
        System.out.println("DAOValue="+daoService.get_dao_info().getDescription());
        //tokenTimeService.load(credentials,tokenTime);
       // tokenTimeService.approve(DAOaddress,BigInteger.valueOf(300));
        daoService.pay(BigInteger.valueOf(10));
        int value=daoService.get_amountRaised();
        model.addAttribute("value",value);
        System.out.println("value="+value);
        return "crowdfunding.html";
    }

    @GetMapping("/buy.html")
    public String buy(){
        return "buy.html";
    }

    @RequestMapping("/service_detail")
    public String service_detail(@RequestParam("address") String address,Model model) throws Exception {
        Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
        p2PTimeService.load(credentials,address);
        p2PTableService.load(credentials,p2pTable);
        P2PInfo p2PInfo=p2PTimeService.getP2PAllInfo();
        String Hash=p2PTimeService.apply();
        System.out.println(Hash);
        model.addAttribute("service_detail");
        model.addAttribute("hash", Hash);
        model.addAttribute("name",p2PInfo.getOwner());
        model.addAttribute("title",p2PInfo.getTitle());
        model.addAttribute("desc",p2PInfo.getDescription());
        model.addAttribute("local","华师社区卫生服务中心");
        model.addAttribute("conAddr",address);
        model.addAttribute("phone","15536474562");
        model.addAttribute("type",p2PInfo.getSort());
        //model.addAttribute("price",price);
       // model.addAttribute("time",time);
        //model.addAttribute("desc",description);
        return "service_detail";
    }

    @RequestMapping("/publish_detail")
    public String publish_detail(
             @RequestParam("ownerName") String ownnerName,
             @RequestParam("title") String title,
             @RequestParam("price") String price,
             @RequestParam("type") String type,
             @RequestParam("description") String description,
             Model model) throws Exception {
        if(ownnerName!=""){
            BigInteger pri = BigInteger.valueOf(Integer.parseInt(price));
            BigInteger state = BigInteger.valueOf(0);
            Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
            p2pContractAddress=p2PTimeService.deploy(credentials,tokenTime,title,pri,type,description);
            tokenTimeService.load(credentials,tokenTime);
            tokenTimeService.approve(p2pContractAddress,pri);//授权
            p2PTimeService.publish();
            p2PTableService.load(credentials,p2pTable);
            p2PTableService.insert(p2pContractAddress,user1Address,title,pri,description,state);
            model.addAttribute("name",ownnerName);
            model.addAttribute("hash",p2pContractAddress);
            model.addAttribute("type",type);
            model.addAttribute("price",price);
            model.addAttribute("title",title);
            model.addAttribute("desc",description);
        }
        return "publish_detail";
    }

    @RequestMapping("/dao_list")
    public  String dao_list(Model model) throws Exception {
        model.addAttribute("hash",DAOtransHash);
        return "dao_list";
    }


}
