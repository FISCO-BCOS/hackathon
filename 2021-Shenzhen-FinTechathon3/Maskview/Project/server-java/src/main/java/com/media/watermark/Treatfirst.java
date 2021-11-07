package com.media.watermark;

import java.util.ArrayList;
/*Add watermark information and location information in the two channels of the image
 @Time 2020-04-20 14:49:01
 */
public class Treatfirst {

    private int[][][] coverImg;                                                                                         //Pixel matrix after adding watermark
    private double[] zeromark;                                                                                          //Extracted zero watermark information sequence

    /**
     * @param coverImg Pixel matrix after adding watermark
     * @param zeroMark Extracted zero watermark information sequence
     */
    public Treatfirst(int[][][] coverImg,double[] zeroMark){
        this.coverImg=coverImg;
        this.zeromark=zeroMark;
    }

    public int[][][] getCoverImg() {
        return coverImg;
    }

    public double[] getZeromark() {
        return zeromark;
    }

    /**Add watermark information and location information in the two channels of the image
     * @param biort filter
     * @param qShift filter
     * @param coverImg Host image matrix
     * @param markImg Watermark image matrix
     * @param nLevels The number of layers of the wavelet transform
     * @param pattern zone bit
     * @param Ug Frequency domain image sequence of random image after ring division
     * @param minradius The radius of the innermost ring
     * @return Generated image matrix sequence
     */
    public static Treatfirst treatfirst(int[][][] coverImg, int nLevels, String biort, String qShift, Complex[][] markImg, double[][] Ug,double minradius,int pattern){
        int markIrow=markImg.length;                                                                                    //The number of rows in the host image matrix
        int markIcole=markImg[0].length;                                                                                //The number of coles in the host image matrix
        int[][] Yc=new int[markIrow][markIcole];
        double[][] G=new double[markIrow][markIcole];                                                                   //G channel pixel matrix
        double[][] B=new double[markIrow][markIcole];                                                                   //B channel pixel matrix
        double[][] R=new double[markIrow][markIcole];                                                                   //R channel pixel matrix

        //Gets the pixel matrix of each channel of RGB image
        for (int i=0;i<markIrow;i++){
            for(int j=0;j<markIcole;j++){
                Yc[i][j]=coverImg[i][j][0];
                R[i][j]=(double)coverImg[i][j][0];
                G[i][j]=coverImg[i][j][1];
                B[i][j]=coverImg[i][j][2];
            }
        }
        //Obtain the zero-watermark sequence containing image feature information in the B channel
        Watermarkembedzero watermarkembedzero=Watermarkembedzero.watermarkembedzero(B);

        //Converts the pixel value image matrix of R channel to the frequency domain
        Complex[][] DFTR=FourierUtils.getFft(R);                                                                        //fast Fourier transform
        DFTR=FourierUtils.shift_to_center(DFTR);

        //The G channel of the host image is transformed by wavelet transform
        Dtwavexfm2 dtwavexfm2G=new Dtwavexfm2(G,nLevels,biort,qShift);
        dtwavexfm2G.dtwavexfm2();
        double[][] Ul=dtwavexfm2G.getYl();                                                                              //Low-frequency wavelet coefficient matrix
        ArrayList<Complex[][][] > Uh=dtwavexfm2G.getYh();                                                               //High-frequency wavelet coefficient matrix

        //The localization image is transformed by wavelet transform
        Dtwavexfm2 dtwavexfm2Ug=new Dtwavexfm2(Ug,nLevels,biort,qShift);
        dtwavexfm2Ug.dtwavexfm2();
        double[][] Gl=dtwavexfm2Ug.getYl();                                                                             //Low-frequency wavelet coefficient matrix
        ArrayList<Complex[][][] > Gh=dtwavexfm2Ug.getYh();                                                              //High-frequency wavelet coefficient matrix

        //The wavelet coefficient matrix of the location image is superimposed on the host image
        double[][] Pl=Ul;                                                                                               //The low-frequency wavelet coefficient matrix of the fusion image
        ArrayList<Complex[][][] > Ph=new ArrayList<>();                                                                 //The high-frequency wavelet coefficient matrix of the fusion image
        for (int i=0;i<nLevels-1;i++){
            Ph.add(multyMatrix(0.0,1,Gh.get(i),Uh.get(i)));
        }
        Ph.add(multyMatrix(0.5,0.5,Gh.get(nLevels-1),Uh.get(nLevels-1)));

        //The fused image pixel matrix is obtained by inverse transformation
        // of the superimposed wavelet coefficient matrix
        Dtwaveifm2 dtwaveifm21=new Dtwaveifm2(Pl,Ph,biort,qShift);
        double[][] Gnew=dtwaveifm21.dtWaveifm2();                                                                       //The pixel matrix of the newly generated G channel

        //Embed the watermark image into the R channel of the host image
        Watermarkembed watermarkembedR=Watermarkembed.watermarkembed(DFTR,markImg,minradius,pattern);
        double[][] Rnew=watermarkembedR.getWatermarkedImg();                                                            //The pixel matrix of the newly generated R channel

        int rowR=Rnew.length;                                                                                           //The number of rows in the R channel pixel matrix
        int coleR=Rnew[0].length;                                                                                       //The number of coles in the R channel pixel matrix

        int row=min(rowR,markIrow);
        int cole=min(coleR,markIcole);

        //The original host image matrix is overlaid with the newly generated pixel matrix
        for (int i=0;i<row;i++){
            for (int j=0;j<cole;j++){
                coverImg[i][j][0]=(int) Math.floor(Rnew[i][j]);
                coverImg[i][j][1]=(int) Math.floor(Gnew[i][j]);
            }
        }

        return new Treatfirst(coverImg,watermarkembedzero.getBinaryFeature());
    }

    /**
     * The two complex matrices are fused according to the input complex matrix and the corresponding coefficients
     * @param ratioa The ratio of the complex matrix matrixa to the new matrix
     * @param ratiob The ratio of the complex matrix matrixb to the new matrix
     * @param matrixa Complex matrix a
     * @param matrixb Complex matrix b
     * @return The complex matrix after fusion
     */
    public static Complex[][][] multyMatrix(double ratioa,double ratiob,Complex[][][] matrixa,Complex[][][] matrixb){
        int rowa=matrixa.length;                                                                                        //The number of rows in the complex matrix a
        int colea=matrixa[0].length;                                                                                    //The number of coles in the complex matrix a
        int channela=matrixa[0][0].length;                                                                              //The number of channels in the complex matrix a
        Complex[][][] matrixNew=new Complex[rowa][colea][channela];                                                     //The complex matrix generated after fusion
        for(int i=0;i<rowa;i++){
            for (int j=0;j<colea;j++){
                for (int k=0;k<channela;k++){
                    matrixNew[i][j][k]=matrixa[i][j][k].multy(ratioa).plus(matrixb[i][j][k].multy(ratiob));
                }
            }
        }
        return matrixNew;                                                                                               //The complex matrix generated after fusion
    }

    /**
     * Calculate the number smaller than the two input parameters
     * @param a input parameter
     * @param b input parameter
     * @return smaller number
     */
    public static int min(int a,int b){
        if(a<b){
            return a;
        }
        else{
            return b;
        }
    }
}
