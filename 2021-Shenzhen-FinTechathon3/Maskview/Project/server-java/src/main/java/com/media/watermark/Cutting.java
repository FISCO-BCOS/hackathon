package com.media.watermark;

import java.util.ArrayList;
/**Correct the clipping attack to find out the real location of the watermark image
 * @Time 2020-04-20 14:49:01
 */
public class Cutting {
    private ArrayList<Complex[][][]> Iph;                                                                               //High-frequency wavelet coefficient matrix Ip
    private double[][] Ipl;                                                                                             //Low-frequency wavelet coefficient matrix Ip
    private double[][] Ipz;                                                                                             //A pixel matrix containing only high frequency subband coefficients
    private double[][] correlation;                                                                                     //correlation matrix
    private int x,y,z;                                                                                                  //The three dimensional dimensions of the subband coefficient matrix
    private Complex[][][]temp;
    private double[] relative;                                                                                          //relative coefficient matrix
    int rowIp, coleIp, rowUd, coleUd;                                                                                   //The dimension information of the matrix
    double[][]Ud;                                                                                                       //The region matrix to be detected
    String biort,qshift;                                                                                                //Filter
    /**
     * @param biort Filter
     * @param qshift Filter
     * @param nlevels The number of wavelet transforms
     * @param Ip Template pixel matrix
     * @param Ud The region matrix to be detected
     * @return this
     */
    public Cutting(double[][] Ud, double[][] Ip, String biort, String qshift, int nlevels){
        //Gets the size information of the two input matrices
        rowIp=Ip.length;
        coleIp=Ip[0].length;
        rowUd=Ud.length;
        coleUd=Ud[0].length;
        this.Ud=Ud;
        this.biort=biort;
        this.qshift=qshift;
        //The input template image is transformed by wavelet transform
        Dtwavexfm2 dtwavexfm2=new Dtwavexfm2(Ip,nlevels,biort,qshift);
        dtwavexfm2.dtwavexfm2();
        Ipl=dtwavexfm2.getYl();                                                                                         //Low-frequency wavelet coefficient matrix Ip
        Iph=dtwavexfm2.getYh();                                                                                         //High-frequency wavelet coefficient matrix Ip
        //Low frequency subband coefficient set to zero
        for(int i=0;i<Ipl.length;i++){
            for(int j=0;j<Ipl[0].length;j++){
                Ipl[i][j]=0;
            }
        }
        try{
            //The subband coefficient matrix of the highest wavelet
            // is retained, and all others are set to zero
            x=Iph.get(0).length;
            y=Iph.get(0)[0].length;
            z=Iph.get(0)[0][0].length;
            Iph.remove(0);
            Complex[][][] temp1=new Complex[x][y][z];
            for(int i=0;i<x;i++){
                for(int j=0;j<y;j++){
                    for(int k=0;k<z;k++){
                        temp1[i][j][k]=new Complex();
                    }
                }
            }
            Iph.add(temp1);
            x=Iph.get(0).length;
            y=Iph.get(0)[0].length;
            z=Iph.get(0)[0][0].length;
            Complex[][][] temp2=new Complex[x][y][z];
            for(int i=0;i<x;i++){
                for(int j=0;j<y;j++){
                    for(int k=0;k<z;k++){
                        temp2[i][j][k]=new Complex();
                    }
                }
            }
            Iph.add(temp2);
            Iph.remove(0);
            temp=Iph.get(0);
            Iph.remove(0);
            Iph.add(temp);

        }catch (IndexOutOfBoundsException e){
            System.out.println("读取越界");
        }
    }
    /**
     *Correct the clipping attack to find out the real location of the watermark image
     * @return Robust watermark sequence
     */
    public double[] cut(){
        //The pixel matrix containing only the highest level
        // information is obtained by inverse wavelet transform
        Dtwaveifm2 dtwaveifm2=new Dtwaveifm2(Ipl,Iph,biort,qshift);
        Ipz=dtwaveifm2.dtWaveifm2();                                                                                    //The pixel matrix generated after reconstruction
        int rowIpz=Ipz.length,coleIpz=Ipz[0].length;
        correlation =new double[rowUd -rowIpz+1][coleUd -coleIpz+1];                                                    //correlation matrix
        int x0=0,y0=0;
        double max=Double.NEGATIVE_INFINITY;
        for(int i = rowIpz; i<= rowUd; i++){
            for(int j = coleIpz; j<= coleUd; j++){
                double[][] temp=new double[rowIpz][coleIpz];                                                            //Temp subpixel blocks are extracted from Ud
                //Extract
                for(int i0=0;i0<rowIpz;i0++){
                    for(int j0=0;j0<coleIpz;j0++){
                        temp[i0][j0]=Ud[i-rowIpz+i0][j-coleIpz+j0];
                    }
                }
                //Calculates the dependency of the subblock to the template
                Corr2 corr2=new Corr2(Ipz,temp,"");
                correlation[i-rowIpz][j-coleIpz]=corr2.Corr2r();
                //Returns the horizontal and vertical coordinates corresponding to the maximum correlation
                if(correlation[i-rowIpz][j-coleIpz]>max){
                    max= correlation[i-rowIpz][j-coleIpz];
                    x0=i-rowIpz;
                    y0=j-coleIpz;
                }
            }
        }
        relative =new double[3];
        relative[0]=max;
        relative[1]=x0;
        relative[2]=y0;
        return relative;
    }
}
