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
public class ComplaintComplaintInputBO {
  private String _serviceID;

  private String _content;

  private String _cardID;

  private BigInteger _now;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_serviceID);
    args.add(_content);
    args.add(_cardID);
    args.add(_now);
    return args;
  }
}
