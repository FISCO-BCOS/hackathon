package org.example.demo.model.bo;

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
public class CallRingSigSet_targetInputBO {
  private String rand_G;

  private String target_addrG;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(rand_G);
    args.add(target_addrG);
    return args;
  }
}
