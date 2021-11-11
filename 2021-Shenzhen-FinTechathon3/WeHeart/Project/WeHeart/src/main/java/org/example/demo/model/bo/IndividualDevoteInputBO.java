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
public class IndividualDevoteInputBO {
  private BigInteger id;

  private BigInteger _value;

  private String _programType;

  private String _message;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(id);
    args.add(_value);
    args.add(_programType);
    args.add(_message);
    return args;
  }
}
