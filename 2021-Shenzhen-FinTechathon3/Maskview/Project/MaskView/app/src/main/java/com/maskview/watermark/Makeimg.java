package com.maskview.watermark;

import java.util.ArrayList;

public class Makeimg {

    private Complex[][] markeImg;

    public Makeimg(Complex[][] a){
        this.markeImg=a;
    }

    public Complex[][] getMarkeImg(){
        return this.markeImg;
    }

    public static Makeimg makeImg(ArrayList<ArrayList<Complex[][]>> Rg, int[] markNum){
        int marknum=markNum.length;

        //Complex[][] watermarked_Img=new Complex[Rg.get(0).get(0).length][Rg.get(0).get(0)[0].length];
        Complex[][] watermarked_Img=Rg.get(markNum[0]).get(1);

        for (int i=2;i<=marknum;i++){
            int num=markNum[i-1];
            ArrayList<Complex[][]> Rga=Rg.get(num);
            Complex[][] Ro1=new Complex[Rga.get(i).length][Rga.get(i)[0].length];
            for (int k = 0; k < Rga.get(i).length; k++) {
                for (int l = 0; l < Rga.get(i)[0].length; l++) {
                    watermarked_Img[k][l]=watermarked_Img[k][l].plus(Rga.get(i)[k][l]);
                }
            }
        }
        return new Makeimg(watermarked_Img);
    }

    public static Makeimg makeImgsecond(ArrayList<ArrayList<Complex[][]>> Rg, int[] markNum){
        int marknum=markNum.length;

        //Complex[][] watermarked_Img=new Complex[Rg.get(0).get(0).length][Rg.get(0).get(0)[0].length];
        Complex[][] watermarked_Img=Rg.get(markNum[0]).get(1+11);

        for (int i=2;i<=marknum;i++){
            int num=markNum[i-1];
            ArrayList<Complex[][]> Rga=Rg.get(num);
            Complex[][] Ro1=new Complex[Rga.get(i).length][Rga.get(i)[0].length];
            for (int k = 0; k < Rga.get(i).length; k++) {
                for (int l = 0; l < Rga.get(i)[0].length; l++) {
                    watermarked_Img[k][l]=watermarked_Img[k][l].plus(Rga.get(i+11)[k][l]);
                }
            }
        }
        return new Makeimg(watermarked_Img);
    }

    public Complex conj(Complex a){
        double areal=a.getRe();
        double aimag=a.getIm();
        return new Complex(areal,0-aimag);
    }
}


