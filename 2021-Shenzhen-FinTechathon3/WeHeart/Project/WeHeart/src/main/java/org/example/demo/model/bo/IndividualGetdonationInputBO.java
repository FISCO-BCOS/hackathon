package org.example.demo.model.bo;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualGetdonationInputBO {
  private byte[] _txid;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_txid);
    return args;
  }
}
