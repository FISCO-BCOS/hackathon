package org.prepay.prepay.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.prepay.prepay.model.bo.ShopAccountsChangeBalanceInputBO;
import org.prepay.prepay.model.bo.ShopAccountsChangeLeverageInputBO;
import org.prepay.prepay.model.bo.ShopAccountsCreateAccountInputBO;
import org.prepay.prepay.model.bo.ShopAccountsSelectByShopIDInputBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class ShopAccountsService {
  public static final String ABI = org.prepay.prepay.utils.IOUtil.readResourceAsString("abi/ShopAccounts.abi");

  public static final String BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/ecc/ShopAccounts.bin");

  public static final String SM_BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/sm/ShopAccounts.bin");

  @Value("${system.contract.shopAccountsAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse changeBalance(ShopAccountsChangeBalanceInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changeBalance", input.toArgs());
  }

  public TransactionResponse selectByShopID(ShopAccountsSelectByShopIDInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "selectByShopID", input.toArgs());
  }

  public TransactionResponse createAccount(ShopAccountsCreateAccountInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "createAccount", input.toArgs());
  }

  public CallResponse tableName() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "tableName", Arrays.asList());
  }

  public TransactionResponse changeLeverage(ShopAccountsChangeLeverageInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changeLeverage", input.toArgs());
  }
}
