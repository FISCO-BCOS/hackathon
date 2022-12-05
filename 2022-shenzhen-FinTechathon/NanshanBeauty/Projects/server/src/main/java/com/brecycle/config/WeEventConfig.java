package com.brecycle.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cmgun
 */
@Data
@Component
@ConfigurationProperties(prefix = "weevent")
public class WeEventConfig {

    public String brokerUrl;

    public String batteryAddTopic;

    public String safeCheckTopic;

    public String productorTransferTopic;

    public String carTransferTopic;

    public String carInfoTopic;

    public String rentTransferTopic;

    public String customerTransferTopic;

    public String batteryEndTopic;
}
