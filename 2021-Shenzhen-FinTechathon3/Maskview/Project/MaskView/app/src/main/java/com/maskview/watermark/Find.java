package com.maskview.watermark;

import java.util.ArrayList;

public class Find {

    public ArrayList<Integer> x;
    public ArrayList<Integer> y;

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

    public static Find find(double[][] img,double radiusMin,double radiusMax){
        ArrayList<Integer> x=new ArrayList<>();
        ArrayList<Integer> y=new ArrayList<>();

        for(int i=0;i<img.length;i++){
            for(int j=0;j<img[0].length;j++){
                if((img[i][j]<radiusMax)&&(img[i][j]>radiusMin)){
                    x.add(i);
                    y.add(j);
                }
            }
        }
        return new Find(x,y);
    }
}
