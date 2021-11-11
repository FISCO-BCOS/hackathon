package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.CallRingSigRingSigVerifyInputBO;
import org.example.demo.model.bo.CallRingSigRing_sigInputBO;
import org.example.demo.model.bo.CallRingSigSet_messageInputBO;
import org.example.demo.model.bo.CallRingSigSet_targetInputBO;
import org.example.demo.model.bo.CallRingSigSetup_ringInputBO;
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
public class CallRingSigService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/CallRingSig.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/CallRingSig.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/CallRingSig.bin");

  @Value("${system.contract.callRingSigAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse ringSigVerify(CallRingSigRingSigVerifyInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "ringSigVerify", input.toArgs());
  }

  public TransactionResponse get_message() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "get_message", Arrays.asList());
  }

  public TransactionResponse get_target() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "get_target", Arrays.asList());
  }

  public TransactionResponse set_target(CallRingSigSet_targetInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "set_target", input.toArgs());
  }

  public TransactionResponse set_message(CallRingSigSet_messageInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "set_message", input.toArgs());
  }

  public TransactionResponse ring_sig(CallRingSigRing_sigInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "ring_sig", input.toArgs());
  }

  public TransactionResponse setup_ring(CallRingSigSetup_ringInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "setup_ring", input.toArgs());
  }
}
