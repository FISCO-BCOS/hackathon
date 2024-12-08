package szu.zhl.test1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/*接收注册的参数*/
@Data
public class jsondatas {

    private String id;


    private List<BigInteger> oldfaceinfo;



    private List<BigInteger> randomnumbers;


    private List<BigInteger> commitment;


    private List<String> appnames;
}