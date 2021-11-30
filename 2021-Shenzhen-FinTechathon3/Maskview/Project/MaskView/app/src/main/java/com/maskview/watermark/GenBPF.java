package com.maskview.watermark;

public class GenBPF {
    private double[][] img;//function [BPF] = GenBPF(img,init_code_book,bpf_cbb,bpf_cbs)
    private double[][] init_code_book;
    private int bpf_cbb,bpf_cbs;
    public GenBPF(double[][]img,double[][]init_code_book,int bpf_cbb,int bpf_cbs){
        this.img=img;
        this.init_code_book=init_code_book;
        this.bpf_cbb=bpf_cbb;
        this.bpf_cbs=bpf_cbs;
    }
    public double[][] GenBPF1(){
        int m=bpf_cbs;
        int n=bpf_cbs;
        int M=img.length;
        int N=img[0].length;
        int sub_m=(int)Math.floor(M/m);
        int sub_n=(int)Math.floor(N/n);
        img=im2bwd(img);
        Linde_Buzo_Gray LBG=new Linde_Buzo_Gray(img,bpf_cbb,1,bpf_cbb,init_code_book);
        int[][]code_book=im2bwi(LBG.LBG());
        int mc=code_book.length;
        int nc=code_book[0].length;
        int sub_mc=(int)Math.floor(mc/m);
        int sub_nc=(int)Math.floor(nc/n);
        double[][]num=new double[sub_mc*sub_nc][1];
        for(int i=0;i<sub_m;i++){
            for(int j=0;j<sub_n;j++){
                int[][]tmp_img=new int[m][n];
                for(int k=0;k<m;k++){
                    for(int l=0;l<n;l++){
                        if(img[i*m+k][j*n+l]>0){
                            tmp_img[k][l]=1;
                        }else{
                            tmp_img[k][l]=0;
                        }
                    }
                }
                double minn = Double.POSITIVE_INFINITY;
                int minindex = -1;
                int count = -1;
                for(int k=0;k<sub_nc;k++){
                    for(int l=0;l<sub_mc;l++){
                        int[][]tmp=new int[m][n];
                        for(int q=0;q<m;q++){
                            for(int w=0;w<n;w++){
                                tmp[q][w]=code_book[l*m+q][k*n+w];
                            }
                        }
                        int summ=sum(tmp_img,tmp);
                        count = count+1;
                        if(minn>summ){
                            minn = summ;
                            minindex = count;
                        }
                    }
                }
                num[minindex][0] = num[minindex][0]+1;
            }
        }
        return num;
    }
    private int sum(int[][]A,int[][]B){
        int sum=0;
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++){
                if(A[i][j]!=B[i][j]){
                    sum++;
                }
            }
        }
        return sum;
    }
    private int[][] im2bwi(double[][]A){
        int[][]temp=new int[A.length][A[0].length];
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++){
                if(A[i][j]>0.5){
                    temp[i][j]=1;
                }else{
                    temp[i][j]=0;
                }
            }
        }
        return temp;
    }
    private double[][] im2bwd(double[][]A){
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++){
                if(A[i][j]>0.5){
                    A[i][j]=1;
                }else{
                    A[i][j]=0;
                }
            }
        }
        return A;
    }
}
