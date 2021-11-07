package com..maskview.watermark;

import java.util.ArrayList;

public class Reflect {

    public int[] X;
    public double minX;
    public double maxX;
    public int[] Y;


    public ArrayList<Integer> t = new ArrayList<>();

    public Reflect(int[] X, double minX, double maxX){
        this.maxX=maxX;
        this.minX=minX;
        this.X=X;
    }

    public int[] reflect(){

        this.Y=this.X;
        for(int i=0;i<this.Y.length;i++){
            if(this.Y[i]>this.maxX){
                t.add(i);
            }
        }



        for(int i=0;i<t.size();i++){
            this.Y[t.get(i)]=(int)(2*maxX)-this.Y[t.get(i)];
        }

        t.clear();

        for (int i=0;i<this.Y.length;i++){
            if(this.Y[i]<minX){
                t.add(i);
            }
        }

        while (!t.isEmpty()){
            for(int i=0;i<t.size();i++){
                this.Y[t.get(i)]=(int)(2*minX) - this.Y[t.get(i)];
            }

            t.clear();

            for(int i=0;i<this.Y.length;i++){
                if(this.Y[i]>this.maxX){
                    t.add(i);
                }
            }

            if (!t.isEmpty()){

                for(int i=0;i<t.size();i++){
                    this.Y[t.get(i)]=(int)(2*maxX)-this.Y[t.get(i)];
                }

            }
            t.clear();
            for (int i=0;i<this.Y.length;i++){
                if(this.Y[i]<minX){
                    t.add(i);
                }
            }

        }
        return this.Y;
    }

}
