package szu.blockchain.check.utils;

import java.math.BigInteger;

public class ToHex {
    public  String intToHex(BigInteger value, int length) {
        String hex = value.toString(16); // 将大整数转换为十六进制字符串
        int padding = length - hex.length(); // 计算需要填充的零的数量
        if (padding > 0) {
            hex = "0x" + "0".repeat(padding) + hex; // 如果需要，填充零
        } else {
            hex = "0x" + hex; // 否则直接加上前缀
        }
        return hex; // 返回十六进制字符串
    }
}
