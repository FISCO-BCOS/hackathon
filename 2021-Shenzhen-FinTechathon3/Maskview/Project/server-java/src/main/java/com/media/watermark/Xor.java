package com.media.watermark;

/*Calculates the xor of two input sequences
 @Time 2020-04-20 14:49:01
 */
public class Xor {
    /**
     * @param a Initial input sequence a
     * @param b Initial input sequence a
     *@return  this
     */
    public static double[] xor(double[] a,double[] b){
        int aa=a.length;                                                                                                //the length of vector a
        int bb=b.length;                                                                                                //the length of vector b
        double[] c=new double[aa];                                                                                      //result vector
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
