package szu.zhl.test1.utils;

import java.util.ArrayList;
import java.util.List;
/*将十进制的承诺转化为16进制长整形*/

public class StringConverter {

    public List<String> convertStrings(List<String> inputStrings) {
        List<String> result = new ArrayList<>();
        for (String str : inputStrings) {
            // 将字符串转换为整数
            long number = Long.parseLong(str);
            // 转换为十六进制字符串并格式化
            String hexString = String.format("0x%016x", number);
            // 添加到结果列表
            result.add(hexString);
        }
        return result;
    }
}