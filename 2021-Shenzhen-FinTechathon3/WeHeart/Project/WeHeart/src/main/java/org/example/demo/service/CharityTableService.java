package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.CharityTableCreateInputBO;
import org.example.demo.model.bo.CharityTableInsertInputBO;
import org.example.demo.model.bo.CharityTableSelectInputBO;
import org.example.demo.model.bo.CharityTableUpdateInputBO;
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
public class CharityTableService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/CharityTable.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/CharityTable.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/CharityTable.bin");

  @Value("${system.contract.charityTableAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse select(CharityTableSelectInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "select", input.toArgs());
  }

  public TransactionResponse update(CharityTableUpdateInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "update", input.toArgs());
  }

  public TransactionResponse insert(CharityTableInsertInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "insert", input.toArgs());
  }

  public TransactionResponse create(CharityTableCreateInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "create", input.toArgs());
  }

  public CallResponse table_name() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "table_name", Arrays.asList());
  }
}
