package szu.zhl.test1.entity;
 import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.util.List;

@Data

public class IdentityJson {
    private String id;
    private List<String> oldfaceinfo;
    private List<String> randomnumbers;
    private List<String> commitment;
    private List<String> oldcmt;

   private  List<String> appnames;
}

