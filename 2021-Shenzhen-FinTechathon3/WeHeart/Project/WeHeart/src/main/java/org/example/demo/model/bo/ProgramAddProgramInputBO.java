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
public class ProgramAddProgramInputBO {
  private String addr;

  private BigInteger totalneed;

  private String title;

  private String programtype;

  private String description;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(addr);
    args.add(totalneed);
    args.add(title);
    args.add(programtype);
    args.add(description);
    return args;
  }
}
