package com.media.watermark;

import java.util.ArrayList;
/**Extract robust information sequence
 * @Time 2020-04-20 14:49:01
 */
public class Decodes {
    private int x,y,num,radius_num;
    private double[][]Y,Ip1;
    private Complex[][] DFTc;
    private ArrayList<Complex[][]> Rc;
    private ArrayList<ArrayList<double[][]>> Rg0;
    /**Extract robust information sequence
     * @param x Locate the abscissa of the watermark image
     * @param y The ordinate of the watermark image located
     * @param num Number of templates
     * @param seeds Random seed sequence
     * @param radius_num Number of rings
     * @param Ip1 Positioning image template
     * @param Rg0 Circle the sequence
     * @param R The R channel matrix of the pixel block
     * @return this
     */
    public Decodes(int x, int y, double[][] R, int[]seeds, double[][]Ip1, int num, int radius_num, ArrayList<ArrayList<double[][]>> Rg0){
            this.x=x;
            this.y=y;
            this.num=num;
            this.Y=R;
            this.Ip1=Ip1;
            this.num=seeds.length-1;
            this.radius_num=radius_num;
            this.Rg0=Rg0;
    }
    /**Extract robust information sequence
     */
    public int[] decode(){
        //Template image size information
        int r1=Ip1.length;
        int c1=Ip1[0].length;
        //The submatrix is extracted from the input matrix according to the input coordinate information
        double[][] Rc=new double[r1][c1];                                                                               //submatrix
        for(int i=x;i<x+r1;i++){
            for(int j=y;j<y+c1;j++){
                Rc[i-x][j-y]=Y[i][j];
            }
        }

        //Transform the matrix containing watermark information to the frequency domain
        DFTc=shift(FourierUtils.getFft(Rc));
        Radius radius= Radius.radius(radius_num,r1,c1);
        double[] radiuses=radius.getR();                                                                                //Ring radius sequence
        //Extend a ring that is too small
        for(int i=radiuses.length-1;i>0;--i){
            if(radiuses[i]-radiuses[i-1]<2){
                radiuses[i-1]=radiuses[i]-2;
            }
        }
        //Divide the ring for the frequency domain image containing watermark
        this.Rc = DivideCircle.divideCircle(radiuses,DFTc).getRc();
        ArrayList<double[][]>Rc0=new ArrayList<>();
        int []codenum=new int[radius_num];                                                                              //Robust watermark sequence
        double []cc=new double[num];
        //The pixel matrix is obtained by centralization and inverse transformation
        for(int j=0;j<radius_num;j++){
            if(j==0){
                Rc0.add(new double[0][]);
            }else{
                Complex[][] R= this.Rc.get(j);
                double[][] r = FourierUtils.getInverseFft(shift(R), R.length, R[0].length);
                Rc0.add(r);
            }
        }
        //The correlation between the ring containing watermark information and
        // the corresponding template ring sequence is calculated
        for(int i=1;i<radius_num;i++){
            for(int j=0;j<num;j++){
                Corr2 corr2=new Corr2(Rc0.get(i),Rg0.get(j).get(i),"");
                cc[j]=corr2.Corr2r();
            }
            double temp=cc[0];
            int tempx=0;
            for(int k=0;k<num;k++){
                if(temp<cc[k]){
                    temp=cc[k];
                    tempx=k;
                }
            }
            codenum[i]=tempx;
        }
        return codenum;
    }
    /**
     * centralize the matrix
     * @param qwe Two-dimensional complex array
     * @return The result matrix after centralization
     */
    public Complex[][] shift(Complex[][] qwe){
        Complex[][] temp=new Complex[qwe.length][qwe[0].length];
        for(int i=0;i<qwe.length;i++){
            for(int j=0;j<qwe[0].length;j++){
                if(i>=0&&i<qwe.length/2&&j>=0&&j<qwe[0].length/2){
                    temp[i+qwe.length/2][j+qwe[0].length/2]=qwe[i][j];
                }else if(i>=qwe.length/2&&j>=0&&j<qwe[0].length/2){
                    temp[i-qwe.length/2][j+qwe[0].length/2]=qwe[i][j];
                }else if(j>=qwe[0].length/2&&i>=0&&i<qwe.length/2){
                    temp[i+qwe.length/2][j-qwe[0].length/2]=qwe[i][j];
                }else{
                    temp[i-qwe.length/2][j-qwe[0].length/2]=qwe[i][j];
                }
            }
        }
        return  temp;
    }
}
