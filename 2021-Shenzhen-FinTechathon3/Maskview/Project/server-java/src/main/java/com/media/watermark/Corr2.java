package com.media.watermark;

import static java.lang.Math.sqrt;
/**
 *Function to Compute the convolution of the input matrix with the convolution kernel vector
 * @Time 2020-04-20 14:49:01
 */
public class Corr2 {
    double[][] a;
    double[][] b;
    String c;
    double[][] d;

    public Corr2(double[][] a, double[][] b, String c){
        this.a=a;
        this.b=b;
        this.c=c;
    }
    /**
     *Function to Compute the convolution of the input matrix with the convolution kernel vector
     * r = sum(sum(a.*b))/sqrt(sum(sum(a.*a))*sum(sum(b.*b)));
     */
    public double Corr2r(){
        double[]c=new double[2];
        c=mean2(a,b,c);
        double mean2_a=c[0];
        double mean2_b=c[1];
        double sum_a=0,sum_b=0,sum_c=0;
        d=new double[a.length][a[0].length];
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[0].length;j++){
                a[i][j]=a[i][j]-mean2_a;
                b[i][j]=b[i][j]-mean2_b;
                sum_a+=a[i][j]*a[i][j];
                sum_b+=b[i][j]*b[i][j];
                sum_c+=a[i][j]*b[i][j];
            }
        }
        return sum_c/sqrt(sum_a*sum_b);
    }
    /**
     *Function to Calculate the average of the two input matrices
     */
    public double[] mean2(double[][] A,double[][]B,double[] C){
        double sum1=0;
        double sum2=0;
        int size=0;
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++){
                sum1+=A[i][j];
                sum2+=B[i][j];
                size++;
            }
        }
        C[0]=sum1/size;
        C[1]=sum2/size;
        return C;
    }

}
