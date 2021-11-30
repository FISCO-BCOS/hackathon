package com.media.watermark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Imread {
    public int[][][] img;
    public String file;
    public int height;
    public int width;

    public Imread(String file){
        this.file=file;
    }

    public int[][][] imread(){

        File file = new File(this.file);

        try{
            BufferedImage BI  = ImageIO.read(file);
            int width = BI.getWidth();
            int height = BI.getHeight();
            int minx = BI.getMinX();
            int miny = BI.getMinY();
            this.img=new int[height][width][3];
            for (int i = minx; i < width; i++) {
                for (int j = miny; j < height; j++) {
                    int pixel = BI.getRGB(i, j);                                                                        //Get the pixel value
                    this.img[j][i][0] = (pixel & 0xff0000) >> 16;
                    this.img[j][i][1]= (pixel & 0xff00) >> 8;
                    this.img[j][i][2] = (pixel & 0xff);
                }
            }
            this.height=height;
            this.width=width;
            BI.flush();
        }catch(Exception e){
            e.printStackTrace();
        }

        //BI.flush();

        return this.img;
    }

}

