package com.find.util;

/**
 * @BelongsProject: network
 * @BelongsPackage: com.liao.networkdisk.utils
 * @Author: YunYang Liao
 * @CreateTime: 2023-05-18  23:59
 * @Description: TODO
 * @Version: 1.0
 */
public class StringTools {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        if (null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        }
        return false;
    }
}
