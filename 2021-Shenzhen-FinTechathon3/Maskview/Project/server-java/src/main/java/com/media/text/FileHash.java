package com.media.text;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.File;
import java.io.IOException;

/**
 * 文件哈希(sha256)
 */

public class FileHash {

    public static void main(String[] args) {
        String filePath = "aa.txt";
        String hash = getSHA256(filePath, MessageDigestAlgorithms.SHA_256);
        System.out.println("java-server Hash : " + hash);
        System.out.println("Android Hash : " + "76b3a089dc5c8b22211991d4dfcbf276059150fbe603ceec6df9e5a3e26e8b93");
        if (hash.equals("76b3a089dc5c8b22211991d4dfcbf276059150fbe603ceec6df9e5a3e26e8b93")) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }
    
    public static String getSHA256(String filePath, String messageDigestAlgorithms) {
        String hex = null;
        try {
            File file = new File(filePath);
            hex = new DigestUtils(messageDigestAlgorithms).digestAsHex(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hex;
    }
}
