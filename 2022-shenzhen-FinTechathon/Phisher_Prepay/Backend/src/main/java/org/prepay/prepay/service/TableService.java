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
import org.prepay.prepay.model.bo.TableInsertInputBO;
import org.prepay.prepay.model.bo.TableRemoveInputBO;
import org.prepay.prepay.model.bo.TableSelectInputBO;
import org.prepay.prepay.model.bo.TableUpdateInputBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class TableService {
  public static final String ABI = org.prepay.prepay.utils.IOUtil.readResourceAsString("abi/Table.abi");

  public static final String BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/ecc/Table.bin");

  public static final String SM_BINARY = org.prepay.prepay.utils.IOUtil.readResourceAsString("bin/sm/Table.bin");

  @Value("${system.contract.tableAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse select(TableSelectInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "select", input.toArgs());
  }

  public CallResponse newCondition() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "newCondition", Arrays.asList());
  }

  public TransactionResponse insert(TableInsertInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "insert", input.toArgs());
  }

  public CallResponse newEntry() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "newEntry", Arrays.asList());
  }

  public TransactionResponse remove(TableRemoveInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "remove", input.toArgs());
  }

  public TransactionResponse update(TableUpdateInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "update", input.toArgs());
  }
}
