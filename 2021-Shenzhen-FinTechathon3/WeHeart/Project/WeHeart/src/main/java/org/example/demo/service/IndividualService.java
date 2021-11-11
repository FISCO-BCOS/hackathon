package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.IndividualAddprogramInputBO;
import org.example.demo.model.bo.IndividualChangevalueInputBO;
import org.example.demo.model.bo.IndividualDealPointsInputBO;
import org.example.demo.model.bo.IndividualDevoteInputBO;
import org.example.demo.model.bo.IndividualGetTxInforInputBO;
import org.example.demo.model.bo.IndividualGetdonationInputBO;
import org.example.demo.model.bo.IndividualGetinforInputBO;
import org.example.demo.model.bo.IndividualGetprocessInputBO;
import org.example.demo.model.bo.IndividualGetstringinforInputBO;
import org.example.demo.model.bo.IndividualImplementProgramInputBO;
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
public class IndividualService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/Individual.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/Individual.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/Individual.bin");

  @Value("${system.contract.individualAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse getdonationids() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getdonationids", Arrays.asList());
  }

  public CallResponse getstringinfor(IndividualGetstringinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getstringinfor", input.toArgs());
  }

  public TransactionResponse dealPoints(IndividualDealPointsInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "dealPoints", input.toArgs());
  }

  public CallResponse getTxInfor(IndividualGetTxInforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTxInfor", input.toArgs());
  }

  public TransactionResponse changevalue(IndividualChangevalueInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changevalue", input.toArgs());
  }

  public TransactionResponse addprogram(IndividualAddprogramInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addprogram", input.toArgs());
  }

  public CallResponse getprocess(IndividualGetprocessInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getprocess", input.toArgs());
  }

  public TransactionResponse implementProgram(IndividualImplementProgramInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "implementProgram", input.toArgs());
  }

  public TransactionResponse getdonation(IndividualGetdonationInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getdonation", input.toArgs());
  }

  public TransactionResponse getTxs() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getTxs", Arrays.asList());
  }

  public TransactionResponse getBalance() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getBalance", Arrays.asList());
  }

  public CallResponse getinfor(IndividualGetinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getinfor", input.toArgs());
  }

  public TransactionResponse devote(IndividualDevoteInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "devote", input.toArgs());
  }

  public TransactionResponse getprogram() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getprogram", Arrays.asList());
  }
}
