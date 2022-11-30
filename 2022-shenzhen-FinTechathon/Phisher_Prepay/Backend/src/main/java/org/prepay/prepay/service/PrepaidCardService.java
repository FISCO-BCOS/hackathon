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
import org.prepay.prepay.model.bo.PrepaidCardChangeBalanceInputBO;
import org.prepay.prepay.model.bo.PrepaidCardCreateCardInputBO;
import org.prepay.prepay.model.bo.PrepaidCardDeleteCardInputBO;
import org.prepay.prepay.model.bo.PrepaidCardSelectByCardIDInputBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class PrepaidCardService {
  public static final String ABI = org.prepay.prepay.utils.IOUtil.readResourceAsString("abi/PrepaidCard.abi");

  public static final String BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/ecc/PrepaidCard.bin");

  public static final String SM_BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/sm/PrepaidCard.bin");

  @Value("${system.contract.prepaidCardAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse deleteCard(PrepaidCardDeleteCardInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "deleteCard", input.toArgs());
  }

  public TransactionResponse selectAll() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "selectAll", Arrays.asList());
  }

  public TransactionResponse changeBalance(PrepaidCardChangeBalanceInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changeBalance", input.toArgs());
  }

  public CallResponse tableName() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "tableName", Arrays.asList());
  }

  public TransactionResponse selectByCardID(PrepaidCardSelectByCardIDInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "selectByCardID", input.toArgs());
  }

  public TransactionResponse createCard(PrepaidCardCreateCardInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "createCard", input.toArgs());
  }
}
