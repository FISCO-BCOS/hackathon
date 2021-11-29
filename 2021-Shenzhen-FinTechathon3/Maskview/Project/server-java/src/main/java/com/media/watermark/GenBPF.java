package com.media.watermark;

/**
 * The bitmap feature sequence is generated according to the input bitmap image and the initial codebook matrix
 * @Time 2020-04-20 14:49:01
 */
public class GenBPF {
    private double[][] img;                                                                                             //Bitmap image matrix
    private double[][] init_code_book;                                                                                  //Template matrix
    private int bpf_cbb;                                                                                                //Template block size
    private int bpf_cbs;                                                                                                //Block size

    /**The CHF and BPF sequences are constructed using the input two quantifiers and bitmap images
     * @param img Bitmap image matrix
     * @param init_code_book Template matrix
     * @param bpf_cbb Template block size
     * @param bpf_cbs Block size
     *@return  this
     */
    public GenBPF(double[][]img,double[][]init_code_book,int bpf_cbb,int bpf_cbs){
        this.img=img;
        this.init_code_book=init_code_book;
        this.bpf_cbb=bpf_cbb;
        this.bpf_cbs=bpf_cbs;
    }
    /**The bitmap feature sequence is generated according to the input bitmap image and the initial codebook matrix
     *@return  this
     */
    public double[][] GenBPF1(){
        //The size of the block
        int m=bpf_cbs;
        int n=bpf_cbs;
        //The size of the input matrix
        int M=img.length;
        int N=img[0].length;
        //Excision of excess
        int sub_m=(int)Math.floor(M/m);
        int sub_n=(int)Math.floor(N/n);
        //Convert to bitmap image
        img=im2bwd(img);
        //LBG algorithm is used to regenerate the codebook
        Linde_Buzo_Gray LBG=new Linde_Buzo_Gray(img,bpf_cbb,1,bpf_cbb,init_code_book);
        int[][]code_book=im2bwi(LBG.LBG());                                                                             //The regeneration of code
        int mc=code_book.length;
        int nc=code_book[0].length;
        int sub_mc=(int)Math.floor(mc/m);                                                                               //The number of blocks per row
        int sub_nc=(int)Math.floor(nc/n);                                                                               //The number of blocks per cole
        double[][]num=new double[sub_mc*sub_nc][1];
        //Calculate the size of the difference between each block corresponding to
        // the input image and the template image, and find the most similar block
        for(int i=0;i<sub_m;i++){
            for(int j=0;j<sub_n;j++){
                int[][]tmp_img=new int[m][n];
                //Generate 01 image from original image
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
                        //Extract the child blocks in the template
                        for(int q=0;q<m;q++){
                            for(int w=0;w<n;w++){
                                tmp[q][w]=code_book[l*m+q][k*n+w];
                            }
                        }
                        int summ= differNum(tmp_img,tmp);                                                               //quantity variance
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
    /**Calculate the number of data that differ between the two matrices
     * @param A Input 2-D matrix
     * @param B Input 2-D matrix
     *@return  this
     */
    private int differNum(int[][]A, int[][]B){
        int sum=0;                                                                                                      //The number of different data
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A[0].length;j++){
                if(A[i][j]!=B[i][j]){
                    sum++;
                }
            }
        }
        return sum;
    }

    /**The input matrix is segmented according to a certain threshold and converted into bitmap image
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private int[][] im2bwi(double[][] matrix){
        int[][]temp=new int[matrix.length][matrix[0].length];
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
                if(matrix[i][j]>0.5){
                    temp[i][j]=1;
                }else{
                    temp[i][j]=0;
                }
            }
        }
        return temp;
    }
    /**The input matrix is segmented according to a certain threshold and converted into bitmap image
     * @param matrix Input 2-D matrix
     *@return  this
     */
    private double[][] im2bwd(double[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
                if(matrix[i][j]>0.5){
                    matrix[i][j]=1;
                }else{
                    matrix[i][j]=0;
                }
            }
        }
        return matrix;
    }
}
