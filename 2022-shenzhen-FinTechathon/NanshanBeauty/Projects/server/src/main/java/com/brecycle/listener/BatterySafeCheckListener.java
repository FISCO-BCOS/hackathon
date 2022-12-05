package com.brecycle.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.brecycle.entity.dto.BatterySafeCheckParam;
import com.brecycle.service.BatteryService;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电池安全认证监听器
 *
 * @author cmgun
 */
@Slf4j
@Component
public class BatterySafeCheckListener implements IWeEventClient.EventListener {

    @Autowired
    BatteryService batteryService;

    @Override
    public void onEvent(WeEvent event) {
        log.info("电池安全认证监听器，收到event:{}", event);
        List<BatterySafeCheckParam> contents = JSONArray.parseArray(new String(event.getContent()), BatterySafeCheckParam.class);
        for (BatterySafeCheckParam content : contents) {
            try {
                batteryService.safeCheck(content);
            } catch (Exception e) {
                log.error("电池安全认证监听器，消息处理失败，当前参数:{}", JSON.toJSON(content), e);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("电池安全认证监听器，消息处理异常", e);
    }
}
