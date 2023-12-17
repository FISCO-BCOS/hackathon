package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.CCER_TransactionGetTransaction1InputBO;
import org.example.demo.model.bo.CCER_TransactionGetTransaction2InputBO;
import org.example.demo.model.bo.CCER_TransactionGetTransaction3InputBO;
import org.example.demo.model.bo.CCER_Transaction_txrRecord1InputBO;
import org.example.demo.model.bo.CCER_Transaction_txrRecord2InputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class CCER_TransactionService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/CCER_Transaction.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/CCER_Transaction.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/CCER_Transaction.bin");

  @Value("${system.contract.cCER_TransactionAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse _getSize() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "_getSize", Arrays.asList());
  }

  public CallResponse getTransaction3(CCER_TransactionGetTransaction3InputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTransaction3", input.toArgs());
  }

  public CallResponse getTransaction2(CCER_TransactionGetTransaction2InputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTransaction2", input.toArgs());
  }

  public TransactionResponse _txrRecord2(CCER_Transaction_txrRecord2InputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "_txrRecord2", input.toArgs());
  }

  public TransactionResponse _txrRecord1(CCER_Transaction_txrRecord1InputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "_txrRecord1", input.toArgs());
  }

  public CallResponse getTransaction1(CCER_TransactionGetTransaction1InputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTransaction1", input.toArgs());
  }
}
