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
public class IntegralGetTxInforInputBO {
  private byte[] txs_id;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(txs_id);
    return args;
  }
}
