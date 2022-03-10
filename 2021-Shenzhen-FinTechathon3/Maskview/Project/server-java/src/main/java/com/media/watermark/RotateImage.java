package com.media.watermark;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RotateImage {

    public static BufferedImage rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

       BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
       Graphics2D g2 = res.createGraphics();
      // transform
      g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
   }
   public static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
     if (angel >= 90) {
               if (angel / 90 % 2 == 1) {
                        int temp = src.height;
                        src.height = src.width;
                        src.width = temp;
                    }
                angel = angel % 90;
            }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

       int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
       int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
       int des_width = src.width + len_dalta_width * 2;
       int des_height = src.height + len_dalta_height * 2;
       return new Rectangle(new Dimension(des_width, des_height));
    }
}

