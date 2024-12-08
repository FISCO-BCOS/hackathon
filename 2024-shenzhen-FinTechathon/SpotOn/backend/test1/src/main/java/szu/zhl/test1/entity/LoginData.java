package szu.zhl.test1.entity;



import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class LoginData {
    private List<BigInteger> newfaceinfo;        // 新人脸信息
    private String appname;


}