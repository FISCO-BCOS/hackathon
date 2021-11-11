package org.example.demo.model.bo;

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
public class ProgramAdddevoterInputBO {
  private BigInteger id;

  private String addr;

  private BigInteger _amount;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(id);
    args.add(addr);
    args.add(_amount);
    return args;
  }
}
