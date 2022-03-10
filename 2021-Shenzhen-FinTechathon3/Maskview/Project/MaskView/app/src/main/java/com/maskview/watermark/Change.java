package com.maskview.watermark;

public class Change {
    int[] a;
    double[][] b;
    double[][] c;

    public Change(int[] a, double[][] b){
        this.a=a;
        this.b=b;
    }

    public double[][] change(){
        int aa=this.a.length;
        int bb=this.b[0].length;
        int dd=this.b.length;
        if(aa>dd){
            c=new double[aa][this.b[0].length];
            int cc=(aa-dd)/2;
            for(int i=0;i<cc;i++) {
                for (int j = 0; j < this.b[0].length; j++) {
                    c[i][j] = b[cc-1 - i][j];
                }
            }
            for (int i=0;i<dd;i++){
                for(int j=0;j<this.b[0].length;j++){
                c[cc+i][j]=b[i][j];
                }
            }
            for (int i=0;i<cc;i++){
                for(int j=0;j<this.b[0].length;j++) {
                    c[cc + dd + i][j]= b[dd - i-1][j];
                }
            }
        }
        else{
            c=new double[aa][bb];
            for (int i=0;i<aa;i++){
                for (int j=0;j<bb;j++){
                    c[i][j]=b[a[i]][j];
                }
            }
        }
        return this.c;
    }
}
