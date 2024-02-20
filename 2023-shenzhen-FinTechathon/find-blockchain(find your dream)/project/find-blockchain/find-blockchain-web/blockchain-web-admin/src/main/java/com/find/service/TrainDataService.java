package com.find.service;

import com.find.pojo.TrainInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/6
 */


public interface TrainDataService {

    List<TrainInfo> getTrainDataByUser(String username, String timestamp, int dataNums);

    List<TrainInfo> selectUserTaskInfo(String username);
}
