package com.media.watermark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.sql.SQLOutput;
import java.util.ArrayList;
import java.lang.String;

/**
 * Embed the watermark sequence into the image
 *
 * @Time 2020-04-20 14:49:01
 */
public class Robustwatermark {

    private static ArrayList<int[][][]> coverImg = new ArrayList<>();

    /**
     * Function to Transpose the matrix
     * <p>
     * //@param file     The address path of the cover image
     *
     * @param path     The storage path of the watermarked image
     * @param mark_str A string containing watermark information
     * @param pattern  zone bit
     */
    public static void robustWatermark(BufferedImage BI, String path, String mark_str, int pattern) throws IOException {

        long startTime = System.currentTimeMillis();

        int nLevels = 3;                                                                                                //The number of layers of the wavelet transform
        String biort = "legall";                                                                                        //filter
        String qShift = "qshift_c";                                                                                     //filter
        int row = 128;                                                                                                  //The number of coles in the embedded region matrix
        int cole = 128;                                                                                                 //The number of coles in the embedded region matrix
        int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};                                                              //Random number seed sequence
        int aa = 2;
        int bb = 2;
        int mc = BI.getWidth();
        int mr = BI.getHeight();
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
            System.out.println("image is too small");
            System.exit(1);
        }
        int[] markNum = (Str2num.str2Num(mark_str)).getq();                                                             //An int array containing watermark information
        int m = markNum.length;                                                                                         //Number of watermark bits
        int radius_num = 22 + 1;                                                                                        //The number of rings divided by each embedded region

        //Using random number sequence and size information to generate watermark image
        Watermarkimg watermarkimg = Watermarkimg.waterMarkImg(row, cole, seeds, radius_num, markNum, pattern);
        Complex[][] markImg = watermarkimg.getMarkimg();                                                                //Watermark image matrix
        double[][] Ug = watermarkimg.getUg();                                                                           //The frequency domain image sequence after the random image is divided into rings
        double minradius = watermarkimg.getRadiuses()[0];                                                               //Minimum ring radius

        //Embed the watermark image into the cover image
        Watermarkembed1 b = Watermarkembed1.waterMarkEmbed1(nLevels, biort, qShift, BI, row, cole, aa, bb, markImg, Ug, minradius, pattern, k);
        ArrayList<double[]> mark = b.getMark();
        int len = mark.get(0).length;
        if (mark.size() < aa * bb * 4) {
            //System.out.println("mark is not enough");
            System.exit(1);
        }
        double[] zeromark = new double[len * aa * bb * 4];                                                              //Extracted zero watermark information sequence
        File f = new File("test/key.txt");                                                                     //Create a new file object, or create one if it does not exist
        FileWriter fw;
        fw = new FileWriter(f);
        for (int i = 0; i < 4 * aa * bb; ++i) {
            for (int j = 0; j < len; ++j) {
                zeromark[i * len + j] = mark.get(i)[j];
            }
        }
        //Saves the zero-watermark sequence to a file
        for (int i = 0; i < zeromark.length; ++i) {
            System.out.print((int)zeromark[i]);
            int x = (int) zeromark[i];
            fw.write(Integer.toString(x));
            if ((i + 1) % len == 0) {
                fw.write("\n");
            }
        }
        System.out.println();
        fw.close();
        f.delete();
        ImageIO.write(b.getCoverImg(), "png", new File(path));
        }
    public static void main(String[] args) throws IOException {
        long time = 0;
        //int j=0;
        for (int i = 9; i < 10;++i) {
//            String file = "D:\\360MoveData\\Users\\王政辉\\Desktop\\tupian\\t1.png";
//            String path = "D:\\360MoveData\\Users\\王政辉\\Desktop\\MarkedImg\\t_1.png";
            String file = "C:\\Users\\xd01\\Desktop\\tupian\\test.png";
            String path = "C:\\Users\\xd01\\Desktop\\maskedPicture\\test5.jpg";
            try {
                File file1 = new File(file);
                BufferedImage BI = ImageIO.read(file1);
                String mark_str = "1543443552" + i;
                if (mark_str.length() < 11) {
                    System.out.println("the length of mark_str is too short");
                    System.exit(1);
                }else{
                    //Robustwatermark.robustWatermark(BI, path, mark_str, 1);
                    //Robustwatermark.robustWatermark(BI, path, mark_str, 2);
                    Watermarkextract1 a = new Watermarkextract1(path,"F:\\maskview\\watermarktest(1)\\watermark_Rob_Zero\\test\\rg0.txt");
                    System.out.println(a.robustWaterMark);
                    //System.out.println(a.zeroWaterMark);
                }
            } catch (IOException e) {
                System.out.println("Exception");
            }
        }
    }
//public static void main(String[] args) {
//    String file = "C:\\Users\\xd01\\Desktop\\tupian\\test.jpg";
//    //String path = "C:\\Users\\xd01\\Desktop\\tupian\\2222.png";
//    String path="C:\\Users\\xd01\\Desktop\\maskedPicture\\test3.jpg";
//    try {
//        File file1 = new File(file);
//        BufferedImage BI1 = ImageIO.read(file1);
//        int coverImg1[][]=new int[BI1.getHeight()][BI1.getWidth()];
//        for(int m=0;m<BI1.getHeight();++m){
//            for(int n=0;n<BI1.getWidth();++n){
//                int pixel = BI1.getRGB(n, m);
//                coverImg1[m][n]=(pixel & 0xff);
//            }
//        }
//        File file2 = new File(path);
//        BufferedImage BI2 = ImageIO.read(file2);
//        int coverImg2[][]=new int[BI1.getHeight()][BI1.getWidth()];
//        for(int m=0;m<BI2.getHeight();++m){
//            for(int n=0;n<BI2.getWidth();++n){
//                int pixel = BI2.getRGB(n, m);
//                coverImg2[m][n]=(pixel & 0xff);
//            }
//        }
//        double temp=0;
//        for (int i=0;i<BI1.getHeight();++i){
//            for (int j=0;j<BI1.getWidth();++j){
//                temp=temp+ Math.abs(coverImg2[i][j]-coverImg1[i][j]);
//            }
//        }
//        System.out.println(temp/BI1.getHeight()/BI1.getWidth());
//    } catch (IOException e) {
//        System.out.println("Exception");
//    }
//  }
}


