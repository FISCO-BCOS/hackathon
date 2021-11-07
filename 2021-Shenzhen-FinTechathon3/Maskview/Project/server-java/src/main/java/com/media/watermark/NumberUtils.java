package com.media.watermark;

/**
 *Function to Process the input value
 * @Time 2020-04-20 14:49:01
 */
public class NumberUtils {

    /**
     *Function to Process the input value
     * @param a  Pending number
     * @param b  the number of significant digits reserved
     * @return  The number obtained by retaining the b-digit significant digit of a
     */
    public static double getRound(double a,int b){
        double c= Math.pow(10,b);
        int d=(int) Math.floor(c*a);
        return d/c;
    }

    public static int getVariablePow(int a,int b){
        return a;
    }
}
