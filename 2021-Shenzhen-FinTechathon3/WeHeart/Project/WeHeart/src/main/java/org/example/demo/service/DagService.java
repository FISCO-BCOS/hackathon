package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.DAGGetCommitHashInputBO;
import org.example.demo.model.bo.DAGGetblocknumberInputBO;
import org.example.demo.model.bo.DAGGetchildrenInputBO;
import org.example.demo.model.bo.DAGGetparentsInputBO;
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
public class DagService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/DAG.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/DAG.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/DAG.bin");

  @Value("${system.contract.dAGAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse getCommitHash(DAGGetCommitHashInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getCommitHash", input.toArgs());
  }

  public CallResponse getblocknumber(DAGGetblocknumberInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getblocknumber", input.toArgs());
  }

  public CallResponse getchildren(DAGGetchildrenInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getchildren", input.toArgs());
  }

  public CallResponse getparents(DAGGetparentsInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getparents", input.toArgs());
  }
}
