package com.media.watermark;

/**
 *Function to extend the rows of the input matrix in the format of the sample vector
 * @Time 2020-04-20 14:49:01
 */
public class Change {
    int[] sampleVector;                                                                                                 //Array of sample vectors
    double[][] matrix;                                                                                                  //Input extension
    double[][] expandedMatrix;                                                                                          //The matrix obtained by extending the matrix x according to the sample vector

    /*
     * @param
     * sampleVector      Array of sample vectors
     * matrix            Matrix with extension
     */
    public Change(int[] sampleVector, double[][] matrix){
        this.sampleVector =sampleVector;
        this.matrix = matrix;
    }

    /**
     * Function to extend the rows of the input matrix in the format of the sample vector
     *@return The changed matrix
     * */
    public double[][] change(){
        int svLen=this.sampleVector.length;                                                                             //The length of the sample vector
        int matColes=this.matrix[0].length;                                                                             //The number of columns in the input matrix
        int matRows=this.matrix.length;                                                                                 //The number of rows in the input matrix

        //Compare the length of the vector to the number of rows in the matrix
        // extend the rows of the input matrix in the format of the sample vector
        if(svLen>matRows){
            expandedMatrix =new double[svLen][this.matrix[0].length];
            int cc=(svLen-matRows)/2;
            for(int i=0;i<cc;i++) {
                for (int j = 0; j < this.matrix[0].length; j++) {
                    expandedMatrix[i][j] = matrix[cc-1 - i][j];
                }
            }
            for (int i=0;i<matRows;i++){
                for(int j = 0; j<this.matrix[0].length; j++){
                expandedMatrix[cc+i][j]= matrix[i][j];
                }
            }
            for (int i=0;i<cc;i++){
                for(int j = 0; j<this.matrix[0].length; j++) {
                    expandedMatrix[cc + matRows + i][j]= matrix[matRows - i-1][j];
                }
            }
        }
        //The submatrix is extracted from the matrix according to the sample vector
        else{
            expandedMatrix =new double[svLen][matColes];
            for (int i=0;i<svLen;i++){
                for (int j=0;j<matColes;j++){
                    expandedMatrix[i][j]= matrix[sampleVector[i]][j];
                }
            }
        }
        return this.expandedMatrix;                                                                                     //The transformed matrix
    }
}
