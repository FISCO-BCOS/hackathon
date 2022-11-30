package org.prepay.prepay.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.prepay.prepay.config.SystemConfig;
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
   * @notice: must use @Qualifier("TransferService") with @Autowired to get this Bean
   */
  @Bean("TransferService")
  public Map<String, TransferService> initTransferServiceManager() throws Exception {
    Map<String, TransferService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	TransferService transferService = new TransferService();
    	transferService.setAddress(this.config.getContract().getTransferAddress());
    	transferService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	transferService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, transferService);
    }
    log.info("++++++++TransferService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("TableService") with @Autowired to get this Bean
   */
  @Bean("TableService")
  public Map<String, TableService> initTableServiceManager() throws Exception {
    Map<String, TableService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	TableService tableService = new TableService();
    	tableService.setAddress(this.config.getContract().getTableAddress());
    	tableService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	tableService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, tableService);
    }
    log.info("++++++++TableService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("ShopAccountsService") with @Autowired to get this Bean
   */
  @Bean("ShopAccountsService")
  public Map<String, ShopAccountsService> initShopAccountsServiceManager() throws Exception {
    Map<String, ShopAccountsService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	ShopAccountsService shopAccountsService = new ShopAccountsService();
    	shopAccountsService.setAddress(this.config.getContract().getShopAccountsAddress());
    	shopAccountsService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	shopAccountsService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, shopAccountsService);
    }
    log.info("++++++++ShopAccountsService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("ServiceHistoryService") with @Autowired to get this Bean
   */
  @Bean("ServiceHistoryService")
  public Map<String, ServiceHistoryService> initServiceHistoryServiceManager() throws Exception {
    Map<String, ServiceHistoryService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	ServiceHistoryService serviceHistoryService = new ServiceHistoryService();
    	serviceHistoryService.setAddress(this.config.getContract().getServiceHistoryAddress());
    	serviceHistoryService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	serviceHistoryService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, serviceHistoryService);
    }
    log.info("++++++++ServiceHistoryService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("RefundService") with @Autowired to get this Bean
   */
  @Bean("RefundService")
  public Map<String, RefundService> initRefundServiceManager() throws Exception {
    Map<String, RefundService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	RefundService refundService = new RefundService();
    	refundService.setAddress(this.config.getContract().getRefundAddress());
    	refundService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	refundService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, refundService);
    }
    log.info("++++++++RefundService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("PrepaidCardService") with @Autowired to get this Bean
   */
  @Bean("PrepaidCardService")
  public Map<String, PrepaidCardService> initPrepaidCardServiceManager() throws Exception {
    Map<String, PrepaidCardService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	PrepaidCardService prepaidCardService = new PrepaidCardService();
    	prepaidCardService.setAddress(this.config.getContract().getPrepaidCardAddress());
    	prepaidCardService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	prepaidCardService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, prepaidCardService);
    }
    log.info("++++++++PrepaidCardService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("LeverageService") with @Autowired to get this Bean
   */
  @Bean("LeverageService")
  public Map<String, LeverageService> initLeverageServiceManager() throws Exception {
    Map<String, LeverageService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	LeverageService leverageService = new LeverageService();
    	leverageService.setAddress(this.config.getContract().getLeverageAddress());
    	leverageService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	leverageService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, leverageService);
    }
    log.info("++++++++LeverageService map:{}", serviceMap);
    return serviceMap;
  }

  /**
   * @notice: must use @Qualifier("ComplaintService") with @Autowired to get this Bean
   */
  @Bean("ComplaintService")
  public Map<String, ComplaintService> initComplaintServiceManager() throws Exception {
    Map<String, ComplaintService> serviceMap = new ConcurrentHashMap<>(this.hexPrivateKeyList.size());
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
    	ComplaintService complaintService = new ComplaintService();
    	complaintService.setAddress(this.config.getContract().getComplaintAddress());
    	complaintService.setClient(this.client);
    	org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor txProcessor = 
    		org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, cryptoKeyPair);
    	complaintService.setTxProcessor(txProcessor);
    	serviceMap.put(userAddress, complaintService);
    }
    log.info("++++++++ComplaintService map:{}", serviceMap);
    return serviceMap;
  }
}
