package com.brecycle.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.brecycle.controller.hanlder.BusinessException;
import com.brecycle.entity.dto.BatteryTransferParam;
import com.brecycle.entity.dto.CustomerTransferParam;
import com.brecycle.enums.BatteryStatus;
import com.brecycle.service.BatteryService;
import com.brecycle.service.PointService;
import com.google.common.collect.Lists;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费者电池流转监听器
 *
 * @author cmgun
 */
@Slf4j
@Component
public class CustomerTransferListener implements IWeEventClient.EventListener {

    @Autowired
    BatteryService batteryService;
    @Autowired
    PointService pointService;

    @Override
    public void onEvent(WeEvent event) {
        log.info("消费者电池流转监听器，收到event:{}", event);
        List<CustomerTransferParam> contents = JSONArray.parseArray(new String(event.getContent()), CustomerTransferParam.class);
        for (CustomerTransferParam content : contents) {
            try {
                // 必须有相关参数
                Long chargeTimes = content.getChargeTimes();
                if (chargeTimes == null) {
                    throw new BusinessException("充放电次数不可为空");
                }
                batteryService.transfer(content, BatteryStatus.WAIT_RECYCLE.getValue());
                // 消费者积分派发
                pointService.customerPoint(content);
            } catch (Exception e) {
                log.error("消费者电池流转监听器，消息处理失败，当前参数:{}", JSON.toJSON(content), e);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("电池流转监听器，消息处理异常", e);
    }
}
