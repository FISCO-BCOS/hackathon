package com.media.watermark;

/**
 *The corresponding radius sequence is generated according to the size of the input and the number of circle
 * @Time 2020-04-20 14:49:01
 */
public class Radius {

    private double[] r;                                                                                                 //The generated radius sequence

    public Radius(double[] rr){
        this.r=rr;
    }

    public double[] getR(){
        return this.r;
    }

    /**
     * The corresponding radius sequence r is generated according to the size of the input
     *
     * @param rr The number of rows in the matrix
     * @param cc The number of coles in the matrix
     * @param radiusNum The number of circles divided
     * @return r The generated radius sequence
     */
    public static Radius radius(int radiusNum,int rr,int cc){
        double[] r=new double[radiusNum];
        double[] s=new double[radiusNum];
        int[] t=new int[radiusNum];

        //The circular area is evenly distributed
        r[radiusNum-1]=rr/2;                                                                                            //The generated radius sequence
        s[radiusNum-1]=(int) Math.floor(Math.PI*r[radiusNum-1]*r[radiusNum-1]);                                         //Sequence of areas
        t[radiusNum-1]=(int) Math.floor(s[radiusNum-1]/(radiusNum));
        for(int i=radiusNum-2;i>=0;i--){
            r[i]=(double) Math.round(Math.pow((s[i+1]-t[i+1])/ Math.PI,0.5));
            s[i]= Math.PI*r[i]*r[i];
            t[i]=(int) Math.floor(s[i]/(i+1));
        }
        return  new Radius(r);                                                                                          //radius sequence
    }
}
