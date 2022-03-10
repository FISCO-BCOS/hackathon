package com.media.watermark;

import java.util.ArrayList;
/**
 * Function to find the value of the value in the threshold range in the
 * input matrix and return the coordinate sequence of the corresponding position
 * @Time 2020-04-20 14:49:01
 */
public class Find {

    public ArrayList<Integer> x;                                                                                        //The x-coordinate sequence of the required points
    public ArrayList<Integer> y;                                                                                        //The y-coordinate sequence of the required points

    /*
     * @param
     * x              The x-coordinate sequence of the required points
     * y              The y-coordinate sequence of the required points
     *@return  this
     */
    public Find(ArrayList<Integer> x1,ArrayList<Integer> y1){
        this.x=x1;
        this.y=y1;
    }

    public ArrayList<Integer> getX() {
        return this.x;
    }

    public ArrayList<Integer> getY(){
        return this.y;
    }

    /**
     * Function to find the value of the value in the threshold range in the
     * input matrix and return the coordinate sequence of the corresponding position
     * @param matrix Find object matrix
     * @param radiusMin The lower bound of the interval
     * @param radiusMax The upper bound of the interval
     * @return Find The sequence of coordinates of the required points
     */
    public static Find find(double[][] matrix,double radiusMin,double radiusMax){
        ArrayList<Integer> x=new ArrayList<>();
        ArrayList<Integer> y=new ArrayList<>();

        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){
                if((matrix[i][j]<radiusMax)&&(matrix[i][j]>radiusMin)){
                    x.add(i);
                    y.add(j);
                }
            }
        }
        return new Find(x,y);
    }
}
