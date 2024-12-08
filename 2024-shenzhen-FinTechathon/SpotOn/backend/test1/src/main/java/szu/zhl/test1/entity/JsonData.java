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
public class JsonData {
    @JsonProperty("id")
    private String id;

    @JsonProperty("m")
    @JsonDeserialize(using = HexListDeserializer.class)
    private List<BigInteger> oldfaceinfo;

    @JsonProperty("r")
    @JsonDeserialize(using = HexListDeserializer.class)
    private List<BigInteger> randomnumbers;

    @JsonProperty("cmt")
    @JsonDeserialize(using = HexListDeserializer.class)
    private List<BigInteger> commitment;

    @JsonProperty("appname")
    private List<String> appname;

    public static class HexListDeserializer extends JsonDeserializer<List<BigInteger>> {
        @Override
        public List<BigInteger> deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            List<BigInteger> result = new ArrayList<>();

            // 确保当前token是数组开始
            if (p.getCurrentToken() != JsonToken.START_ARRAY) {
                p.nextToken();  // 移动到数组开始
                if (p.getCurrentToken() != JsonToken.START_ARRAY) {
                    throw new IOException("Expected array of hex strings");
                }
            }

            // 读取数组元素
            while (p.nextToken() != JsonToken.END_ARRAY) {
                String hexValue = p.getText();
                if (hexValue.startsWith("0x")) {
                    hexValue = hexValue.substring(2);
                }
                result.add(new BigInteger(hexValue, 16));
            }

            return result;
        }
    }
}