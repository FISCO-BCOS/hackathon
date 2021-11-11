package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.RingSigPrecompiledRingSigVerifyInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class RingSigPrecompiledService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/RingSigPrecompiled.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/RingSigPrecompiled.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/RingSigPrecompiled.bin");

  @Value("${system.contract.ringSigPrecompiledAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse ringSigVerify(RingSigPrecompiledRingSigVerifyInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "ringSigVerify", input.toArgs());
  }
}
