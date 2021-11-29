package com.maskview.watermark;

public class Radius {

    private double[] r;

    public Radius(double[] rr){
        this.r=rr;
    }

    public double[] getR(){
        return this.r;
    }

    public static Radius radius(int radiusNum,int rr,int cc){
        double[] r=new double[radiusNum];
        double[] s=new double[radiusNum];
        int[] t=new int[radiusNum];

        r[radiusNum-1]=rr/2;
        s[radiusNum-1]=(int) Math.floor(Math.PI*r[radiusNum-1]*r[radiusNum-1]);
        t[radiusNum-1]=(int) Math.floor(s[radiusNum-1]/(radiusNum));
        for(int i=radiusNum-2;i>=0;i--){
            r[i]=(double) Math.round(Math.pow((s[i+1]-t[i+1])/ Math.PI,0.5));
            s[i]= Math.PI*r[i]*r[i];
            t[i]=(int) Math.floor(s[i]/(i+1));
        }
        return  new Radius(r);
    }
}
