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
public class IntegralGetBalanceInputBO {
  private String _user;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_user);
    return args;
  }
}
