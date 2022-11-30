package org.prepay.prepay.config;

import java.lang.String;
import lombok.Data;

@Data
public class ContractConfig {
  private String shopAccountsAddress;

  private String complaintAddress;

  private String serviceHistoryAddress;

  private String transferAddress;

  private String leverageAddress;

  private String refundAddress;

  private String prepaidCardAddress;

  private String tableAddress;
}
