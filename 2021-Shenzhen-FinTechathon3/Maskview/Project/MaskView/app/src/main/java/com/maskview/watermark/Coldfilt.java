package com.maskview.watermark;


public class Coldfilt {
    private double[] ha;
    private double[] hb;
    private double[][] x;
    double[][] Y;
    double[][] Ychange;

    public Coldfilt(double[] ha, double[] hb, double[][] x){
        this.ha=ha;
        this.hb=hb;
        this.x=x;
    }

    public double[][] getY(){
        return  this.Y;
    }

    public double[][] coldFilt(){
        int r=x.length;
        int c=x[0].length;
        int m=this.ha.length;
        if(m!=hb.length){
            System.out.println("Lengths of ha and hb must be the same!");
            System.exit(0);
        }
        if ((m%2)>0){
            System.out.println("Lengths of ha and hb must be even!");
        }
        int m2=(int) Math.floor(m/2.0);
        int a[]=new int[r+m-1+m+1];
        for(int i=0;i<a.length;i++){
               a[i]=i-m+1;
        }
        Reflect reflect=new Reflect(a,0.5,r+0.5);
        int[] xe=reflect.reflect();

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

        int[] t=new int[(r+2*m-2-6)/4+1];
        for(int i=0;i<t.length;i++){
            t[i]=6+4*i-1;
        }

        int r2=r/2;
        Y=new double[r2][c];

        double summ=0;
        for(int i=0;i<ha.length;++i){
            summ=summ+ha[i]*hb[i];
        }
        int[] s1=new int[r2/2];
        int[] s2=new int[r2/2];
        if(summ>0){
            for (int i=0;i<s1.length;++i){
                s1[i]=2*i;
                s2[i]=s1[i]+1;
            }

        }
        else{
            for (int i=0;i<s1.length;++i){
                s2[i]=2*i;
                s1[i]=s1[i]+1;
            }
        }

        int[] xe1=new int[t.length];
        int[] xe2=new int[t.length];
        int[] xe3=new int[t.length];
        int[] xe4=new int[t.length];
        for(int i=0;i<t.length;i++){
            xe1[i]=xe[t[i]]-1;
            xe2[i]=xe[t[i]-1]-1;
            xe3[i]=xe[t[i]-2]-1;
            xe4[i]=xe[t[i]-3]-1;
        }

        Change change1=new Change(xe1,x);
        Change change2=new Change(xe2,x);
        Change change3=new Change(xe3,x);
        Change change4=new Change(xe4,x);

        Conv2 conv1=new Conv2(change1.change(),hbo);
        Conv2 conv2=new Conv2(change2.change(),hao);
        Conv2 conv3=new Conv2(change3.change(),hbe);
        Conv2 conv4=new Conv2(change4.change(),hae);
        double[][] y1=plus(conv2.conv2(),conv4.conv2());
        double[][] y2=plus(conv1.conv2(),conv3.conv2());
        for(int i=0;i<s1.length;i++){
            for (int j=0;j<y1[0].length;j++){
            this.Y[s1[i]][j]=y1[i][j];
            this.Y[s2[i]][j]=y2[i][j];
            }
        }

        return this.Y;
    }

    public double[][] plus(double[][] a,double[][] b){
        int mr=a.length;
        int mc=a[0].length;
        double[][] c=new double[mr][mc];
        for (int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                c[i][j]=a[i][j]+b[i][j];
            }
        }
        return c;
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
