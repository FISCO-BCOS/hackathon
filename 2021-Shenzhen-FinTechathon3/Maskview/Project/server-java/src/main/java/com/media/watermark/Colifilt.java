package com.media.watermark;

/**
 * Filter the columns of image X using the two filters ha and hb = reverse(ha).
 * ha operates on the odd samples of X and hb on the even samples.
 * Both filters should be even length, and h should be approx linear phase with
 * a quarter sample advance from its mid pt (ie |h(m/2)| > |h(m/2 + 1)|).
 * @Time 2020-04-20 14:49:01
 */

public class Colifilt {
    private double[] ha;                                                                                                //Odd sample convolution kernel
    private double[] hb;                                                                                                //Even sample convolution kernel
    private double[][] x;                                                                                               //Input image X
    double[][] Y;                                                                                                       //The matrix obtained by filtering the matrix x
    double[][] Ychange;                                                                                                 //The matrix obtained by transposing the Y matrix

    /*
     * @param
     * X             2D matrix
     * ha            Odd sample convolution kernel
     * hb            Even sample convolution kernel
     */
    public Colifilt(double[] ha, double[] hb, double[][] x){
        this.ha=ha;
        this.hb=hb;
        this.x=x;
    }

    public double[][] getY(){
        return  this.Y;
    }

    /**
     * Filter the columns of image X using the two filters ha and hb = reverse(ha).
     *@return The filtered matrix
     * The output is interpolated by two from the input sample rate and the results
     * from the two filters, Ya and Yb, are interleaved to give Y.
     * Symmetric extension with repeated end samples is used on the composite X
     * columns before each filter is applied.
     * */
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

        //Set up vector for symmetric extension of x with repeated end samples.
        int sample[]=new int[r+m2-1+m2+1];
        for(int i=0;i<sample.length;i++){
            sample[i]=i-m2+1;
        }

        Reflect reflect=new Reflect(sample,0.5,r+0.5);
        int[] xe=reflect.reflect();                                                                                     //The symmetrically expanded vector

        int[] t=new int[(r+m-4)/2+1];
        for(int i=0;i<t.length;i++){
            t[i]=4+2*i;
        }

        //Compute the dot product of vectors
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
        double[] hao=new double[m/2];                                                                                   //extracted sample odd sequence from ha
        double[] hae=new double[m/2];                                                                                   //extracted sample even sequence from ha
        double[] hbo=new double[m/2];                                                                                   //extracted sample odd sequence from hb
        double[] hbe=new double[m/2];                                                                                   //extracted sample even sequence from hb
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

        int[] xeHae=new int[tb.length];
        int[] xeHbe=new int[tb.length];
        int[] xeHao=new int[tb.length];
        int[] xeHbo=new int[tb.length];

        //In the expanded image matrix, the submatrix is extracted by coordinates
        for(int i=0;i<tb.length;i++){
            xeHae[i]=xe[tb[i]-3]-1;
            xeHbe[i]=xe[ta[i]-3]-1;
            xeHao[i]=xe[tb[i]-1]-1;
            xeHbo[i]=xe[ta[i]-1]-1;
        }

        //Expand the image matrix as an extension vector
        Change changeHae=new Change(xeHae,x);
        Change changeHbe=new Change(xeHbe,x);
        Change changeHao=new Change(xeHao,x);
        Change changeHbo=new Change(xeHbo,x);

        //The expanded image matrix is filtered
        Conv2 convHae=new Conv2(changeHae.change(),hae);
        Conv2 convHbe=new Conv2(changeHbe.change(),hbe);
        Conv2 convHao=new Conv2(changeHao.change(),hao);
        Conv2 convHbo=new Conv2(changeHbo.change(),hbo);

        convHae.conv2();
        convHbe.conv2();
        convHao.conv2();
        convHbo.conv2();

        //The four submatrices are combined in a coordinate sequence
        for (int i=0;i<s.length;i++){
            for (int j=0;j<c;j++){
                Y[s[i]][j]=convHae.convMatrix[i][j];
                Y[s[i]+1][j]=convHbe.convMatrix[i][j];
                Y[s[i]+2][j]=convHao.convMatrix[i][j];
                Y[s[i]+3][j]=convHbo.convMatrix[i][j];
            }
        }

        return this.Y;
    }




}

