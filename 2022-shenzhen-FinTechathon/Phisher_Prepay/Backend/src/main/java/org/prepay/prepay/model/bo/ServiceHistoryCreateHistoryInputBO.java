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
public class ServiceHistoryCreateHistoryInputBO {
  private String _serviceID;

  private String _cardID;

  private String _record;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_serviceID);
    args.add(_cardID);
    args.add(_record);
    return args;
  }
}
