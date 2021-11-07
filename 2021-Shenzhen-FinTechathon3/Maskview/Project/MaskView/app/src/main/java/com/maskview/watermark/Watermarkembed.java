package com.maskview.watermark;

public class Watermarkembed {

    private double[][] watermarkedImg;

    public Watermarkembed(double[][] img) {
        this.watermarkedImg = img;
    }

    public double[][] getWatermarkedImg() {
        return this.watermarkedImg;
    }

    public static Watermarkembed watermarkembed(Complex[][] Rc, Complex[][] markImg, double minradius, int pattern) {
        int rr = Rc.length;
        int rc = Rc[0].length;
        Complex[][] watermarked_Img = new Complex[rr][rc];
        ///////////////////////////////////////////////////////////////////////////////////////////////////////更改部分
        if (pattern == 1) {
            for (int i = 0; i < rr; ++i) {
                for (int j = 0; j < rc; ++j) {
                    if (markImg[i][j].getRe() == 0 && markImg[i][j].getIm() == 0) {
                        watermarked_Img[i][j] = Rc[i][j].multy(1.0);
                    } else {
                        watermarked_Img[i][j] = Rc[i][j].multy(0.8).plus(markImg[i][j].multy(0.2));
                    }
                }
            }

            FourierUtils.shift_to_center(watermarked_Img);
            double[][] img1 = FourierUtils.getInverseFft(watermarked_Img, rr, rc);
            return new Watermarkembed(img1);
        } else {
            for (int i = 0; i < rr; ++i) {
                for (int j = 0; j < rc; ++j) {
                    if (markImg[i][j].getRe() == 0 && markImg[i][j].getIm() == 0) {
                        watermarked_Img[i][j] = Rc[i][j].multy(1.0);
                    } else {
                        watermarked_Img[i][j] = Rc[i][j].multy(0.6).plus(markImg[i][j].multy(0.4));
                    }
                }
            }

            FourierUtils.shift_to_center(watermarked_Img);
            double[][] img1 = FourierUtils.getInverseFft(watermarked_Img, rr, rc);
            return new Watermarkembed(img1);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public Complex conj(Complex a) {
        double areal = a.getRe();
        double aimag = a.getIm();
        return new Complex(areal, 0 - aimag);
    }
}
