package szu.zhl.test1.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;
/*接收注册的参数*/

@Data
public class RegisterData {

    private String id;
    private List<String> oldfaceinfo;
    private List<String> randomnumbers;
    private List<String> commitment;
    private List<String> oldcmt;

}