package com.media.text;

import com.media.utils.Pbkdf2WithSha256;

/**
 * pbkdf2加密解密
 */

public class Pbkdf2 {

    public static void main(String[] args) {
        String salt = "b94e71d68b3bd062f5127d3a43b1d268b2b487d8f9e00fe3298b9d86bb798b78";
        String key = Pbkdf2WithSha256.getPBKDF2("123321", salt);
        System.out.println(key);
        if (key.equals("561c20ac03c4dc4d09500197334b9118f5e4e055962089e254b3268307c368a1")) {
            System.out.println(true);
        }
        boolean result = Pbkdf2WithSha256.verify("123321", salt, "561c20ac03c4dc4d09500197334b9118f5e4e055962089e254b3268307c368a1");
        System.out.println(result);
    }
}
