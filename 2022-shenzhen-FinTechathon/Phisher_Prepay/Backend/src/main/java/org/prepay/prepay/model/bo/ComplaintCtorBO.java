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
public class ComplaintCtorBO {
  private String _serviceContract;

  private String _refundContract;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_serviceContract);
    args.add(_refundContract);
    return args;
  }
}
