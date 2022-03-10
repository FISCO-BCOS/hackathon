package com.media.text;

import java.io.File;

/**
 * @author YZR
 * @date 2020/11/20 17:25
 * @note
 */

public class AESTest {

    public static void main(String[] args) throws Exception {
        //String content = "Hello world!";        // 原文内容
        String key = "11";                  // AES加密/解密用的原始密码

        // 加密数据, 返回密文
        //byte[] cipherBytes = AESUtils.encrypt(content.getBytes(), key.getBytes());
        //System.out.println(new String(cipherBytes));
        // 解密数据, 返回明文
        //byte[] plainBytes = AESUtils.decrypt(cipherBytes, key.getBytes());
        // 输出解密后的明文: "Hello world!"
        //System.out.println(new String(plainBytes));

        /*
         * AES 对文件的加密/解密
         */
        long start = System.currentTimeMillis();
        // 将 文件demo.jpg 加密后输出到 文件demo.jpg_cipher
        AESUtils.encryptFile(new File("test.jpg"), new File("demo.jpg"), key.getBytes());
        // 将 文件demo.jpg_cipher 解密后输出到 文件demo.jpg_plain
        //AESUtils.decryptFile(new File("11"), new File("demo1.jpg"), key.getBytes());
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        // 对比 原文件demo.jpg 和 解密得到的文件demo.jpg_plain 两者的 MD5 将会完全相同
    }

}
