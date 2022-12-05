package com.brecycle.config;

import com.brecycle.listener.*;
import com.webank.weevent.client.IWeEventClient;
import com.webank.weevent.client.WeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cmgun
 */
@Configuration
public class WeEventClientConfig {

    @Autowired
    WeEventConfig eventConfig;
    @Autowired
    BatteryAddListener batteryAddListener;
    @Autowired
    BatterySafeCheckListener batterySafeCheckListener;
    @Autowired
    BatteryTransferListener batteryTransferListener;
    @Autowired
    CustomerTransferListener customerTransferListener;
    @Autowired
    BatteryEndListener batteryEndListener;
    @Autowired
    BatteryCarInfoListener batteryCarInfoListener;

    @Bean
    public IWeEventClient clientConfig() throws Exception {
        IWeEventClient client = IWeEventClient.builder()
                .brokerUrl(eventConfig.getBrokerUrl())
                .groupId(WeEvent.DEFAULT_GROUP_ID)
                .build();
        // 打开topic
        client.open(eventConfig.getBatteryAddTopic());
        client.open(eventConfig.getSafeCheckTopic());
        client.open(eventConfig.getProductorTransferTopic());
        client.open(eventConfig.getRentTransferTopic());
        client.open(eventConfig.getCarTransferTopic());
        client.open(eventConfig.getCustomerTransferTopic());
        client.open(eventConfig.getBatteryEndTopic());
        client.open(eventConfig.getCarInfoTopic());
        // 绑定监听器
        client.subscribe(eventConfig.getBatteryAddTopic(), WeEvent.OFFSET_LAST, null, batteryAddListener);
        client.subscribe(eventConfig.getSafeCheckTopic(), WeEvent.OFFSET_LAST, null, batterySafeCheckListener);
        client.subscribe(eventConfig.getProductorTransferTopic(), WeEvent.OFFSET_LAST, null, batteryTransferListener);
        client.subscribe(eventConfig.getRentTransferTopic(), WeEvent.OFFSET_LAST, null, batteryTransferListener);
        client.subscribe(eventConfig.getCarTransferTopic(), WeEvent.OFFSET_LAST, null, batteryTransferListener);
        client.subscribe(eventConfig.getCustomerTransferTopic(), WeEvent.OFFSET_LAST, null, customerTransferListener);
        client.subscribe(eventConfig.getBatteryEndTopic(), WeEvent.OFFSET_LAST, null, batteryEndListener);
        client.subscribe(eventConfig.getCarInfoTopic(), WeEvent.OFFSET_LAST, null, batteryCarInfoListener);
        return client;
    }
}
