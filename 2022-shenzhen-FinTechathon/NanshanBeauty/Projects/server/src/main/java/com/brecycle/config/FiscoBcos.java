package com.brecycle.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * fisco bcos配置
 * @author cmgun
 */
@Slf4j
@Data
@Component
public class FiscoBcos {

    @Autowired
    private FiscoConfig fiscoConfig;

    private BcosSDK bcosSDK;

    @PostConstruct
    public void init() {
        ConfigProperty configProperty = loadProperty();
        try {
            ConfigOption configOption = new ConfigOption(configProperty, CryptoType.ECDSA_TYPE);
            bcosSDK = new BcosSDK(configOption);
        } catch (ConfigException e) {
            log.error("init error", e);
        }
    }

    private ConfigProperty loadProperty() {

        ConfigProperty configProperty = new ConfigProperty();
        configProperty.setCryptoMaterial(fiscoConfig.getCryptoMaterial());
        configProperty.setAccount(fiscoConfig.getAccount());
        Map network = fiscoConfig.getNetwork();
        configProperty.setNetwork(network);
        configProperty.setAmop(fiscoConfig.getAmop());
        configProperty.setThreadPool(fiscoConfig.getThreadPool());
        return configProperty;
    }
}
