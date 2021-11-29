package com.media.watermark;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The robust watermark and zero watermark information in the input matrix are extracted
 *
 * @Time 2020-04-20 14:49:01
 */
public class Extract extends Thread {
    private String biort = "legall";                                                                                      //filter
    private String qshift = "qshift_c";                                                                                   //filter
    private int rr = 128, cc = 128;                                                                                          //Size of template matrix
    private int num = 10;                                                                                                 //Number of random templates
    private int stepnum = 90;                                                                                             //The Angle of a single rotation
    private int nlevels = 3;                                                                                              //The number of wavelet transforms
    private int radius_num;                                                                                             //Number of rings
    private int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};                                                                       //Random number seed sequence
    private ArrayList<Complex[][][]> Uh;                                                                                //High frequency wavelet subband coefficient sequence
    private double[][] Ul;                                                                                              //Low-frequency wavelet coefficient matrix
    private int[] codenum;                                                                                              //Robust watermark sequence
    private double[][] G,B,R, Ip1, Ip2;                                                                                    //Initial image matrix
    private int teip;                                                                                                   //
    private long startTime;                                                                                             //start time
    private double[] relative;                                                                                          //relative coefficient matrix
    private int tip;                                                                                                    //
    private int block_size = 256;                                                                                         //The size of the pixel block extracted on each corner
    private BufferedImage BI;                                                                                           //Import the image
    private ArrayList<ArrayList<double[][]>> Rg0;                                                                       //Template image sequence

    public Extract(BufferedImage BI, int tip, double[][] Ip1, double[][] Ip2, int radius_num, int teip, long startTime, ArrayList<ArrayList<double[][]>> Rg0) {
        this.Ip1 = Ip1;
        this.Ip2 = Ip2;
        this.radius_num = radius_num;
        this.teip = teip;
        this.startTime = startTime;
        this.Rg0 = Rg0;
        this.tip = tip;
        this.BI = BI;
        G = new double[block_size][block_size];
        R = new double[block_size][block_size];
        B = new double[block_size][block_size];
        //Extract the pixel blocks at each corner
        //upper left
        if (teip == 0) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    int pixel = BI.getRGB(k, j);
                    R[j][k] = (pixel & 0xff0000) >> 16;
                    G[j][k] = (pixel & 0xff00) >> 8;
                    B[j][k] = (pixel & 0xff);
                }
            }
        }//lower left
        else if (teip == 1) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    int pixel = BI.getRGB(k, BI.getHeight() - 256 + j);
                    R[k][256 - 1 - j] = (pixel & 0xff0000) >> 16;
                    G[k][256 - 1 - j] = (pixel & 0xff00) >> 8;
                    B[k][256 - 1 - j] = (pixel & 0xff);
                }
            }
        }//lower right
        else if (teip == 2) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    int pixel = BI.getRGB(BI.getWidth() - 256 + k, BI.getHeight() - 256 + j);
                    R[256 - j - 1][256 - 1 - k] = (pixel & 0xff0000) >> 16;
                    G[256 - j - 1][256 - 1 - k] = (pixel & 0xff00) >> 8;
                    B[256 - j - 1][256 - 1 - k] = (pixel & 0xff) ;
                }
            }
        }//upper right
        else {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    int pixel = BI.getRGB(BI.getWidth() - 256 + k, j);
                    R[256 - 1 - k][j] = (pixel & 0xff0000) >> 16;
                    G[256 - 1 - k][j] = (pixel & 0xff00) >> 8;
                    B[256 - 1 - k][j] = (pixel & 0xff) ;
                }
            }
        }
    }

    @Override
    public void run() {
        int tp = extract();
        if (tp == 1){
            String s2 ="";
            for (int i = 1; i < radius_num; i++) {
                s2 = s2 + Integer.toString(codenum[i]);
            }
            s2 = s2 + "\n";
            Watermarkextract1.robustWaterMarkList.add(s2);
        }
        else {                                                                                                          //If the watermark sequence is not extracted, another set of pixel blocks will be extracted
            if (tip == 1) {
                if (teip == 0) {
                    for (int j = 0; j < 256; j++) {
                        for (int k = 0; k < 256; k++) {
                            int pixel = BI.getRGB(k + 128, j + 128);
                            R[j][k] = (pixel & 0xff0000) >> 16;
                            G[j][k] = (pixel & 0xff00) >> 8;
                            B[j][k] = (pixel & 0xff);
                        }
                    }
                } else if (teip == 1) {
                    for (int j = 0; j < 256; j++) {
                        for (int k = 0; k < 256; k++) {
                            int pixel = BI.getRGB(k + 128, BI.getHeight() - 256 + j - 128);
                            R[k][256 - 1 - j] = (pixel & 0xff0000) >> 16;
                            G[k][256 - 1 - j] = (pixel & 0xff00) >> 8;
                            B[k][256 - 1 - j] = (pixel & 0xff);
                        }
                    }
                } else if (teip == 2) {
                    for (int j = 0; j < 256; j++) {
                        for (int k = 0; k < 256; k++) {
                            int pixel = BI.getRGB(BI.getWidth() - 256 + k - 128, BI.getHeight() - 256 + j - 128);
                            R[256 - j - 1][256 - 1 - k] = (pixel & 0xff) >> 16;
                            G[256 - j - 1][256 - 1 - k] = (pixel & 0xff00) >> 8;
                            B[256 - j - 1][256 - 1 - k] = (pixel & 0xff);
                        }
                    }
                } else {
                    for (int j = 0; j < 256; j++) {
                        for (int k = 0; k < 256; k++) {
                            int pixel = BI.getRGB(BI.getWidth() - 256 + k - 128, j + 128);
                            R[256 - 1 - k][j] = (pixel & 0xff0000) >> 16;
                            G[256 - 1 - k][j] = (pixel & 0xff00) >> 8;
                            B[256 - 1 - k][j] = (pixel & 0xff);
                        }
                    }
                }
                int tp1 = extract();                                                                                    //Extract the watermark in the new pixel block
                if (tp1 == 1) {
                    String s2 = "";
                    for (int i = 1; i < radius_num; i++) {
                        s2 = s2 + Integer.toString(codenum[i]);
                    }
                    s2 = s2 + "\n";
                    Watermarkextract1.robustWaterMarkList.add(s2);
                }
            }
        }
    }

    public int extract() {
        //The G channel of the Input image is transformed by wavelet transform
        Dtwavexfm2 dtwavexfm2 = new Dtwavexfm2(G, nlevels, biort, qshift);
        dtwavexfm2.dtwavexfm2();
        Ul = dtwavexfm2.getYl();                                                                                          //Low-frequency wavelet coefficient matrix
        Uh = dtwavexfm2.getYh();                                                                                          //High-frequency wavelet coefficient matrix
        //Low frequency subband coefficient set to zero
        for (int i = 0; i < Ul.length; i++) {
            for (int j = 0; j < Ul[0].length; j++) {
                Ul[i][j] = 0;
            }
        }
        try {
            //The subband coefficient matrix of the highest wavelet
            // is retained, and all others are set to zero
            int x, y, z;                                                                                                  //The three dimensional dimensions of the subband coefficient matrix
            x = Uh.get(0).length;
            y = Uh.get(0)[0].length;
            z = Uh.get(0)[0][0].length;
            Uh.remove(0);
            //In the first layer, the high frequency subband coefficient is set to zero
            Complex[][][] temp1 = new Complex[x][y][z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        temp1[i][j][k] = new Complex();
                    }
                }
            }
            Uh.add(temp1);
            x = Uh.get(0).length;
            y = Uh.get(0)[0].length;
            z = Uh.get(0)[0][0].length;
            //In the second layer, the high frequency subband coefficient is set to zero
            Complex[][][] temp2 = new Complex[x][y][z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        temp2[i][j][k] = new Complex();
                    }
                }
            }
            Uh.add(temp2);
            Uh.remove(0);
            Complex[][][] temp = Uh.get(0);
            Uh.remove(0);
            Uh.add(temp);                                                                                               //High-frequency wavelet coefficient matrix
        } catch (IndexOutOfBoundsException e) {
            System.out.println("读取越界");
        }
        //The pixel matrix containing only the highest level
        // information is obtained by inverse wavelet transform
        Dtwaveifm2 dtwaveifm2 = new Dtwaveifm2(Ul, Uh, biort, qshift);
        double[][] Ud = dtwaveifm2.dtWaveifm2();
        //Geometric trim attack correction
        Cutting cutti = new Cutting(Ud, Ip2, biort, qshift, nlevels);
        relative = cutti.cut();                                                                                         //relative coefficient
        if (relative[0] > 0.2) {
            //Extract watermark sequence
            Decodes de = new Decodes((int) relative[1], (int) relative[2], R, seeds, Ip1, num, radius_num, Rg0);
            codenum = de.decode();
            double[][] Zero=new double[rr][cc];                                                                         //submatrix
            for(int i=(int)relative[1];i<relative[1]+rr;i++){
                for(int j=(int)relative[2];j<relative[2]+cc;j++){
                    Zero[i-(int)relative[1]][j-(int)relative[2]]=B[i][j];
                }
            }
            Watermarkembedzero watermarkembedzero=Watermarkembedzero.watermarkembedzero(Zero);
            double[] a=watermarkembedzero.getBinaryFeature();
            String zeroCode="";
            for (int i = 0; i < a.length; i++) {
                zeroCode = zeroCode + Integer.toString((int)a[i]);
            }
            Watermarkextract1.zeroWaterMarkList.add(zeroCode);
        } else {
            return 0;
        }
        return 1;
    }
}
