package com.brecycle.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.brecycle.entity.dto.BatteryEndParam;
import com.brecycle.entity.dto.BatteryTransferParam;
import com.brecycle.enums.BatteryStatus;
import com.brecycle.service.BatteryService;
import com.brecycle.service.PointService;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电池拆解监听器
 *
 * @author cmgun
 */
@Slf4j
@Component
public class BatteryEndListener implements IWeEventClient.EventListener {

    @Autowired
    BatteryService batteryService;
    @Autowired
    PointService pointService;

    @Override
    public void onEvent(WeEvent event) {
        log.info("电池拆解监听器，收到event:{}", event);
        List<BatteryEndParam> contents = JSONArray.parseArray(new String(event.getContent()), BatteryEndParam.class);
        for (BatteryEndParam content : contents) {
            try {
                // 电池流转
                if (!StringUtils.equals(content.getOriginUserName(), content.getToUserName())) {
                    // 流转1：梯次利用→回收商，2nd Recycle
                    // 流转2：回收商→回收商，1nd end life
                    BatteryTransferParam transferParam = new BatteryTransferParam();
                    transferParam.setId(content.getId());
                    transferParam.setOriginUserName(content.getOriginUserName());
                    transferParam.setToUserName(content.getToUserName());
                    transferParam.setRemark("电池拆解回收");
                    batteryService.transfer(transferParam, BatteryStatus.RECYCLE.getValue());
                }
                // 电池寿命终结
                batteryService.endLife(content);
                // FIXME 积分计算
                if (content.getSecondUsed()) {
                    // 有梯次利用
                    pointService.secondUsedPoint(content);
                }
                // 拆解商（回收商）派发积分
                pointService.secondRecyclePoint(content);

            } catch (Exception e) {
                log.error("电池拆解监听器，消息处理失败，当前参数:{}", JSON.toJSON(content), e);
            }
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("电池录入监听器，消息处理异常", e);
    }
}
