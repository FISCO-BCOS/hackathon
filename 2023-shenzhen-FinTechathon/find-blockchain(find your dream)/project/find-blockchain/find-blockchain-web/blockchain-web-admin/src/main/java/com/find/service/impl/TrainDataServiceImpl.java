package com.find.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.find.component.WebSocketServer;
import com.find.dao.AccountDao;
import com.find.pojo.TrainInfo;
import com.find.pojo.TrainInfoByOneContext;
import com.find.pojo.TrainInfoContext;
import com.find.service.TrainDataService;
import com.find.service.WebSocketService;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/6
 */

@Service
public class TrainDataServiceImpl implements TrainDataService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    TrainInfoContext trainInfoContext;

    @Autowired
    TrainInfoByOneContext trainInfoByOneContext;

    @Autowired
    WebSocketService webSocketService;

    @Override
    public List<TrainInfo> getTrainDataByUser(String username, String timestamp, int dataNums) {
        trainInfoByOneContext.removeAllNode();
        List<TrainInfo> trainInfoList = accountDao.selectTrainInfoByUser(username, timestamp, dataNums);
        for (TrainInfo trainInfo : trainInfoList) {
            trainInfoByOneContext.putNode(trainInfo.getTimestamp()+trainInfo.getNodeId(),trainInfo);
        }
        return trainInfoList;
    }

    @Override
    public List<TrainInfo> selectUserTaskInfo(String username) {
        //初始化
        trainInfoContext.removeAllNode();
        List<TrainInfo> trainInfos = accountDao.selectUserTaskInfo(username);
        for (TrainInfo trainInfo : trainInfos) {
            trainInfoContext.putNode(trainInfo.getTimestamp(),trainInfo);
        }

        try {
            webSocketService.nodeInCall("in");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trainInfos;
    }
}
