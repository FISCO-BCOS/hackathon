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
public class TableRemoveInputBO {
  private String arg0;

  private String arg1;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(arg0);
    args.add(arg1);
    return args;
  }
}
