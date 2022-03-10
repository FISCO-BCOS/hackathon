package com.media.watermark;

/**
 *Filter the columns of image X using the two filters ha and hb = reverse(ha).
 * ha operates on the odd samples of X and hb on the even samples.
 * Both filters should be even length, and h should be approx linear phase with
 * a quarter sample advance from its mid pt (ie |h(m/2)| > |h(m/2 + 1)|).
 * @Time 2020-04-20 14:49:01
 */

public class Coldfilt {
    private double[] ha;                                                                                                //Odd sample convolution kernel
    private double[] hb;                                                                                                //Even sample convolution kernel
    private double[][] x;                                                                                               //2-D image
    double[][] Y;                                                                                                       //The filtered image matrix
    double[][] Ychange;                                                                                                 //The matrix after transpose

    /*
     * @param
     * X             2D matrix
     * ha            Odd sample convolution kernel
     * hb            Even sample convolution kernel
     */
    public Coldfilt(double[] ha, double[] hb, double[][] x){
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
     * */
    public double[][] coldFilt(){
        int r=x.length;                                                                                                 //The number of rows in the matrix x
        int c=x[0].length;                                                                                              //The number of coles in the matrix x
        int m=this.ha.length;                                                                                           //The length of Odd sample convolution kernel                                                                                          //The length of

        //To determine whether the two convolution kernels have the same length
        if(m!=hb.length){
            System.out.println("Lengths of ha and hb must be the same!");
            System.exit(0);
        }

        //To determine whether the length of the convolution kernel is even
        if ((m%2)>0){
            System.out.println("Lengths of ha and hb must be even!");
        }

        //Set up vector for symmetric extension of a with repeated end samples.
        int sample[]=new int[r+m-1+m+1];                                                                                //sample sequence
        for(int i=0;i<sample.length;i++){
               sample[i]=i-m+1;
        }
        Reflect reflect=new Reflect(sample,0.5,r+0.5);
        int[] xe=reflect.reflect();                                                                                     //The symmetrically expanded vector

        //Select odd and even samples from ha and hb.
        double[] hao=new double[m/2];                                                                                   //extracted sample odd sequence from ha
        double[] hae=new double[m/2];                                                                                   //extracted sample even sequence from hb
        double[] hbo=new double[m/2];                                                                                   //extracted sample odd sequence from hb
        double[] hbe=new double[m/2];                                                                                   //extracted sample even sequence from hb
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

        //Compute the dot product of vectors
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

        //Perform filtering on columns of extended matrix X(xe,:) in 4 ways

        //In the expanded image matrix, the submatrix is extracted by coordinates
        int[] xeHbo=new int[t.length];
        int[] xeHao=new int[t.length];
        int[] xeHbe=new int[t.length];
        int[] xeHae=new int[t.length];
        for(int i=0;i<t.length;i++){
            xeHbo[i]=xe[t[i]]-1;
            xeHao[i]=xe[t[i]-1]-1;
            xeHbe[i]=xe[t[i]-2]-1;
            xeHae[i]=xe[t[i]-3]-1;
        }

        //Expand the image matrix as an extension vector
        Change changeHbo=new Change(xeHbo,x);
        Change changeHao=new Change(xeHao,x);
        Change changeHbe=new Change(xeHbe,x);
        Change changeHae=new Change(xeHae,x);

        //The expanded image matrix is filtered
        Conv2 convHbo=new Conv2(changeHbo.change(),hbo);
        Conv2 convHao=new Conv2(changeHao.change(),hao);
        Conv2 convHbe=new Conv2(changeHbe.change(),hbe);
        Conv2 convHae=new Conv2(changeHae.change(),hae);
        double[][] y1=plus(convHao.conv2(),convHae.conv2());
        double[][] y2=plus(convHbo.conv2(),convHbe.conv2());

        //Merge the two submatrices
        for(int i=0;i<s1.length;i++){
            for (int j=0;j<y1[0].length;j++){
            this.Y[s1[i]][j]=y1[i][j];
            this.Y[s2[i]][j]=y2[i][j];
            }
        }

        return this.Y;
    }

    /**
     * Function to Compute the sum of two matrices
     * @param matrixa matrixb
     *          two matrices of the same size
     * @return sumMatrix
     * sum of matrices
     */
    public double[][] plus(double[][] matrixa,double[][] matrixb){
        int mr=matrixa.length;
        int mc=matrixa[0].length;
        double[][] sumMatrix=new double[mr][mc];
        for (int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                sumMatrix[i][j]=matrixa[i][j]+matrixb[i][j];
            }
        }
        return sumMatrix;
    }

    /**
     * Function to Transpose the matrix
     * @return
     *matrix after transpose
     */
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
