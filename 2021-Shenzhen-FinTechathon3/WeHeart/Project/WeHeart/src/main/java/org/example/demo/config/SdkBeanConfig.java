package org.example.demo.config;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SdkBeanConfig {

    @Autowired
    private SystemConfig config;

    @Bean
    public Client client() throws Exception {
        String certPaths = this.config.getCertPath();
        String[] possibilities = certPaths.split(",|;");
        for(String certPath: possibilities ) {
            try{
                ConfigProperty property = new ConfigProperty();
                configNetwork(property);
                configCryptoMaterial(property,certPath);

                ConfigOption configOption = new ConfigOption(property);
                Client client = new BcosSDK(configOption).getClient(config.getGroupId());

                BigInteger blockNumber = client.getBlockNumber().getBlockNumber();
                log.info("Chain connect successful. Current block number {}", blockNumber);

                configCryptoKeyPair(client);
                log.info("is Gm:{}, address:{}", client.getCryptoSuite().cryptoTypeConfig == 1, client.getCryptoSuite().getCryptoKeyPair().getAddress());
                return client;
            }
            catch (Exception ex) {
                log.error(ex.getMessage());
                try{
                    Thread.sleep(5000);
                }catch (Exception e) {}
            }
        }
        throw new ConfigException("Failed to connect to peers:" + config.getPeers());
    }

    public void configNetwork(ConfigProperty configProperty) {
        String peerStr = config.getPeers();
        List<String> peers = Arrays.stream(peerStr.split(",")).collect(Collectors.toList());
        Map<String, Object> networkConfig = new HashMap<>();
        networkConfig.put("peers", peers);

        configProperty.setNetwork(networkConfig);
    }

    public void configCryptoMaterial(ConfigProperty configProperty,String certPath) {
        Map<String, Object> cryptoMaterials = new HashMap<>();
        cryptoMaterials.put("certPath", certPath);
        configProperty.setCryptoMaterial(cryptoMaterials);
    }

    public void configCryptoKeyPair(Client client) {
        if (config.getHexPrivateKey() == null || config.getHexPrivateKey().isEmpty()){
            client.getCryptoSuite().setCryptoKeyPair(client.getCryptoSuite().createKeyPair());
            return;
        }
        String privateKey;
        if (!config.getHexPrivateKey().contains(",")) {
            privateKey = config.getHexPrivateKey();
        } else {
            String[] list = config.getHexPrivateKey().split(",");
            privateKey = list[0];
        }
        if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
            privateKey = privateKey.substring(2);
            config.setHexPrivateKey(privateKey);
        }
        client.getCryptoSuite().setCryptoKeyPair(client.getCryptoSuite().createKeyPair(privateKey));
    }
}
