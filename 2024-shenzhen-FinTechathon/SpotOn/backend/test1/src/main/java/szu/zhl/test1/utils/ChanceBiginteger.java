package szu.zhl.test1.utils;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class ChanceBiginteger {
     public List<String> ChanceBiginteger(List<String> data) {
        // 将字符串列表转换为 BigInteger 列表，并返回字符串形式
        List<String> processedAttribute = data.stream()
            .map(Double::parseDouble)
            .map(value -> BigInteger.valueOf(Math.round((value + 1) * 1000)))
            .map(BigInteger::toString) // 将 BigInteger 转换为字符串
            .collect(Collectors.toList());
        return processedAttribute;
    }

}
