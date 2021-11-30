package com.maskview.watermark;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;

public class Robustwatermark {

    private static ArrayList<int[][][]> coverImg = new ArrayList<>();
    private Bitmap bitmap;
    private String key = "";


    public Bitmap getbitmap() {
        return bitmap;
    }

    public Bitmap robustWatermark(Bitmap bitmap, String mark_str, int pattern) throws IOException {
        //public static void main(String[] args)throws IOException {
        long startTime = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        key = "";
        int nLevels = 3;
        String biort = "legall";
        String qShift = "qshift_c";
        int rr = 128;
        int cc = 128;
        int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int aa = 2;
        int bb = 2;
        int mc = width;
        int mr = height;
        int k = 0;
        if (mr < 1000 && mr >= 512 || mc < 1000 && mc >= 512) {
            aa = 1;
            bb = 1;
            k = 30;
        }
        if (mr < 512 && mr >= 260 || mc < 512 && mc >= 260) {
            aa = 1;
            bb = 1;
            k = 127;
        }
        if (mr < 260 || mc < 260) {
            //System.out.println("image is too small");
            return null;
        }

        /*int[] markNum = (Str2num.str2Num(mark_str)).getq();
        int m = markNum.length;
        int radius_num = 22 + 1;                                   //此次更改radius_num写死   修改的函数Markimg,watermarkembed

        Watermarkimg a = Watermarkimg.waterMarkImg(rr, cc, seeds, radius_num, markNum, pattern);
        Complex[][] markImg = a.getMarkimg();
        double[][] Ug = a.getUg();
        double minradius = a.getRadiuses()[0];*/

        Watermarkembed1 b = Watermarkembed1.waterMarkEmbed1(nLevels, biort, qShift, bitmap, rr, cc, aa, bb,  pattern, k, mark_str);
        ArrayList<double[]> mark = b.getMark();
        int len = mark.get(0).length;
        double[] zeromark = new double[len * aa * bb * 4];
        /*File f= new File("key.txt");//新建一个文件对象，如果不存在则创建一个该文件
        FileWriter fw;
        fw=new FileWriter(f);*/
        for (int i = 0; i < 4 * aa * bb; ++i) {
            for (int j = 0; j < len; ++j) {
                zeromark[i * len + j] = mark.get(i)[j];
            }
        }

        for (int i = 0; i < zeromark.length; ++i) {
            //System.out.println(zeromark[i]);
            int x = (int) zeromark[i];
            //fw.write(Integer.toString(x));
            //s = new StringBuffer();
            key = key + x;
            //s.append(x);
            if ((i + 1) % len == 0) {
                //fw.write("\n");
                key = key + "\n";
            }
        }
        //fw.close();
        //Log.e("============", "序列:" + key );
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        return b.getbitmap();
        //ImageIO.write(b.getCoverImg(),"jpg",new File(path));
        //long endtime= System.currentTimeMillis();
        //System.out.println("程序运行时间：" + ( endtime- startTime) + "ms");
    }

    public String getKey() {
        return key;
    }

}
