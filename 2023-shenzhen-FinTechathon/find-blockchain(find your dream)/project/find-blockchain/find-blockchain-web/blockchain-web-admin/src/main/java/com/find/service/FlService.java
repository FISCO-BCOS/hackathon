package com.find.service;

import com.find.dao.Result;
import com.find.pojo.OnlineNode;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import org.springframework.aop.target.LazyInitTargetSource;

import javax.management.ObjectName;
import java.io.IOException;
import java.util.List;

public interface FlService {

    void sendModel2Cli(String message) throws  IOException;

    void sendModel2Cli2(String message) throws PythonExecutionException, IOException;

    void queryOnlineNode();

    void startTraining(Integer setWorkerNum, Integer rounds,Double reward);

    void sendModel2Server(List<String> TrainList);

    List<String> querySecurity(List<String> nodeIdList);

    String querySecurityNode();

    String updateSecurityNode();

    void UpdateNode(List<String> nodeIdList);

    void offline(String userName);

    Result requestTask(String nodeId);
}

