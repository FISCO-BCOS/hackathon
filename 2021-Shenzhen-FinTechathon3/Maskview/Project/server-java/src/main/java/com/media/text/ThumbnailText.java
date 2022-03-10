package com.media.text;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author YZR
 * @date 2020/11/20 17:52
 * @note 图片压缩
 */

public class ThumbnailText {

    public static void main(String[] args) throws IOException {
        /*long start = System.currentTimeMillis();
        //Thumbnails.of("demo.jpg").scale(1f).outputQuality(0.6f).toFile("kkk.jpg");
        Thumbnails.of("test.jpg").scale(1f).outputQuality(0.6f).toFile("kkk.jpg");
        long end = System.currentTimeMillis();
        System.out.println(end - start);*/

        long start = System.currentTimeMillis();
        BufferedImage thumbnail = Thumbnails.of("kkk.jpg")
                .scale(0.01f)
                .asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File("kkk1.jpg"));
        long end = System.currentTimeMillis();
        System.out.println(end - start);

    }
}
