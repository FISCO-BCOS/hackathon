package com.media.watermark;

/**
 *Function to Compute the convolution of the input matrix with the convolution kernel vector
 * @Time 2020-04-20 14:49:01
 */
public class Conv2 {
    double[][] matrix;                                                                                                  //Input matrix
    double[] convKernel;                                                                                                //Convolution kernel vector
    double[][] convMatrix;                                                                                              //The convolved input matrix

    /*
     * @param
     * matrixa           Input matrix
     * vectorb           Convolution kernel vector
     */
    public Conv2(double[][] matrixa, double[] vectorb){
        this.matrix =matrixa;
        this.convKernel =vectorb;
    }

    /**
     *Function to Compute the convolution of the input matrix with the convolution kernel vector
     */

    public double[][] conv2(){
        int aLength=this.matrix.length;                                                                                 //The number of rows in the matrix
        int aWidth=this.matrix[0].length;                                                                               //The number of coles in the matrix
        int bLength=this.convKernel.length;                                                                             //The length of the convolution kernel vector

        convMatrix =new double[aLength-bLength+1][aWidth];
        for(int i = 0; i < convMatrix.length; ++i)
            for (int j = 0; j < convMatrix[0].length; ++j)
            {
                double tmp = 0;                                                                                         //The result of a convolution
                for (int m = 0; m < bLength;  ++m){
                    tmp =tmp+ convKernel[bLength-1-m] * matrix[i+m][j];                                                 //Convolution formula
                }
                this.convMatrix[i][j] = tmp;
            }
        return this.convMatrix;                                                                                         //The convolved input matrix
    }
}
