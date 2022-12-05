package com.brecycle.service;

import com.brecycle.entity.User;
import com.brecycle.entity.dto.*;

import java.util.List;

public interface BatteryService {

    /**
     * 新增单个电池
     * @param param
     */
    void add(BatteryInfoParam param) throws Exception;

    /**
     * 安全审查
     * @param param
     * @throws Exception
     */
    void safeCheck(BatterySafeCheckParam param);

    /**
     * 电池流转
     * @param param
     */
    void transfer(BatteryTransferParam param, String batteryStatus);

    /**
     * 电池拆解
     * @param param
     */
    void endLife(BatteryEndParam param) throws Exception;

    /**
     * 获取电池溯源信息
     * @param batteryId
     * @return
     */
    List<TraceInfoDTO> getTraceInfo(String batteryId, String currentUserName) throws Exception;

    /**
     * 电池列表
     * @param param
     * @param currentUserName
     * @return
     * @throws Exception
     */
    PageResult<BatteryListDTO> batteryList(BatteryListParam param, String currentUserName) throws Exception;

    /**
     * 保存汽车生产信息
     * @param car
     * @param param
     */
    void saveCarInfo(User car, BatteryCarInfoParam param) throws Exception;
}
