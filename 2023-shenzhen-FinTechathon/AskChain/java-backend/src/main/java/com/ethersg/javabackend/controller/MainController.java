package com.ethersg.javabackend.controller;

import com.ethersg.javabackend.enums.ResultCode;
import com.ethersg.javabackend.pojo.*;
import com.ethersg.javabackend.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@RestController
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    // -----------------通用------------------- //
    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<Object> register(@RequestBody RegisterInfo registerInfo) {
        try {
            mainService.register(registerInfo);
            return new Result<>(ResultCode.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    /**
     * 登录
     * @return 返回登录用户身份、用户名
     */
    @PostMapping("/login")
    public Result<UserInfo> login(@RequestBody String password) {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.login(password));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    // -----------------企业端------------------- //
    /**
     * ESG项目提交
     * @return 项目id
     */
    @PostMapping("/upload")
    public Result<TransactionReceipt> upload(@RequestBody ESGProject esgProject) {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.upload(esgProject));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    /**
     * 项目信息修改
     * @param esgProjectWithID 项目内容（包含id）
     */
    @PostMapping("/update")
    public Result<TransactionReceipt> update(@RequestBody ESGProjectWithID esgProjectWithID) {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.update(esgProjectWithID));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    /**
     * 公司查看ESG项目
     */
    @GetMapping("/lookup/company")
    public Result<List<ESGProjectListByCompany>> companyLookup() {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.companyLookup());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    // -----------------ESG评估机构端------------------- //

    /**
     * 评估机构查看ESG项目
     */
    @GetMapping("/lookup/organization")
    public Result<List<ESGProjectByOrganization>> organizationLookup() {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.organizationLookup());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    /**
     * 评估机构查看ESG评分
     */
    @GetMapping("/scores")
    public Result<List<ESGScoreList>> scores() {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.organizationGetScores());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    /**
     * 打分
     */
    @PutMapping("/score")
    public Result<TransactionReceipt> putScore(@RequestBody ESGScore esgScore) {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.putScore(esgScore));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public Result<Object> verify(@RequestBody String credential) {
        try {
            mainService.verify(credential);
            return new Result<>(ResultCode.SUCCESS, null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }

    // -----------------政府端------------------- //

    /**
     * 获取评分
     * @param companyName 公司名称
     */
    @GetMapping("/score/{companyName}")
    public Result<BigInteger> getScore(@PathVariable("companyName") String companyName) {
        try {
            return new Result<>(ResultCode.SUCCESS, mainService.getScore(companyName));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result<>(e.getMessage());
        }
    }
}
