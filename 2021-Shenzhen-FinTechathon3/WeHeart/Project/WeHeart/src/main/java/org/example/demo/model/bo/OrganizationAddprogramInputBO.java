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
public class OrganizationAddprogramInputBO {
  private BigInteger totalneed;

  private String title;

  private String programType;

  private String description;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(totalneed);
    args.add(title);
    args.add(programType);
    args.add(description);
    return args;
  }
}
