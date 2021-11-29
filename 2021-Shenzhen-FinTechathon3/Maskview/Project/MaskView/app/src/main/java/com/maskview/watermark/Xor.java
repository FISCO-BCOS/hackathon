package com.maskview.watermark;

public class Xor {
    public static double[] xor(double[] a,double[] b){
        int aa=a.length;
        int bb=b.length;
        double[] c=new double[aa];
        if(aa==bb){
            for (int i=0;i<aa;++i){
                if(a[i]==b[i]){
                    c[i]=1;
                }else{
                    c[i]=0;
                }
            }
        }
        return c;
    }
}
