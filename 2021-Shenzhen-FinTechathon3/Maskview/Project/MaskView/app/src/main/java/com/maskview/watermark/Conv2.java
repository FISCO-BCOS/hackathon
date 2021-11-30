package com.maskview.watermark;

public class Conv2 {
    double[][] a;
    double[] b;
    double[][] c;

    public Conv2(double[][] a, double[] b){
        this.a=a;
        this.b=b;
    }

    public double[][] conv2(){
        int aLength=this.a.length;
        int aWidth=this.a[0].length;
        int bLength=this.b.length;
        //int bWidth=this.b[0].length;

        c=new double[aLength-bLength+1][aWidth];
        for(int i = 0; i < c.length; ++i)
            for (int j = 0; j < c[0].length; ++j)
            {
                double tmp = 0;

                for (int m = 0; m < bLength;  ++m){
                    //for (int n = 0; n < bWidth; ++n)
                    tmp =tmp+b[bLength-1-m] * a[i+m][j ];//卷积公式
                }

                this.c[i][j] = tmp;
            }

        return this.c;
    }
}
