package com.maskview.watermark;

public class Str2num {
    private String p;                   //输入字符串
    private int[] q;                    //输出整形数组

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

    public static Str2num str2Num(String p){
        int[] q=new int[p.length()];
        for(int i=0;i<p.length();i++){
            q[i]=p.charAt(i)-'0';
        }
        return new Str2num(p,q);
    }
}
