package com.brecycle.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.brecycle.entity.dto.BatteryTransferParam;
import com.brecycle.service.BatteryService;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电池流转监听器
 *
 * @author cmgun
 */
@Slf4j
@Component
public class BatteryTransferListener implements IWeEventClient.EventListener {

    @Autowired
    BatteryService batteryService;

    @Override
    public void onEvent(WeEvent event) {
        log.info("电池流转监听器，收到event:{}", event);
        List<BatteryTransferParam> contents = JSONArray.parseArray(new String(event.getContent()), BatteryTransferParam.class);
        for (BatteryTransferParam content : contents) {
            try {
                batteryService.transfer(content, null);
            } catch (Exception e) {
                log.error("电池流转监听器，消息处理失败，当前参数:{}", JSON.toJSON(content), e);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("电池流转监听器，消息处理异常", e);
    }
}
