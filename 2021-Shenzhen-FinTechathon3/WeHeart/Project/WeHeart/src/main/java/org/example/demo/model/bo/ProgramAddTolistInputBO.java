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
public class ProgramAddTolistInputBO {
  private String addr;

  private BigInteger id;

  private String title;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(addr);
    args.add(id);
    args.add(title);
    return args;
  }
}
