package com.brecycle.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.brecycle.entity.User;
import com.brecycle.entity.dto.BatteryCarInfoParam;
import com.brecycle.mapper.UserMapper;
import com.brecycle.service.BatteryService;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电池信息登记-车辆信息监听器
 *
 * @author cmgun
 */
@Slf4j
@Component
public class BatteryCarInfoListener implements IWeEventClient.EventListener {

    @Autowired
    BatteryService batteryService;
    @Autowired
    UserMapper userMapper;

    @Override
    public void onEvent(WeEvent event) {
        log.info("电池信息登记-车辆信息监听器，收到event:{}", event);
        List<BatteryCarInfoParam> contents = JSONArray.parseArray(new String(event.getContent()), BatteryCarInfoParam.class);
        for (BatteryCarInfoParam content : contents) {
            try {
                // 信息登记
                User carEnt = userMapper.selectByUserName(content.getCarEntUserName());
                batteryService.saveCarInfo(carEnt, content);
            } catch (Exception e) {
                log.error("电池信息登记-车辆信息监听器，消息处理失败，当前参数:{}", JSON.toJSON(content), e);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("电池录入监听器，消息处理异常", e);
    }
}
