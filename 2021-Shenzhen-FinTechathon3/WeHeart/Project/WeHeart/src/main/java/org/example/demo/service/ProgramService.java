package org.example.demo.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.ProgramAddProgramInputBO;
import org.example.demo.model.bo.ProgramAddTolistInputBO;
import org.example.demo.model.bo.ProgramAdddevoterInputBO;
import org.example.demo.model.bo.ProgramChangestatusInputBO;
import org.example.demo.model.bo.ProgramChangetotalInputBO;
import org.example.demo.model.bo.ProgramChangevalueInputBO;
import org.example.demo.model.bo.ProgramGetaddrInputBO;
import org.example.demo.model.bo.ProgramGetdevoterInputBO;
import org.example.demo.model.bo.ProgramGetinforInputBO;
import org.example.demo.model.bo.ProgramGetisexistInputBO;
import org.example.demo.model.bo.ProgramGetnumberInputBO;
import org.example.demo.model.bo.ProgramGetprocessInputBO;
import org.example.demo.model.bo.ProgramGetprogramInputBO;
import org.example.demo.model.bo.ProgramGetstatusInputBO;
import org.example.demo.model.bo.ProgramGetstringinforInputBO;
import org.example.demo.model.bo.ProgramGettotalInputBO;
import org.example.demo.model.bo.ProgramGetvalueInputBO;
import org.example.demo.model.bo.ProgramImplementInputBO;
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
public class ProgramService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/Program.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/Program.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/Program.bin");

  @Value("${system.contract.programAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse changestatus(ProgramChangestatusInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changestatus", input.toArgs());
  }

  public CallResponse getaddr(ProgramGetaddrInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getaddr", input.toArgs());
  }

  public TransactionResponse addTolist(ProgramAddTolistInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addTolist", input.toArgs());
  }

  public CallResponse getstringinfor(ProgramGetstringinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getstringinfor", input.toArgs());
  }

  public TransactionResponse implement(ProgramImplementInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "implement", input.toArgs());
  }

  public CallResponse getvalue(ProgramGetvalueInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getvalue", input.toArgs());
  }

  public CallResponse getstatus(ProgramGetstatusInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getstatus", input.toArgs());
  }

  public CallResponse getprocess(ProgramGetprocessInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getprocess", input.toArgs());
  }

  public TransactionResponse changevalue(ProgramChangevalueInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changevalue", input.toArgs());
  }

  public TransactionResponse addProgram(ProgramAddProgramInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addProgram", input.toArgs());
  }

  public CallResponse getisexist(ProgramGetisexistInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getisexist", input.toArgs());
  }

  public CallResponse getnumber(ProgramGetnumberInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getnumber", input.toArgs());
  }

  public CallResponse getdevoter(ProgramGetdevoterInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getdevoter", input.toArgs());
  }

  public CallResponse getinfor(ProgramGetinforInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getinfor", input.toArgs());
  }

  public TransactionResponse changetotal(ProgramChangetotalInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "changetotal", input.toArgs());
  }

  public CallResponse getprogram(ProgramGetprogramInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getprogram", input.toArgs());
  }

  public CallResponse gettotal(ProgramGettotalInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "gettotal", input.toArgs());
  }

  public TransactionResponse adddevoter(ProgramAdddevoterInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "adddevoter", input.toArgs());
  }
}
