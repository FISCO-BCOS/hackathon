package com.maskview.watermark;

import java.util.ArrayList;

public class Treatfirst {

    private int[][][] coverImg;
    private double[] zeromark;

    public Treatfirst(int[][][] coverImg,double[] b){
        this.coverImg=coverImg;
        this.zeromark=b;
    }

    public int[][][] getCoverImg() {
        return coverImg;
    }

    public double[] getZeromark() {
        return zeromark;
    }

    public static Treatfirst treatfirst(int[][][] coverImg, int nLevels, String biort, String qShift, Complex[][] markImg, double[][] Ug, double minradius, int pattern){
        int mr=markImg.length;
        int mc=markImg[0].length;
        int[][] Yc=new int[mr][mc];
        double[][] U=new double[mr][mc];
        double[][] V=new double[mr][mc];

        double[][]Y=new double[mr][mc];
        for (int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                Yc[i][j]=coverImg[i][j][0];
                Y[i][j]=(double)coverImg[i][j][0];
                U[i][j]=coverImg[i][j][1];
                V[i][j]=coverImg[i][j][2];
            }
        }

        Watermarkembedzero watermarkembedzero=Watermarkembedzero.watermarkembedzero(V);
        //Watermarkextractzero watermarkextractzero=Watermarkextractzero.watermarkextractzero(V);
        //double[] ccc=Xor.xor(watermarkembedzero.getBinaryFeature(),watermarkextractzero.getBinaryFeature());

        Complex[][] DFTc= FourierUtils.getFft(Y);                                         //快速傅里叶变换
        DFTc= FourierUtils.shift_to_center(DFTc);
        //DFTc=FourierUtils.shift_to_center(DFTc);
        //Y=FourierUtils.getInverseFft(DFTc,mr,mc);


        Dtwavexfm2 dtwavexfm22=new Dtwavexfm2(U,nLevels,biort,qShift);
        dtwavexfm22.dtwavexfm2();
        double[][] Ul=dtwavexfm22.getYl();
        ArrayList<Complex[][][] > Uh=dtwavexfm22.getYh();

        Dtwavexfm2 dtwavexfm21=new Dtwavexfm2(Ug,nLevels,biort,qShift);
        dtwavexfm21.dtwavexfm2();
        double[][] Gl=dtwavexfm21.getYl();
        ArrayList<Complex[][][] > Gh=dtwavexfm21.getYh();

        double[][] Pl=Ul;
        ArrayList<Complex[][][] > Ph=new ArrayList<>();
        for (int i=0;i<nLevels-1;i++){
            Ph.add(multyMatrix(0.0,1,Gh.get(i),Uh.get(i)));
        }
        Ph.add(multyMatrix(0.5,0.5,Gh.get(nLevels-1),Uh.get(nLevels-1)));
        Dtwaveifm2 dtwaveifm21=new Dtwaveifm2(Pl,Ph,biort,qShift);
        double[][] z=dtwaveifm21.dtWaveifm2();

        Watermarkembed watermarkembed=Watermarkembed.watermarkembed(DFTc,markImg,minradius,pattern);
        double[][] watermarked_img=watermarkembed.getWatermarkedImg();

        int r1=watermarked_img.length ;
        int c1=watermarked_img[0].length;
        int r2=z.length;
        int c2=z[0].length;
        int rr=min(r1,mr);
        int cc=min(c1,mc);
        for (int i=0;i<rr;i++){
            for (int j=0;j<cc;j++){
                coverImg[i][j][0]=(int) Math.floor(watermarked_img[i][j]);
                coverImg[i][j][1]=(int) Math.floor(z[i][j]);
                //coverImg[i][j][2]=(int) Math.floor(coverImg[i][j][2]);
            }
        }

        return new Treatfirst(coverImg,watermarkembedzero.getBinaryFeature());
    }

    public static Complex[][][] multyMatrix(double a, double b, Complex[][][] aa, Complex[][][] bb){
        int m1=aa.length;
        int m2=aa[0].length;
        int m3=aa[0][0].length;
        Complex[][][] ccc=new Complex[m1][m2][m3];
        for(int i=0;i<m1;i++){
            for (int j=0;j<m2;j++){
                for (int k=0;k<m3;k++){
                    ccc[i][j][k]=aa[i][j][k].multy(a).plus(bb[i][j][k].multy(b));
                }
            }
        }
        return ccc;
    }

    public static int min(int a,int b){
        if(a<b){
            return a;
        }
        else{
            return b;
        }
    }
}
