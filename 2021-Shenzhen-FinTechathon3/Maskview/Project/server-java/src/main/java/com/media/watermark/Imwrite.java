package com.media.watermark;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Imwrite {

    public static BufferedImage RGBtoImage (int[][][]m){
        int w = m.length;
        int h = m[0].length;
        BufferedImage output_img = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
        for (int j = 0; j < h; j++){
            for (int i = 0; i < w; i++){
                if(m[i][j][0]>255){
                    m[i][j][0]=255;
                }else if(m[i][j][0]<0){
                    m[i][j][0]=0;
                }
                if(m[i][j][1]>255){
                    m[i][j][1]=255;
                }else if(m[i][j][1]<0){
                    m[i][j][1]=0;
                }
                if(m[i][j][2]>255){
                    m[i][j][2]=255;
                }else if(m[i][j][2]<0){
                    m[i][j][2]=0;
                }
                //System.out.println(m[i][j][0]+" "+m[i][j][1]+" "+m[i][j][2]);
                Color color = new Color(m[i][j][0],m[i][j][1],m[i][j][2]);
                output_img.setRGB(j,i, color.getRGB());
            }
        }
        return output_img;
    }

}
