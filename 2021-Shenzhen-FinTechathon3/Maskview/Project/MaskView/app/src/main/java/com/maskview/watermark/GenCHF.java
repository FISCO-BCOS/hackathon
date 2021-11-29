package com.maskview.watermark;

import java.util.ArrayList;

public class GenCHF {
    private double[][]img,max_quan,min_quan,init_code_book;
    private int chf_len;
    public GenCHF(double[][]img,double[][]max_quan,double[][]min_quan,double[][]init_code_book,int chf_len){
        this.chf_len=chf_len;
        this.img=img;
        this.max_quan=max_quan;
        this.min_quan=min_quan;
        this.init_code_book=init_code_book;
    }
    public ArrayList<int[][]> GenCHF1(){
        ArrayList<int[][]>t=new ArrayList<>();
        int c=1;
        Linde_Buzo_Gray LBG=new Linde_Buzo_Gray(img,c,c,chf_len,init_code_book);
        double[][]maxSingle=new double[max_quan.length*max_quan[0].length][1];
        for(int i=0;i<max_quan[0].length;i++){
            for(int j=0;j<max_quan.length;j++){
                maxSingle[i*max_quan.length+j][0]=max_quan[j][i];
            }
        }
        int m=max_quan.length*max_quan[0].length;
        int[][] num_max=new int[chf_len][1];
        double[][] minSingle=new double[min_quan.length*min_quan[0].length][1];
        for(int i=0;i<min_quan[0].length;i++){
            for(int j=0;j<min_quan.length;j++){
                minSingle[i*min_quan.length+j][0]=min_quan[j][i];
            }
        }
        int[][] num_min=new int[chf_len][1];
        double[][]t1=LBG.LBG();
        for(int i=0;i<m;i++){
            double[][]max_matrix=new double[t1.length][t1[0].length];
            double[][]min_matrix=new double[t1.length][t1[0].length];
            for(int q=0;q<max_matrix.length;q++){
                for(int w=0;w<max_matrix[0].length;w++){
                    max_matrix[q][w]=t1[q][w]-maxSingle[i][0];
                }
            }
            int max_index = find(max_matrix);
            if(max_index!=-1){
                num_max[max_index][0]++;
            }
            for(int q=0;q<min_matrix.length;q++){
                for(int w=0;w<min_matrix[0].length;w++){
                    min_matrix[q][w]=t1[q][w]-minSingle[i][0];
                }
            }
            int min_index = find(min_matrix);
            if(min_index!=-1){
                num_min[min_index][0]++;
            }
        }
        t.add(num_max);
        t.add(num_min);
        return t;
    }
    private int find(double[][]A){
        int index=min(A);
        if(A[index][0]>=0){
            return index;
        }else{
            return -1;
        }
    }
    private int min(double[][]A){
        int index=0;
        double minx=A[0][0];
        for(int i=0;i<A.length;i++){
            if(Double.isNaN(A[i][0])){
                continue;
            }
            index=i;
            minx=Math.abs(A[i][0]);
            break;
        }
        for(int i=0;i<A.length;i++){
            if(Double.isNaN(A[i][0])){
                continue;
            }
            if(Math.abs(A[i][0])<minx){
                index=i;
                minx=Math.abs(A[i][0]);
            }
        }
        return index;
    }
}
