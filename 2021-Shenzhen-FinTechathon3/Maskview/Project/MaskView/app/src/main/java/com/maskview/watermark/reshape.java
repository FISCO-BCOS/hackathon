package com.maskview.watermark;

public class reshape {
    public  static double[][] reshape1(double[][] matrix,int r,int c){
        if(matrix == null){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        if(r == 0 || c == 0){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        int row = matrix.length;
        int columns = matrix[0].length;

        if(columns * row != r * c){
            System.out.println("the matrix size is not equal to r*c at bitimgGen reshape");
            System.exit(0);
            return matrix;
        }
        double [][]result = new double[r][c];
        int rr = 0;
        int cc = 0;
        int index = 0;
        for(int i = 0; i < row; ++i){
            for(int j = 0; j < columns;++j){
                index = i * columns + j;

                rr = index / r;//行
                cc = index % c;//列

                result[rr][cc] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] reshape1(double[] matrix,int r,int c){
        if(matrix == null){
            System.out.println("the matrix is null at bitimgGen reshape");
            System.exit(0);
            return null;
        }

        int length = matrix.length;

        if(length != r * c){
            System.out.println("the matrix size is not equal to r*c at bitimgGen reshape");
            System.exit(0);
            return null;
        }
        double [][]result = new double[r][c];
        int rr = 0;
        int cc = 0;
        int index = 0;
        for(int i = 0; i < length; ++i){
            index = i;
            rr = index / r;//行
            cc = index % c;//列

            result[rr][cc] = matrix[i];
        }
        return result;
    }

    public static double[][] transposition(double[][] matrix){
        int r=matrix.length;
        int c=matrix[0].length;
        double[][] result=new double[c][r];
        for (int i=0;i<r;++i){
            for (int j=0;j<c;++j){
                result[j][i]=matrix[i][j];
            }
        }
        return result;
    }

}
