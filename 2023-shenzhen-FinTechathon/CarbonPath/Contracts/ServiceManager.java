package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.config.SystemConfig;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Slf4j
public class ServiceManager {
  @Autowired
  private SystemConfig config;

  @Autowired
  private Client client;

  List<String> hexPrivateKeyList;

  @PostConstruct
  public void init() {
    hexPrivateKeyList = Arrays.asList(this.config.getHexPrivateKey().split(","));
  }

  /**
   * @notice: must use @Qualifier("EmissionAccountingService") with @Autowired to get this Bean
   */
  @Bean("EmissionAccountingService")
  public Map<String, EmissionAccountingService> initEmissionAccountingServiceManager() throws Exception {
    Map<String, EmissionAccountingService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
    for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
    	String privateKey = this.hexPrivateKeyList.get(i);
    	if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
    		privateKey = privateKey.substring(2);
    	}
    	if (privateKey.isEmpty()) {
    		continue;
    	}
    	org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
    	org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    	String userAddress = cryptoKeyPair.getAddress();
    	log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
    	EmissionAccountingService emissionAccountingService = new EmissionAccountingService();
    	emissionAccountingService.setAddress(this.config.getContract().getEmissionAccountingAddress());
    	emissionAccountingService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	emissionAccountingService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, emissionAccountingService);
    }
    log.info("++++++++EmissionAccountingService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("ClearanceService") with @Autowired to get this Bean
   */
  @Bean("ClearanceService")
  public Map<String, ClearanceService> initClearanceServiceManager() throws Exception {
    Map<String, ClearanceService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
    for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
    	String privateKey = this.hexPrivateKeyList.get(i);
    	if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
    		privateKey = privateKey.substring(2);
    	}
    	if (privateKey.isEmpty()) {
    		continue;
    	}
    	org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
    	org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    	String userAddress = cryptoKeyPair.getAddress();
    	log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
    	ClearanceService clearanceService = new ClearanceService();
    	clearanceService.setAddress(this.config.getContract().getClearanceAddress());
    	clearanceService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	clearanceService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, clearanceService);
    }
    log.info("++++++++ClearanceService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("CEA_TransactionService") with @Autowired to get this Bean
   */
  @Bean("CEA_TransactionService")
  public Map<String, CEA_TransactionService> initCEA_TransactionServiceManager() throws Exception {
    Map<String, CEA_TransactionService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
    for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
    	String privateKey = this.hexPrivateKeyList.get(i);
    	if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
    		privateKey = privateKey.substring(2);
    	}
    	if (privateKey.isEmpty()) {
    		continue;
    	}
    	org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
    	org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    	String userAddress = cryptoKeyPair.getAddress();
    	log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
    	CEA_TransactionService cEA_TransactionService = new CEA_TransactionService();
    	cEA_TransactionService.setAddress(this.config.getContract().getCEA_TransactionAddress());
    	cEA_TransactionService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	cEA_TransactionService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, cEA_TransactionService);
    }
    log.info("++++++++CEA_TransactionService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("CCER_TransactionService") with @Autowired to get this Bean
   */
  @Bean("CCER_TransactionService")
  public Map<String, CCER_TransactionService> initCCER_TransactionServiceManager() throws Exception {
    Map<String, CCER_TransactionService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
    for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
    	String privateKey = this.hexPrivateKeyList.get(i);
    	if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
    		privateKey = privateKey.substring(2);
    	}
    	if (privateKey.isEmpty()) {
    		continue;
    	}
    	org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
    	org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    	String userAddress = cryptoKeyPair.getAddress();
    	log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
    	CCER_TransactionService cCER_TransactionService = new CCER_TransactionService();
    	cCER_TransactionService.setAddress(this.config.getContract().getCCER_TransactionAddress());
    	cCER_TransactionService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	cCER_TransactionService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, cCER_TransactionService);
    }
    log.info("++++++++CCER_TransactionService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("CCER_HistoryService") with @Autowired to get this Bean
   */
  @Bean("CCER_HistoryService")
  public Map<String, CCER_HistoryService> initCCER_HistoryServiceManager() throws Exception {
    Map<String, CCER_HistoryService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
    for (int i = 0; i < this.hexPrivateKeyList.size(); i++) {
    	String privateKey = this.hexPrivateKeyList.get(i);
    	if (privateKey.startsWith("0x") || privateKey.startsWith("0X")) {
    		privateKey = privateKey.substring(2);
    	}
    	if (privateKey.isEmpty()) {
    		continue;
    	}
    	org.fisco.bcos.sdk.crypto.CryptoSuite cryptoSuite = new org.fisco.bcos.sdk.crypto.CryptoSuite(this.client.getCryptoType());
    	org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    	String userAddress = cryptoKeyPair.getAddress();
    	log.info("++++++++hexPrivateKeyList[{}]:{},userAddress:{}", i, privateKey, userAddress);
    	CCER_HistoryService cCER_HistoryService = new CCER_HistoryService();
    	cCER_HistoryService.setAddress(this.config.getContract().getCCER_HistoryAddress());
    	cCER_HistoryService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	cCER_HistoryService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, cCER_HistoryService);
    }
    log.info("++++++++CCER_HistoryService map:{}", serviceMap);
    return serviceMap;
  }
}
