package com.media.watermark;

/**
 * The CHF and BPF sequences are constructed using the input two quantifiers and bitmap images
 * @Time 2020-04-20 14:49:01
 */
public class Genbinaryfeature {
    private double[] binaryFeature;

    public Genbinaryfeature (double[] a){
        this.binaryFeature=a;
    }

    public double[] getBinaryFeature() {
        return binaryFeature;
    }
    /**The CHF and BPF sequences are constructed using the input two quantifiers and bitmap images
     * @param CHF_max_all Maximum quantizer
     * @param CHF_min_all Minimum quantizer
     * @param BPF_all Bitmap matrix
     * @param bpf_num The length of the bitmap sequence
     * @param chf_num Quantify the length of the sequence
     *@return  this
     */
    public static Genbinaryfeature genbinaryFeature(double[][] CHF_max_all, double[][] CHF_min_all, double[][] BPF_all, int bpf_num, int chf_num){

        int wm_num = bpf_num+chf_num*2;                                                                                 //The length of the watermark sequence
        double[][] sum_vector=new double[CHF_max_all.length+CHF_min_all.length+BPF_all.length][1];                      //Splicing sequence
        for (int i=0;i<chf_num;++i){
            sum_vector[i][0]=CHF_max_all[i][0];
            sum_vector[i+chf_num][0]=CHF_max_all[i][0];
        }
        for (int i=0;i<BPF_all.length;++i){
            sum_vector[i+chf_num*2][0]=BPF_all[i][0];
        }
        double[] binary_sum_vector = new double[wm_num];                                                                //Watermark sequence
        double avg=mean(sum_vector);
        for (int i=0;i<sum_vector.length;++i){
            if(sum_vector[i][0]>avg){
                binary_sum_vector[i]=1;
            }
        }
        return new Genbinaryfeature(binary_sum_vector);
    }

    /**Calculate the mean of the matrix
     * @param matrix 2-D Input matrix
     *@return  this
     */
    public static double mean(double[][] matrix){
        double sum=0;
        double avg=0;
        if (matrix.length==0){
            System.out.println("this matrix is null at Genbinaryfeature mean");
            return avg;
        }
        for (int i=0;i<matrix.length;++i){
            for (int j=0;j<matrix[0].length;++j){
                sum+=matrix[i][j];
            }
        }
        return sum/(matrix.length*matrix[0].length);
    }
}
