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
public class CharityTableCreateInputBO {
  private String _table_name;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_table_name);
    return args;
  }
}
