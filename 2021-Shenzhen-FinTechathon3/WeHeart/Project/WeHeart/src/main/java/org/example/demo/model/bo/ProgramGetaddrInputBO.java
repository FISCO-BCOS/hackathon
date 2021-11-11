package org.example.demo.model.bo;

import java.lang.Object;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramGetaddrInputBO {
  private BigInteger id;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(id);
    return args;
  }
}
