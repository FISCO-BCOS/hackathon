package com.maskview.watermark;


public class Colfilter {

    public double[][] X;
    public double[] h;
    public double[][] Y;
    public double[][] Ychange;      //矩阵转置

    //public int mr;
    //public int mc;


    public int[] aa;
    public int[] xe;
    public double[][] xChange;      //改变尺寸


    public Colfilter(double[][] X, double[] h      /*,int mr,int mc*/){

        this.X=X;
        this.h=h;
        //this.mr=mr;
        //this.mc=mc;

    }

    public double[][] colfilter(){

        int r=this.X.length;
        int c=this.X[0].length;
        int m=h.length;
        int m2=(int) Math.floor((double)(m/2));
        aa=new int[r+m2*2];
        if (X!=null){
            int i=1-m2;
            int k=0;
            while(i<=r+m2){
                aa[k]=i;
                ++k;
                ++i;
            }
            /*for(int i=1-m2;i<r+m2;i++){
               aa[i]=i;
           }*/

           Reflect reflect=new Reflect(aa,0.5,r+0.5);
           xe=new int[r+m2*2];
           xe=reflect.reflect();
           Change change=new Change(xe,X);
           xChange=change.change();
           Conv2 conv2=new Conv2(xChange,h);
           this.Y=new double[X.length][X[0].length];
           this.Y=conv2.conv2();
        }
        else{
            Y=new double[r+1-m%2][c];
        }
        return this.Y;
    }

    public double[][] transPosition(){
        int mm=this.Y.length;
        int cc=this.Y[0].length;
        this.Ychange=new double[cc][mm];
        for (int i=0;i<mm;i++){
            for(int j=0;j<cc;j++){
                this.Ychange[j][i]=this.Y[i][j];
            }
        }
        return this.Ychange;
    }


}
