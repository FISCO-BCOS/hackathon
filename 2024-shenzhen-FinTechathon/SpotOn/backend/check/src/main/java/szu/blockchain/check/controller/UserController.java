package szu.blockchain.check.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import szu.blockchain.check.entity.ProofData;
import szu.blockchain.check.entity.ZkProofData;
import szu.blockchain.check.service.CheckService;



@RestController
public class UserController {

    @Autowired
    private CheckService checkService;

    @PostMapping("/check")
    public boolean Check(@RequestBody ProofData proofData) {
        boolean result1 = new CheckService().check(proofData);
        return result1;


    }

    @PostMapping("/zkcheck")
    public boolean ZkCheck(@RequestBody ZkProofData zkproofData) {
        System.out.println("zkproofData");
        boolean result2 = new CheckService().zkcheck(zkproofData);
        System.out.println(result2);
        return result2;


    }
    @GetMapping("/test")
    public String test(){
        return "hello";
    }

}