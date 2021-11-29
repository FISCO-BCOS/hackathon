package com.media.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * @author YZR
 * @date 2020/11/6 16:49
 * pbkdf2+sha256的加密与解密
 */

public class Pbkdf2WithSha256 {

    /**
     * 根据password和salt生成密文
     */
    public static String getPBKDF2(String password, String salt) {
        //将16进制字符串形式的salt转换成byte数组
        byte[] bytes = DatatypeConverter.parseHexBinary(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), bytes,
                Constants.PBKDF2_ITERATIONS, Constants.HASH_SIZE * 4);
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(Constants.PBKDF2_ALGORITHM);
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
            //将byte数组转换为16进制的字符串
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 对输入的密码进行验证
     */
    public static boolean verify(String password, String salt, String key) {
        // 用相同的盐值对用户输入的密码进行加密
        String result = getPBKDF2(password, salt);
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return result.equals(key);
    }

    /**
     * 生成随机盐值salt
     */
    public static String getSalt() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[Constants.SALT_SIZE / 2];
            random.nextBytes(bytes);
            //将byte数组转换为16进制的字符串
            return DatatypeConverter.printHexBinary(bytes).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
