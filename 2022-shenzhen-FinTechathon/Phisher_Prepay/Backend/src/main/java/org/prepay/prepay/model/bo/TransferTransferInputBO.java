package org.prepay.prepay.model.bo;

import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransferInputBO {
  private String _cardID;

  private String _shopID;

  private String _serviceID;

  private String _record;

  private BigInteger _value;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_cardID);
    args.add(_shopID);
    args.add(_serviceID);
    args.add(_record);
    args.add(_value);
    return args;
  }
}
