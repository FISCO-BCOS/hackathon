package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.IntegralAddPointsInputBO;
import org.example.demo.model.bo.IntegralDealPointsInputBO;
import org.example.demo.model.bo.IntegralGetBalanceInputBO;
import org.example.demo.model.bo.IntegralGetTxInforInputBO;
import org.example.demo.model.bo.IntegralGetTxsInputBO;
import org.example.demo.model.bo.IntegralGettxidInputBO;
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
public class IntegralService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/Integral.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/Integral.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/Integral.bin");

  @Value("${system.contract.integralAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse getTxInfor(IntegralGetTxInforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTxInfor", input.toArgs());
  }

  public CallResponse getTxs(IntegralGetTxsInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getTxs", input.toArgs());
  }

  public TransactionResponse dealPoints(IntegralDealPointsInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "dealPoints", input.toArgs());
  }

  public TransactionResponse gettxid(IntegralGettxidInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "gettxid", input.toArgs());
  }

  public TransactionResponse getBalance(IntegralGetBalanceInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "getBalance", input.toArgs());
  }

  public TransactionResponse addPoints(IntegralAddPointsInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addPoints", input.toArgs());
  }
}
