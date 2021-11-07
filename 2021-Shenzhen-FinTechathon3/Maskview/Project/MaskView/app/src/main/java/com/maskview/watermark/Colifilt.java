package com.maskview.watermark;


public class Colifilt {
    private double[] ha;
    private double[] hb;
    private double[][] x;
    double[][] Y;
    double[][] Ychange;

    public Colifilt(double[] ha, double[] hb, double[][] x){
        this.ha=ha;
        this.hb=hb;
        this.x=x;
    }

    public double[][] getY(){
        return  this.Y;
    }

    public double[][] coliFilt(){
        int r=x.length;
        int c=x[0].length;
        if(r==0){
            System.exit(0);
        }
        int m=this.ha.length;
        if(m!=hb.length){
            System.out.println("Lengths of ha and hb must be the same!");
            System.exit(0);
        }
        if ((m%2)>0){
            System.out.println("Lengths of ha and hb must be even!");
        }

        int m2=(int) Math.floor(m/2.0);
        int r2=r*2;
        Y=new double[r2][c];
        int a[]=new int[r+m2-1+m2+1];
        for(int i=0;i<a.length;i++){
            a[i]=i-m2+1;
        }

        Reflect reflect=new Reflect(a,0.5,r+0.5);
        int[] xe=reflect.reflect();

        int[] t=new int[(r+m-4)/2+1];
        for(int i=0;i<t.length;i++){
            t[i]=4+2*i;
        }
        double summ=0;
        for(int i=0;i<ha.length;++i){
            summ=summ+ha[i]*hb[i];
        }
        int[] ta=new int[t.length];
        int[] tb=new int[t.length];
        if(summ>0){
            for (int i=0;i<t.length;++i){
                ta[i]=t[i];
                tb[i]=t[i]-1;
            }

        }
        else{
            for (int i=0;i<ta.length;++i){
                ta[i]=t[i]-1;
                tb[i]=t[i];
            }
        }
        double[] hao=new double[m/2];
        double[] hae=new double[m/2];
        double[] hbo=new double[m/2];
        double[] hbe=new double[m/2];
        for (int i=0;i<hao.length;i++){
            hao[i]=ha[i*2];
            hae[i]=ha[i*2+1];
            hbo[i]=hb[i*2];
            hbe[i]=hb[i*2+1];
        }

        int[]s=new int[r*2/4];
        for (int i=0;i<s.length;i++){
            s[i]=4*i;
        }

        int[] xe1=new int[tb.length];
        int[] xe2=new int[tb.length];
        int[] xe3=new int[tb.length];
        int[] xe4=new int[tb.length];

        for(int i=0;i<tb.length;i++){
            xe1[i]=xe[tb[i]-3]-1;
            xe2[i]=xe[ta[i]-3]-1;
            xe3[i]=xe[tb[i]-1]-1;
            xe4[i]=xe[ta[i]-1]-1;
        }
        Change change1=new Change(xe1,x);
        Change change2=new Change(xe2,x);
        Change change3=new Change(xe3,x);
        Change change4=new Change(xe4,x);

        Conv2 conv1=new Conv2(change1.change(),hae);
        Conv2 conv2=new Conv2(change2.change(),hbe);
        Conv2 conv3=new Conv2(change3.change(),hao);
        Conv2 conv4=new Conv2(change4.change(),hbo);

        conv1.conv2();
        conv2.conv2();
        conv3.conv2();
        conv4.conv2();

        for (int i=0;i<s.length;i++){
            for (int j=0;j<c;j++){
                Y[s[i]][j]=conv1.c[i][j];
                Y[s[i]+1][j]=conv2.c[i][j];
                Y[s[i]+2][j]=conv3.c[i][j];
                Y[s[i]+3][j]=conv4.c[i][j];
            }
        }

        return this.Y;
    }




}

