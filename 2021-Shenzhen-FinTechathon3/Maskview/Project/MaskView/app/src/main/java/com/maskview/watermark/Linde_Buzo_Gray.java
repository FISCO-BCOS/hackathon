package com.maskview.watermark;

import java.util.ArrayList;

public class Linde_Buzo_Gray {
    private double[][]img;
    public double[][]code_book;
    private int box_r,box_c,no_blocks;
    public double min_err;
    public double[][] scode_t;
    Linde_Buzo_Gray(double[][]img,int box_r,int box_c,int no_blocks,double[][]code_book) {//img(64x64double[]),int,int,int,double[](可能为NaN)
        this.img=img;
        this.box_c=box_c;
        this.box_r=box_r;
        this.code_book=code_book;
        this.no_blocks=no_blocks;
        min_err=0;
    }
    public double[][] LBG(){
        int max_iter= 500;
        int min_error=0;
        int s1=img.length;
        int s2=img[0].length;
        if(box_r==1){
            double[][]blc=img;
            int size=blc.length*blc[0].length;
            double[][]code_t=reshape(blc,size,1);
            int siz_code = box_r;
            scode_t=trans(reshape(code_t,siz_code,size));
            double[][]scode_r=code_book;
            double err=min_error+1;
            int iter=0;
            while (iter<max_iter&&err>min_error){
                iter=iter+1;
                double[][]d1=dist(scode_t,trans(scode_r));//注意
                double[][]d2=trans(d1);
                int[] m1=min(d2);
                double[][]Nscode_r=new double[no_blocks][siz_code];
                for(int i=0;i<no_blocks;i++){
                    double[] y=mean(equal(scode_t,m1,i));
                    for(int j=0;j<siz_code;j++){
                        Nscode_r[i][j]=y[j];
                    }

                }
                err=mean(diag(dist(scode_r,trans(Nscode_r))));
                scode_r =Nscode_r;
                min_err=err;
                code_book=scode_r;
            }
        }else{
            scode_t=block(img,box_r,box_c);
            int siz_code = box_r;
            double[][]scode_r=code_book;
            double err=min_error+1;
            int iter=0;
            while (iter<max_iter&&err>min_error){
                iter=iter+1;
                double[][]d1=dist(scode_t,trans(scode_r));//注意
                double[][]d2=trans(d1);
                int[] m1=min(d2);
                double[][]Nscode_r=new double[no_blocks][siz_code];
                for(int i=0;i<no_blocks;i++){
                    double[] y=mean(equal(scode_t,m1,i));
                    int c=y.length;
                    for(int j=0;j<siz_code;j++){
                        if(y.length==1){
                            Nscode_r[i][j]=Double.NaN;
                        }else{
                            Nscode_r[i][j]=y[j];
                        }
                    }
                }
                err=mean(diag(dist(scode_r,trans(Nscode_r))));
                scode_r =Nscode_r;
                min_err=err;
                code_book=scode_r;
            }
        }
        return code_book;
    }
    private ArrayList<double[]> equal(double[][]A, int[]d, int i){
        ArrayList<double[]>t=new ArrayList<>();
        for(int j=0;j<d.length;j++){
            if(d[j]==i){
                t.add(A[j]);
            }
        }
        return t;
    }
    private double[][]diag(double[][]a){
        double[][]temp=new double[a.length][1];
        for(int i=0;i<a.length;i++){
            temp[i][0]=a[i][i];
        }
        return temp;
    }
    private double mean(double[][]a){
        double max=0;
        for(int i=0;i<a.length;i++){
            max+=a[i][0];
        }
        return max/a.length;
    }
    private double[]mean(ArrayList<double[]>t){
        int size=t.size();
        if(size==0){
            double[]temps=new double[1];
            temps[0]=Double.NaN;
            return temps;
        }
        double[]temp=new double[t.get(0).length];
        for(int k=0;k<t.get(0).length;k++){
            double sum=0;
            for(int i=0;i<size;i++){
                sum+=t.get(i)[k];
            }
            sum/=size;
            temp[k]=sum;
        }
        return temp;
    }
    private int[] min(double[][]d){
        int[]temp=new int[d[0].length];
        for(int i=0;i<d[0].length;i++){
            double min=d[0][i];
            temp[i]=0;
            for(int j=0;j<d.length;j++){
                if(d[j][i]<min){
                    min=d[j][i];
                    temp[i]=j;
                }
            }
        }
        return temp;
    }
    private double[][] dist(double[][]a,double[][]b){
        double[][]temp=new double[a.length][b[0].length];
        for(int i=0;i<a.length;i++){
            for(int j=0;j<b[0].length;j++){
                double max=0;
                for(int k=0;k<b.length;k++){
                    max+=(a[i][k]-b[k][j])*(a[i][k]-b[k][j]);
                }
                temp[i][j]=Math.sqrt(max);
            }
        }
        return temp;
    }
    private double[][] trans(double[][] reshape) {
        int m=reshape.length;
        int n=reshape[0].length;
        double[][] temp=new double[n][m];
        for(int i=0;i<temp.length;i++){
            for(int j=0;j<temp[0].length;j++){
                temp[i][j]=reshape[j][i];
            }
        }
        return temp;
    }
    private double[][] trans(double[] reshape) {
        double[][]temp=new double[reshape.length][1];
        for(int i=0;i<temp.length;i++){
            temp[i][0]=reshape[i];
        }
        return temp;
    }
    private double[][] block(double[][] img, int box_r, int box_c) {
        int x=img.length*img[0].length/box_r;
        double temp[][]=new double[img.length*img[0].length/box_r][box_r];
        int flag=0;
        for(int i=0;i<x;i++){
            for(int j=0;j<box_r;j++){
                temp[i][j]=img[flag%img.length][flag/img.length];
                flag++;
            }
        }
        return temp;
    }
    public double[][] reshape(double[][] A,int m,int n){
        double[][] temp=new double[m][n];
        int flag=0;
        for(int i=0;i<A[0].length;i++){
            for(int j=0;j<A.length;j++){
                temp[flag%m][flag/m]=A[j][i];
                flag++;
            }
        }
        return temp;
    }
}
