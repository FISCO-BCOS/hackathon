package com.maskview.watermark;

public class operation {
    public static double mean(double[][] I){
        int m=I.length;
        int n=I[0].length;
        if(m>0){
            int sum=0;
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    sum+=I[i][j];
                }
            }
            return sum/(m*n);
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }

    public static double max(double[][] I){
        int m=I.length;
        int n=I[0].length;
        if (m>0){
            double max=I[0][0];
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    if(max<I[i][j]){
                        max=I[i][j];
                    }
                }
            }
            return max;
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }

    public static double min(double[][] I){
        int m=I.length;
        int n=I[0].length;
        if (m>0){
            double min=I[0][0];
            for (int i=0;i<m;++i){
                for (int j=0;j<n;++j){
                    if(min>I[i][j]){
                        min=I[i][j];
                    }
                }
            }
            return min;
        }else{
            System.out.println("the matrix is null. at bitimgGen max");
            System.exit(0);
            return 0;
        }
    }
}
