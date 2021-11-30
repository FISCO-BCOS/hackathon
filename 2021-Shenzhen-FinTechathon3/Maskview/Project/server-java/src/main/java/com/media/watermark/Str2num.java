package com.media.watermark;

/*Converts the input string to an int array
 @Time 2020-04-20 14:49:01
 */
public class Str2num {
    private String p;                                                                                                   //Input string
    private int[] q;                                                                                                    //Output array

    public String getP (){
        return this.p;
    }

    public int[] getq (){
        return this.q;
    }

    public Str2num(String b,int[] qq){
        this.p=b;
        this.q=qq;
    }

    /**
     * Compute the conjugate of the complex number of inputs
     */
    public static Str2num str2Num(String p){
        int[] q=new int[p.length()];
        for(int i=0;i<p.length();i++){
            q[i]=p.charAt(i)-'0';
        }
        return new Str2num(p,q);
    }
}
