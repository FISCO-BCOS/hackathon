package com.media.utils;

/**
 * @author YZR
 * @date 2020/11/9 16:41
 */

public class SmsUtils {

    public static int getCode() {
        return (int) (Math.random() * 8999 + 1000);
    }

}
