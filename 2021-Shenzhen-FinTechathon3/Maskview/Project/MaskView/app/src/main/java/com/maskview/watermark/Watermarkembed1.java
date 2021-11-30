package com.maskview.watermark;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class Watermarkembed1 {

    private Bitmap bitmap;
    private ArrayList<double[]> mark;

    public Watermarkembed1(Bitmap bitmap, ArrayList<double[]> b) {
        this.bitmap = bitmap;
        this.mark = b;
    }

    public Bitmap getbitmap() {
        return bitmap;
    }

    public ArrayList<double[]> getMark() {
        return mark;
    }

    public static Watermarkembed1 waterMarkEmbed1(int nLevels, String biort, String qShift, Bitmap bitmap, int rr, int cc, int a, int b, int pattern, int k, String mark_str) throws IOException {
        int row=128;
        int cole=128;
        //int[] markNum = (Str2num.str2Num(mark_str)).getq();
        //int m = markNum.length;
        int radius_num = 22 + 1;                                   //此次更改radius_num写死   修改的函数Markimg,watermarkembed
        int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};


        int mc = bitmap.getWidth();
        int mr = bitmap.getHeight();

        ArrayList<double[]> mark = new ArrayList<>();

        int[][][] t1 = new int[a][b][2];
        int[][][] t2 = new int[a][b][2];
        int[][][] t3 = new int[a][b][2];
        int[][][] t4 = new int[a][b][2];

        for (int i = 0; i < a; ++i) {
            for (int j = 0; j < b; ++j) {
                //int k = 30;
                t1[i][j][0] = rr - k + (i) * rr * 2;
                t1[i][j][1] = cc - k + (j) * cc * 2;
                t2[i][j][0] = mr - rr * 2 * (i + 1) + k;
                t2[i][j][1] = rr - k + (j) * cc * 2;
                t3[i][j][0] = mr - rr * 2 * (i + 1) + k;
                t3[i][j][1] = mc - cc * 2 * (j + 1) + k;
                t4[i][j][0] = rr - k + (i) * rr * 2;
                t4[i][j][1] = mc - cc * 2 * (j + 1) + k;
            }
        }

        for (int i = 0; i < a; ++i) {
            for (int j = 0; j < b; ++j) {
                int[] x = new int[4];
                int[] y = new int[4];
                x[0] = t1[i][j][0];
                y[0] = t1[i][j][1];
                x[1] = t2[i][j][0];
                y[1] = t2[i][j][1];
                x[2] = t3[i][j][0];
                y[2] = t3[i][j][1];
                x[3] = t4[i][j][0];
                y[3] = t4[i][j][1];
                for (int l = 0; l < 4; ++l) {
                    int[][][] coverImg = new int[rr][cc][3];
                    for (int m = 0; m < rr; ++m) {
                        for (int n = 0; n < cc; ++n) {
                            int pixel = bitmap.getPixel(y[l] + n, x[l] + m);
                            coverImg[m][n][0] = (pixel & 0xff0000) >> 16;
                            coverImg[m][n][1] = (pixel & 0xff00) >> 8;
                            coverImg[m][n][2] = (pixel & 0xff);
                        }
                    }
                    String mark_str1=mark_str+Integer.toString(l);
                    int[] markNum = (Str2num.str2Num(mark_str)).getq();
                    Watermarkimg watermarkimg = Watermarkimg.waterMarkImg(rr, cc, seeds, radius_num, markNum, pattern);
                    Complex[][] markImg = watermarkimg.getMarkimg();
                    double[][] Ug = watermarkimg.getUg();
                    double minradies = watermarkimg.getRadiuses()[0];
                    coverImg = rotateImage2(coverImg, 360 - l * 90);
                    Treatfirst treatfirst = Treatfirst.treatfirst(coverImg, nLevels, biort, qShift, markImg, Ug, minradies, pattern);
                    mark.add(treatfirst.getZeromark());
                    coverImg = treatfirst.getCoverImg();
                    coverImg = rotateImage2(coverImg, l * 90);
                    for (int m = 0; m < rr; ++m) {
                        for (int n = 0; n < cc; ++n) {
                            //System.out.println(m[i][j][0]+" "+m[i][j][1]+" "+m[i][j][2]);
                            if (coverImg[m][n][0] > 255) {
                                coverImg[m][n][0] = 255;
                            } else if (coverImg[m][n][0] < 0) {
                                coverImg[m][n][0] = 0;
                            }
                            if (coverImg[m][n][1] > 255) {
                                coverImg[m][n][1] = 255;
                            } else if (coverImg[m][n][1] < 0) {
                                coverImg[m][n][1] = 0;
                            }
                            if (coverImg[m][n][2] > 255) {
                                coverImg[m][n][2] = 255;
                            } else if (coverImg[m][n][2] < 0) {
                                coverImg[m][n][2] = 0;
                            }
                            //Color color = Color.FromArgb(coverImg[m][n][0],coverImg[m][n][1],coverImg[m][n][2]);
                            int rgb = ((255 * 256 + coverImg[m][n][0]) * 256 + coverImg[m][n][1]) * 256 + coverImg[m][n][2];
                            bitmap.setPixel(y[l] + n, x[l] + m, rgb);

                        }
                    }
                }
            }
        }
        return new Watermarkembed1(bitmap, mark);
    }

    public static int[][][] rotateImage2(int[][][] a, int degree) {
        int mr = a.length;
        int mc = a[0].length;
        if (degree == 0 || degree == 360) {
            return a;
        } else if (degree == 90) {
            int[][][] c = new int[mc][mr][3];
            for (int i = 0; i < mr; ++i) {
                for (int j = 0; j < mc; ++j) {
                    c[mc - 1 - j][i][0] = a[i][j][0];
                    c[mc - 1 - j][i][1] = a[i][j][1];
                    c[mc - 1 - j][i][2] = a[i][j][2];
                }
            }
            return c;
        } else if (degree == 180) {
            int[][][] c = new int[mr][mc][3];
            for (int i = 0; i < mr; ++i) {
                for (int j = 0; j < mc; ++j) {
                    c[mr - 1 - i][mc - j - 1][0] = a[i][j][0];
                    c[mr - 1 - i][mc - j - 1][1] = a[i][j][1];
                    c[mr - 1 - i][mc - j - 1][2] = a[i][j][2];
                }
            }
            return c;
        } else if (degree == 270) {
            int[][][] c = new int[mc][mr][3];
            int[][][] d = new int[mc][mr][3];
            for (int i = 0; i < mr; ++i) {
                for (int j = 0; j < mc; ++j) {
                    c[mc - 1 - j][i][0] = a[i][j][0];
                    c[mc - 1 - j][i][1] = a[i][j][1];
                    c[mc - 1 - j][i][2] = a[i][j][2];
                }
            }
            for (int i = 0; i < mc; ++i) {
                for (int j = 0; j < mr; ++j) {
                    d[mr - 1 - i][mc - j - 1][0] = c[i][j][0];
                    d[mr - 1 - i][mc - j - 1][1] = c[i][j][1];
                    d[mr - 1 - i][mc - j - 1][2] = c[i][j][2];
                }
            }
            return d;
        } else {
            return a;
        }
    }
};




