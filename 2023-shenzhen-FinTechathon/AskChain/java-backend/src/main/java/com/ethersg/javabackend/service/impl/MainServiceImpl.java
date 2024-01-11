package com.ethersg.javabackend.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ethersg.javabackend.pojo.*;
import com.ethersg.javabackend.service.MainService;
import com.ethersg.javabackend.util.ESGRatingSystem;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {

    @Value("${bcos.key-store-path}")
    private String keyStorePath;
    @Value("${bcos.config-file}")
    private String configFile;
    @Value("${bcos.contract-address}")
    private String contractAddress;

    private CryptoSuite cryptoSuite;
    private ESGRatingSystem esgRatingSystem;

    @Override
    public void register(RegisterInfo registerInfo) {
        // 创建非国密类型的CryptoSuite
        cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 随机生成非国密公私钥对
        cryptoSuite.createKeyPair().storeKeyPairWithP12(keyStorePath, registerInfo.getPassword());
        // 初始化BcosSDK
        BcosSDK sdk =  BcosSDK.build(configFile);
        // 为群组1初始化client
        Client client = sdk.getClient(1);
        // 加载账户
        cryptoSuite.loadAccount("p12", keyStorePath, registerInfo.getPassword());
        System.out.println("current address: " + cryptoSuite.getCryptoKeyPair().getAddress());
        esgRatingSystem = ESGRatingSystem.load(contractAddress, client, cryptoSuite.getCryptoKeyPair());
        // 在链上注册信息
        esgRatingSystem.registerUser(registerInfo.getName(), registerInfo.getAccountType(), registerInfo.getOrganizationCode(), registerInfo.getLocation(), registerInfo.getField());
    }

    @Override
    public UserInfo login(String password) {    // 通过client获取CryptoSuite对象
        // 初始化BcosSDK
        BcosSDK sdk =  BcosSDK.build(configFile);
        // 为群组1初始化client
        Client client = sdk.getClient(1);
        cryptoSuite = client.getCryptoSuite();
        // 加载pem账户文件
        cryptoSuite.loadAccount("p12", keyStorePath, password);
        System.out.println("current address: " + cryptoSuite.getCryptoKeyPair().getAddress());
        esgRatingSystem = ESGRatingSystem.load(contractAddress, client, cryptoSuite.getCryptoKeyPair());

        // 请求智能合约，并返回用户信息
        try {
            Tuple2<String, String> userInfo = esgRatingSystem.getUserInfo(cryptoSuite.getCryptoKeyPair().getAddress());
            return UserInfo.builder().username(userInfo.getValue1()).identity(userInfo.getValue2()).build();
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransactionReceipt upload(ESGProject esgProject) {
        String fileUrl = JSON.toJSONString(esgProject.getFileUrl());
        byte[] property = {esgProject.getProperty().getBytes()[0]};
        return esgRatingSystem.uploadProject(esgProject.getEsgName(), esgProject.getDescription(), property, fileUrl);
    }

    @Override
    public TransactionReceipt update(ESGProjectWithID esgProjectWithID) {
        String fileUrl = JSON.toJSONString(esgProjectWithID.getFileUrl());
        return esgRatingSystem.modifyProject(esgProjectWithID.getId(), esgProjectWithID.getDescription(), fileUrl);
    }

    @Override
    public List<ESGProjectListByCompany> companyLookup() {
        List<ESGProjectListByCompany> queryList = new ArrayList<>();
        String currentAddress = cryptoSuite.getCryptoKeyPair().getAddress();
        try {
            for (int i = 0; i < esgRatingSystem.getProjectsAmount(currentAddress).intValue(); i++) {
                Tuple7<BigInteger, String, String, byte[], String, String, BigInteger> project = esgRatingSystem.viewProject(currentAddress, BigInteger.valueOf(i));
                String property = new String(project.getValue4()) + '\0';
                List<String> fileUrl = JSON.parseArray(project.getValue6(), String.class);
                queryList.add(ESGProjectListByCompany.builder()
                                .id(project.getValue1())
                                .esgName(project.getValue2())
                                .esgDescription(project.getValue3())
                                .property(property)
                                .fileUrl(fileUrl)
                                .score(project.getValue7())
                        .build());
            }
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
        return queryList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ESGProjectByOrganization> organizationLookup() {
        List<ESGProjectByOrganization> queryList = new ArrayList<>();
        try {
            List<String> companys = esgRatingSystem.getCompanys();
            for (String company : companys) {
                int projectAmount = esgRatingSystem.getProjectsAmount(company).intValue();
                for (int i = 0; i < projectAmount; i++) {
                    Tuple7<BigInteger, String, String, byte[], String, String, BigInteger> project = esgRatingSystem.viewProject(company, BigInteger.valueOf(i));
                    String property = new String(project.getValue4()) + '\0';
                    List<String> fileUrl = JSON.parseArray(project.getValue6(), String.class);
                    queryList.add(ESGProjectByOrganization.builder()
                                    .id(project.getValue1())
                                    .companyName(esgRatingSystem.getUserInfo(company).getValue1())
                                    .property(property)
                                    .esgName(project.getValue2())
                                    .esgDescription(project.getValue3())
                                    .fileUrl(fileUrl)
                                    .score(project.getValue7())
                                    .companyAddress(project.getValue5())
                            .build());
                }
            }
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
        return queryList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ESGScoreList> organizationGetScores() {
        List<ESGScoreList> queryList = new ArrayList<>();
        try {
            List<String> companys = esgRatingSystem.getCompanys();
            List<String> agencies = esgRatingSystem.getAssessmentAgencies();
            for (String company : companys) {
                int projectAmount = esgRatingSystem.getProjectsAmount(company).intValue();
                for (int i = 0; i < projectAmount; i++) {
                    Tuple7<BigInteger, String, String, byte[], String, String, BigInteger> project = esgRatingSystem.viewProject(company, BigInteger.valueOf(i));
                    String property = new String(project.getValue4()) + '\0';
                    List<String> fileUrl = JSON.parseArray(project.getValue6(), String.class);
                    ESGScoreList esgScoreList = ESGScoreList.builder()
                            .id(project.getValue1())
                            .companyName(esgRatingSystem.getUserInfo(company).getValue1())
                            .property(property)
                            .esgName(project.getValue2())
                            .esgDescription(project.getValue3())
                            .fileUrl(fileUrl)
                            .companyAddress(project.getValue5())
                            .build();
                    for (String agency : agencies) {
                        int projectScore = esgRatingSystem.getProjectScore(company, BigInteger.valueOf(i), agency).intValue();
                        String standard = esgRatingSystem.getProjectStandard(company, BigInteger.valueOf(i), agency);
                        if (projectScore != 0) {
                            ESGScoreList item = new ESGScoreList(esgScoreList);
                            item.setOrganization(esgRatingSystem.getUserInfo(agency).getValue1());
                            item.setScore(BigInteger.valueOf(projectScore));
                            item.setStandard(standard);
                            queryList.add(item);
                        }
                    }
                }
            }
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
        return queryList;
    }

    @Override
    public TransactionReceipt putScore(ESGScore score) {
        return esgRatingSystem.scoreESG(score.getCompanyAddress(), score.getProjectId(), score.getScore(), score.getStandard());
    }

    @Override
    public BigInteger getScore(String companyName) {
        try {
            return esgRatingSystem.getCompanyScore(companyName);
        } catch (ContractException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verify(String result) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject credential = JSONObject.parseObject(result);
        JSONObject response = restTemplate.postForObject("http://localhost:6101/step1/verifyCredential", MapUtil.of("credential", credential.getJSONObject("result")), JSONObject.class);
        Assert.notNull(response, "Request for verify CPT failed");
        if (response.getBooleanValue("result")) {
            String address = credential.getJSONObject("result").getJSONObject("claim").getString("address");
            BigInteger level = credential.getJSONObject("result").getJSONObject("claim").getBigInteger("level");
            esgRatingSystem.registerAssessmentAgency(address, level);
        } else {
            throw new RuntimeException("Credential illegal");
        }
    }
}
