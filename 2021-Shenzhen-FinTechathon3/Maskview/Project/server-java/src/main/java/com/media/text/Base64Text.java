package com.media.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Base64Text {

    private static Logger logger = LoggerFactory.getLogger(Base64Text.class);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        decodeBase64ToImage(readTxt("aa.txt"), "", "");
        long end = System.currentTimeMillis();
        System.out.println("解码时间： " + (end - start));
        //readTxt("text.txt");
        //String qq = encodeImgageToBase64(new File("t16.jpg"));
        //System.out.println(qq);
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     */
    public static void decodeBase64ToImage(String base64, String path, String imgName) {
        imgName = "t17.jpg";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            FileOutputStream write = new FileOutputStream(new File(imgName));
            byte[] decoderBytes = decoder.decodeBuffer(base64);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读txt文件内容, 采用字节数组缓冲流读, 速度最快
     */
    public static String readTxt(String path) {
        long start = System.currentTimeMillis();
        //StringBuilder sBuilder = new StringBuilder();
        StringBuffer sBuffer = new StringBuffer();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                //System.out.println(new String(buf, 0, len));
                //sBuilder.append(new String(bytes, 0, len));
                sBuffer.append(new String(bytes, 0, len));
            }
            //System.out.println(sBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        logger.info(sBuffer.toString());
        System.out.println(end - start);
        return sBuffer.toString();
    }

    /**
     * 图片base64编码
     */
    public static String encodeImgageToBase64(File imageFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
    }
}

