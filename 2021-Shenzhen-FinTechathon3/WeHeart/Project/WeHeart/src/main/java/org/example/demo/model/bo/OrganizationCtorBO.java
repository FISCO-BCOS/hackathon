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
public class OrganizationCtorBO {
  private String Programaddr;

  private String DAGaddr;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(Programaddr);
    args.add(DAGaddr);
    return args;
  }
}
