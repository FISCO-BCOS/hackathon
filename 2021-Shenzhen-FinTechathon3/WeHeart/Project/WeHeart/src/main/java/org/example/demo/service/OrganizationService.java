package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.OrganizationAddprogramInputBO;
import org.example.demo.model.bo.OrganizationChangevalueInputBO;
import org.example.demo.model.bo.OrganizationGetDAGInputBO;
import org.example.demo.model.bo.OrganizationGetdevoterInputBO;
import org.example.demo.model.bo.OrganizationGetinforInputBO;
import org.example.demo.model.bo.OrganizationGetprocessInputBO;
import org.example.demo.model.bo.OrganizationGetstringinforInputBO;
import org.example.demo.model.bo.OrganizationImplementProgramInputBO;
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
public class OrganizationService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/Organization.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/Organization.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/Organization.bin");

  @Value("${system.contract.organizationAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse getstringinfor(OrganizationGetstringinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getstringinfor", input.toArgs());
  }

  public CallResponse getinfor(OrganizationGetinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getinfor", input.toArgs());
  }

  public TransactionResponse getdevoter(OrganizationGetdevoterInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getdevoter", input.toArgs());
  }

  public TransactionResponse getDAG(OrganizationGetDAGInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getDAG", input.toArgs());
  }

  public TransactionResponse getprogram() throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getprogram", Arrays.asList());
  }

  public TransactionResponse changevalue(OrganizationChangevalueInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changevalue", input.toArgs());
  }

  public TransactionResponse addprogram(OrganizationAddprogramInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addprogram", input.toArgs());
  }

  public CallResponse getprocess(OrganizationGetprocessInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getprocess", input.toArgs());
  }

  public TransactionResponse implementProgram(OrganizationImplementProgramInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "implementProgram", input.toArgs());
  }
}
