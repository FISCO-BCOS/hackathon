package org.prepay.prepay.model.bo;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferCtorBO {
  private String _cardContract;

  private String _shopContract;

  private String _leverageContract;

  private String _serviceContract;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_cardContract);
    args.add(_shopContract);
    args.add(_leverageContract);
    args.add(_serviceContract);
    return args;
  }
}
