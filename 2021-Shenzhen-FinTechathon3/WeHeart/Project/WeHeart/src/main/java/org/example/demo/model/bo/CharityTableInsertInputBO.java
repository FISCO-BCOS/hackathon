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
public class CharityTableInsertInputBO {
  private String p2p_address;

  private String owner_address;

  private String title;

  private BigInteger price;

  private String description;

  private BigInteger state;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(p2p_address);
    args.add(owner_address);
    args.add(title);
    args.add(price);
    args.add(description);
    args.add(state);
    return args;
  }
}
