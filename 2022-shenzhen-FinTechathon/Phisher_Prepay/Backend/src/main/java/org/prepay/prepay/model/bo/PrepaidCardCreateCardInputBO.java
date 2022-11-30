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
public class PrepaidCardCreateCardInputBO {
  private String _cardID;

  private String _consumerID;

  private String _shopID;

  private BigInteger _balance;

  private BigInteger _contractHash;

  private BigInteger _createTime;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_cardID);
    args.add(_consumerID);
    args.add(_shopID);
    args.add(_balance);
    args.add(_contractHash);
    args.add(_createTime);
    return args;
  }
}
