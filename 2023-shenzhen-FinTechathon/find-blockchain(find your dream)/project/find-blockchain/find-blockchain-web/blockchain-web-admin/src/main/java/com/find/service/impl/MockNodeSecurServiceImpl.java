package com.find.service.impl;

import com.find.NodeSecurService;
import com.find.service.FlService;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Author Su
 * @Date 2023/7/11
 * @Description 可信节点安全认证查询服务降级兜底类
 */
@Slf4j
public class MockNodeSecurServiceImpl implements NodeSecurService {

    @Override
    public void createNodeAccount(String nodeId) {
        log.error("可信节点安全认证查询接口调用失败");
    }

    @Override
    public List<String> validation(List<String> nodeList) {
        log.error("可信节点安全认证查询接口调用失败");
        return null;
    }

    @Override
    public List<String> Upload_data(String data) {
        log.error("数据上传到区块链失败");
        return null;
    }

    @Override
    public List<String> Transaction(List<String> nodeLIst) {
        log.error("可信节点安全认证查询接口调用失败");
        return null;
    }

    @Override
    public void queryNodeAccount() {
        log.error("可信节点安全认证查询接口调用失败");
    }
}