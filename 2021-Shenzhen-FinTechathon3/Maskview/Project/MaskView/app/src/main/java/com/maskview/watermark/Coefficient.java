package com.maskview.watermark;


import java.util.ArrayList;

public class Coefficient {

    ArrayList<Complex[][]> Rc;

    public Coefficient(ArrayList<Complex[][]> Rc1 ){
        this.Rc=Rc1;
    }

    public ArrayList<Complex[][]> getRc(){
        return this.Rc;
    }

    public static Coefficient coefficient(double[] radiuses, Complex[][] DFTc){
        ArrayList<Complex[][]> Rc=new ArrayList<>();
        Complex zero=new Complex(0,0);

        int radiusNum=radiuses.length;
        int mr=DFTc.length;
        int mc=DFTc[0].length;
        double[][] distance=new double[mr][mc];
        double midX=(mr-1)/2,midY=(mc-1)/2;
        ArrayList<Integer> x;
        ArrayList<Integer> y;

        for (int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                distance[i][j]= Math.pow(Math.pow((double)(i-midX),2.0)+ Math.pow((double)(j-midY),2.0),0.5);
            }
        }

        Find find=Find.find(distance,0,radiuses[0]);

        x=find.getX();
        y=find.getY();

        int sizeNum=x.size();
        Complex[][] rc=new Complex[mr][mc];

        for (int i=0;i<mr;++i){
            for (int j=0;j<mc;j++){
                rc[i][j]=zero;
            }
        }

        for (int i=0;i<sizeNum;++i){
            int xx= (int) x.get(i);
            int yy= (int) y.get(i);
            rc[xx][yy]=DFTc[xx][yy];
        }
        Rc.add(rc);
        for (int i=0;i<radiusNum-1;++i){
            rc=new Complex[mr][mc];
            int k=1;
            for(int j=0;j<mr;++j){
                for(int l=0;l<mc;++l){
                    rc[j][l]=zero;
                }
            }

            for(int j=0;j<mr;j++){
                for(int l=0;l<mc;l++){
                    if((distance[j][l]>radiuses[i])&&(distance[j][l])<radiuses[i+1]){
                        rc[j][l]=DFTc[j][l];
                    }
                }
            }
            k=k+1;
            Rc.add(rc);

        }

        return new Coefficient(Rc);
    }

}
