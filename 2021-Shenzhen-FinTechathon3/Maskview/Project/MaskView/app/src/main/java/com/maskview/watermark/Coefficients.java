package com.maskview.watermark;


import java.util.ArrayList;

public class Coefficients {

    private ArrayList<ArrayList<Complex[][]>> Rg;

    public Coefficients(ArrayList<ArrayList<Complex[][]> > Rg1){
        this.Rg=Rg1;
    }

    public ArrayList<ArrayList<Complex[][]>> getRg(){
        return this.Rg;
    }

    public static Coefficients coefficients(ArrayList<Complex[][]> Rc, ArrayList<Complex[][]> DFTg, int radiusNum, int num){
        Complex[][] c;
        ArrayList<ArrayList<Complex[][]>> Rg=new ArrayList<>();
        Complex zero=new Complex(0,0);
        ArrayList<Complex[][]> aa=new ArrayList<>();
        Complex[][] a=Rc.get(0);
        int mr=a.length;
        int mc=a[0].length;

        for(int l=0;l<num;l++){
            aa=new ArrayList<>();
            for (int i=0;i<radiusNum;i++){
                a=Rc.get(i);
                Complex[][]b=new Complex[mr][mc];
                c=DFTg.get(l);

                for (int j=0;j<mr;j++){
                    for (int k=0;k<mc;k++){
                        if(a[j][k].getRe()==0.0&&a[j][k].getImage()==0.0){
                            b[j][k]=zero;
                        }
                        else{
                            b[j][k]=c[j][k];
                        }
                    }
                }

                aa.add(b);
            }
            Rg.add(aa);
        }
        return new Coefficients(Rg);
    }

}
