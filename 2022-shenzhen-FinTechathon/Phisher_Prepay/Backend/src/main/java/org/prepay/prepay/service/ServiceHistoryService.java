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
import org.prepay.prepay.model.bo.ServiceHistoryChangeComplaintInputBO;
import org.prepay.prepay.model.bo.ServiceHistoryCreateHistoryInputBO;
import org.prepay.prepay.model.bo.ServiceHistorySelectByServiceIDInputBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class ServiceHistoryService {
  public static final String ABI = org.prepay.prepay.utils.IOUtil.readResourceAsString("abi/ServiceHistory.abi");

  public static final String BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/ecc/ServiceHistory.bin");

  public static final String SM_BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/sm/ServiceHistory.bin");

  @Value("${system.contract.serviceHistoryAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse changeComplaint(ServiceHistoryChangeComplaintInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changeComplaint", input.toArgs());
  }

  public CallResponse tableName() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "tableName", Arrays.asList());
  }

  public TransactionResponse createHistory(ServiceHistoryCreateHistoryInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "createHistory", input.toArgs());
  }

  public TransactionResponse selectByServiceID(ServiceHistorySelectByServiceIDInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "selectByServiceID", input.toArgs());
  }
}
