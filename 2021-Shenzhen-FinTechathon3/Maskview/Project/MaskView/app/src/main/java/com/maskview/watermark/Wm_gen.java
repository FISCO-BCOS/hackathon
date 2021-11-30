package com.maskview.watermark;

public class Wm_gen {
    private double[] wm;

    public double[] getWm() {
        return wm;
    }

    public Wm_gen(double[] a){
        this.wm=a;
    }

    public static Wm_gen wm_gen(double[] base_wm,int num){
        int base_len=base_wm.length;
        int n=num/base_len;
        int m=num%base_len;
        double[] wm=new double[num];
        for (int i=0;i<n;++i){
            for (int j=0;j<base_len;++j){
                wm[i*base_len+j]=base_wm[j];
            }
        }
        for (int j=0;j<m;++j){
            wm[n*base_len+j]=base_wm[j];
        }
        return new Wm_gen(wm);
    }
}
