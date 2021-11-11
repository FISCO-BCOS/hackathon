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
public class ProgramImplementInputBO {
  private BigInteger id;

  private String _describe;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(id);
    args.add(_describe);
    return args;
  }
}
