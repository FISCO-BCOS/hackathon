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
public class CallRingSigSetup_ringInputBO {
  private String ring_param_info;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(ring_param_info);
    return args;
  }
}
