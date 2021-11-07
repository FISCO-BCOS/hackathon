package com.maskview.watermark;

public class diffuseGen {
    private double sum;
    private double[][][] sumw;

    public  diffuseGen(double sum1,double[][][] sumw1){
        this.sum=sum1;
        this.sumw=sumw1;
    }
    public static diffuseGen diffusegen(int x,int y,int xm,int ym,int[][] prority,double[] avg,double[] maxv,double[] minv,double[][][] v,double sum,double[][][] sumw,double w,double[][][] e,double[][][] t){
        if(prority[xm][ym]>prority[x][y]){
            sum+=w;
        }
        for (int i=0;i<v[0][0].length;++i){
            if (v[xm][ym][i]>avg[i]){
                t[xm][ym][i]=maxv[i];
            }else{
                t[xm][ym][i]=minv[i];
            }
        }

        for (int i=0;i<e[0][0].length;++i){
            e[xm][ym][i]=v[xm][ym][i]-t[xm][ym][i];
            sumw[xm][ym][i]=sumw[xm][ym][i]+(e[xm][ym][i]*w);
        }
        return new diffuseGen(sum,sumw);
    }

    public double getSum(){
        return this.sum;
    }

    public double[][][] getSumw() {
        return sumw;
    }
}
