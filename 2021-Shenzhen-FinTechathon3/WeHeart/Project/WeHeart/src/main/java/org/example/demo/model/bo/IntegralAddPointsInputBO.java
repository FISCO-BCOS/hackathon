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
public class IntegralAddPointsInputBO {
  private String _user;

  private BigInteger _value;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_user);
    args.add(_value);
    return args;
  }
}
