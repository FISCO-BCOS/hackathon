package com.media.watermark;

import java.util.ArrayList;
/**
 * Based on the initial input pixel matrix, the maximum/minimum quantifier sequence generates the CHF sequence
 * @Time 2020-04-20 14:49:01
 */
public class GenCHF {
    private double[][] img;                                                                                             //Bitmap image matrix
    private double[][] max_quan;                                                                                        //Maximum quantizer sequence
    private double[][] min_quan;                                                                                        //Minimum quantizer sequence
    private double[][] init_code_book;                                                                                  //Template matrix
    private int chf_len;                                                                                                //sequence length
    /**Based on the initial input pixel matrix, the maximum/minimum quantifier sequence generates the CHF sequence
     * @param img Bitmap image matrix
     * @param init_code_book Template matrix
     * @param max_quan Maximum quantizer sequence
     * @param min_quan Minimum quantizer sequence
     * @param chf_len sequence length
     *@return  this
     */
    public GenCHF(double[][]img,double[][]max_quan,double[][]min_quan,double[][]init_code_book,int chf_len){
        this.chf_len=chf_len;
        this.img=img;
        this.max_quan=max_quan;
        this.min_quan=min_quan;
        this.init_code_book=init_code_book;
    }
    /**Based on the initial input pixel matrix, the maximum/minimum quantifier sequence generates the CHF sequence
     *@return  this
     */
    public ArrayList<int[][]> GenCHF1(){
        ArrayList<int[][]>t=new ArrayList<>();
        int c=1;
        //LBG algorithm is used to regenerate the codebook
        Linde_Buzo_Gray LBG=new Linde_Buzo_Gray(img,c,c,chf_len,init_code_book);
        double[][]maxSingle=new double[max_quan.length*max_quan[0].length][1];                                          //Maximum sequence
        //assignment
        for(int i=0;i<max_quan[0].length;i++){
            for(int j=0;j<max_quan.length;j++){
                maxSingle[i*max_quan.length+j][0]=max_quan[j][i];
            }
        }
        int m=max_quan.length*max_quan[0].length;
        int[][] num_max=new int[chf_len][1];
        double[][] minSingle=new double[min_quan.length*min_quan[0].length][1];                                         //Minimum sequence
        //assignment
        for(int i=0;i<min_quan[0].length;i++){
            for(int j=0;j<min_quan.length;j++){
                minSingle[i*min_quan.length+j][0]=min_quan[j][i];
            }
        }
        int[][] num_min=new int[chf_len][1];
        double[][]t1=LBG.LBG();                                                                                         //The regeneration of code
        for(int i=0;i<m;i++){
            double[][]max_matrix=new double[t1.length][t1[0].length];
            double[][]min_matrix=new double[t1.length][t1[0].length];
            //Index using Max quantizer
            for(int q=0;q<max_matrix.length;q++){
                for(int w=0;w<max_matrix[0].length;w++){
                    max_matrix[q][w]=t1[q][w]-maxSingle[i][0];
                }
            }
            //Maximum quantization sequence histogram statistics
            int max_index = find(max_matrix);
            if(max_index!=-1){
                num_max[max_index][0]++;
            }
            //Index using Min quantizer
            for(int q=0;q<min_matrix.length;q++){
                for(int w=0;w<min_matrix[0].length;w++){
                    min_matrix[q][w]=t1[q][w]-minSingle[i][0];
                }
            }
            //Minimum quantization sequence histogram statistics
            int min_index = find(min_matrix);
            if(min_index!=-1){
                num_min[min_index][0]++;
            }
        }
        t.add(num_max);
        t.add(num_min);
        return t;
    }
    /**Find the first value of the row corresponding to the minimum value of the matrix
     * @param  matrix Input 2-D matrix
     * @return max
     */
    private int find(double[][] matrix){
        int index=min(matrix);
        if(matrix[index][0]>=0){
            return index;
        }else{
            return -1;
        }
    }
    /**
     * Calculate the mininum value of the input matrix and return the result
     *
     * @param  matrix Input 2-D matrix
     * @return max
     */
    private int min(double[][] matrix){
        int index=0;
        double minx=Math.abs(matrix[0][0]);
        for(int i=0;i<matrix.length;i++){
            if(Double.isNaN(matrix[i][0])){
                continue;
            }
            if(Math.abs(matrix[i][0])<minx){
                index=i;
                minx=Math.abs(matrix[i][0]);
            }
        }
        return index;
    }
}
