package com.webank.webase.yidian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import de.scravy.pair.Pair;
import de.scravy.pair.Pairs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;


@Slf4j
@Data
@Service("realService")
public class TransactionService {

    private RestTemplate rest;
    private String url = "http://121.37.193.91:5002/WeBASE-Front/trans/handleWithSign";

    private String signUserId = "27225741f6e043e0a5d2745025840041";
    private int groupId = 1;
    private String contractName = "HelloWorld";
    private String contractAddress = "0x33754540573715fd5059f35a63b8687eb109cdfa";
    private String funcName = "get";
    private String funcParam = "[]";
    private String contractAbi = "[{\"constant\":false,\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";

    {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(100 * 1000);// ms
        factory.setConnectTimeout(100 * 1000);// ms
        this.rest = new RestTemplate(factory);
    }

    public Pair<Integer, String> sendTransaction() {

        try {
            TransactionParam transParam = new TransactionParam();
            transParam.setGroupId(groupId);
            transParam.setContractAddress(contractAddress);
            transParam.setSignUserId(signUserId);
            transParam.setContractName(contractName);
            transParam.setFuncName(funcName);
            transParam.setFuncParam(JSONArray.parseArray(funcParam));
            transParam.setContractAbi(JSONArray.parseArray(contractAbi));

            log.info("transaction param:{}", JSON.toJSONString(transParam));
            Object rsp = rest.postForObject(url, transParam, Object.class);
            String rspStr = "null";
            if (Objects.nonNull(rsp)) {
                rspStr = JSON.toJSONString(rsp);
            }
            log.info("transaction result:{}", rspStr);

            Pair<Integer, String> myPair = Pairs.from(0, rspStr);
            return myPair;
        } catch (Exception ex) {
            log.error("fail sendTransaction", ex);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String msg=sw.toString();
            System.out.println("Error exception:"+msg);

            Pair<Integer, String> myPair = Pairs.from(1, msg);
            return myPair;
        }

    }
}
