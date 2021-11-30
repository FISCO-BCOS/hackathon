package com.maskview.watermark;

import java.util.ArrayList;

public class Watermarkimg {
    private Complex[][] markimg;
    private double[] radiuses;
    private double[][] Ug;

    public Watermarkimg(Complex[][] markimg1, double[] radiu, double[][] Ug1){
        this.markimg=markimg1;
        this.radiuses=radiu;
        this.Ug=Ug1;
    }

    public double[] getRadiuses() {
        return radiuses;
    }

    public Complex[][] getMarkimg() {
        return markimg;
    }

    public double[][] getUg() {
        return Ug;
    }

    public static Watermarkimg waterMarkImg(int rr, int cc, int[] seeds, int radiusNum, int[] markNum,int pattern){
        double[][] Ug=new double[rr][cc];
        ArrayList<double[][]> Ig=new ArrayList<>();
        ArrayList<Complex[][]> DFTg=new ArrayList<>();
        ArrayList<Complex[][]> Rc;
        ArrayList<ArrayList<Complex[][]> > Rg;
        //int rr = 128;
        //int cc = 128;
        //int[] seeds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int num = seeds.length;
        Ig=new ArrayList<>();
        Sequences randSequence = Sequences.sequences(seeds, rr, cc, num);

        int[][] text3=new int[rr][cc];//随机数种子生成彩色随机图
        for (int i = 0; i < num - 1; ++i) {
            double[][] text2=new double[rr][cc];
            for (int j = 0; j < rr; ++j) {
                for (int k = 0; k < cc; ++k) {
                    text2[j][k]=(double)randSequence.getRandSequences().get(i)[j][k];
                }
            }
            Ig.add(text2);
        }

        for (int i=0;i<rr;++i){
            for (int j=0;j<cc;++j){
                Ug[i][j]=(double)randSequence.getRandSequences().get(num-1)[i][j];
            }
        }

        for(int i=0;i<num-1;i++){
        double[][] Yc=new double[rr][cc];
        Yc=Ig.get(i);
        DFTg.add(FourierUtils.shift_to_center(FourierUtils.getFft(Yc)));
        }

        Radius radius= Radius.radius(radiusNum,rr,cc);
        double[] radiuses=radius.getR();
        //////////////////////////////////////////////////////////////////
        for (int i=radiuses.length-1;i>0;--i){
            if(radiuses[i]-radiuses[i-1]<2){
                radiuses[i-1]=radiuses[i]-2;
            }
        }
        //////////////////////////////////////////////////////////////////
        Rc= Coefficient.coefficient(radiuses,DFTg.get(0)).getRc();
        Rg= Coefficients.coefficients(Rc,DFTg,radiusNum,num-1).getRg();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////本次更改部分
        if (pattern==1){
            Makeimg a= Makeimg.makeImg(Rg,markNum);
            return new Watermarkimg(a.getMarkeImg(),radiuses,Ug);
        }else{
            Makeimg a= Makeimg.makeImgsecond(Rg,markNum);
            return new Watermarkimg(a.getMarkeImg(),radiuses,Ug);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

}
