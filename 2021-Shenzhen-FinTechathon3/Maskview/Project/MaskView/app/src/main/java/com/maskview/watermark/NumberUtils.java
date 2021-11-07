package com.maskview.watermark;

public class NumberUtils {
    public static double getRound(double a,int b){
        double c= Math.pow(10,b);
        int d=(int) Math.floor(c*a);
        return d/c;
    }
    public static int getVariablePow(int a,int b){
        return a;
    }
}
