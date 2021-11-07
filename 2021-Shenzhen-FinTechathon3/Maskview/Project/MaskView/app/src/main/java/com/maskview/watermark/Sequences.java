package com.maskview.watermark;

import java.util.ArrayList;
import java.util.Random;

public class Sequences {

    private ArrayList<int[][]> randSequences;

    public ArrayList<int[][]> getRandSequences(){
        return this.randSequences;
    }

    public Sequences(ArrayList<int[][]> aa){
        this.randSequences=aa;
    }

    public static Sequences sequences(int[]seeds, int rr, int cc, int num){ //randseeds,row,cole,number of seeds
        ArrayList<int[][]> randSequences1=new ArrayList<>();
        for (int i=0;i<num;i++){
            Random r1 = new Random((long)seeds[i]);
            int[][] text=new int[rr][cc];
            for (int j=0;j<rr;j++) {
                for (int k = 0; k < cc; k++) {
                    text[j][k]=r1.nextInt(256);
                }
            }
            randSequences1.add(text);
        }
        return new Sequences(randSequences1);
    }
}
