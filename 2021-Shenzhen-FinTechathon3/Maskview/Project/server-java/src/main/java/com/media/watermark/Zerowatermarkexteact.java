package com.media.watermark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
/*CHF and BPF are used to extract 01 sequences representing image features from the input pixel matrix
 @Time 2020-04-20 14:49:01
 */
public class Zerowatermarkexteact {
    private String file;                                                                                                //Image path to be detected
    /**
     *@param file  Image path to be detected
     *@return  this
     */
    public Zerowatermarkexteact(String file) throws IOException {
        int fg=0;                                                                                                       //Extract result flag
        int[] array_01={0,1,1,0,1,0,0,0,1,0};                                                                           //Initial codebook sequence
        //Import detection image
        this.file = file;
        File file0 = new File(this.file);
        BufferedImage BI = ImageIO.read(file0);                                                                         //Image matrix to be detected
        //Import zero watermark sequence information
        File files=new File("test//key.txt");
        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(files)));
        String s=reader.readLine();
        ArrayList<String> ss=new ArrayList<>();
        while (s!=null){
            ss.add(s);
            s=reader.readLine();
        }
        ArrayList<double[]>key=new ArrayList<>();                                                                       //Zero watermark sequence
        for(int i=0;i<ss.size();i++){
            String tp=ss.get(i);
            double[]temp=new double[tp.length()];
            for(int j=0;j<tp.length();j++){
                temp[j]=Integer.parseInt(String.valueOf(tp.charAt(j)));;
            }
            key.add(temp);
        }
        //Import location information
        File files2=new File("test//world.txt");
        BufferedReader reader2=new BufferedReader(new InputStreamReader(new FileInputStream(files2)));
        ArrayList<String> ss1=new ArrayList<>();
        ArrayList<String> ss2=new ArrayList<>();
        for(int i=0;i<4;i++){
            String s1=reader2.readLine();
            if(s1==null){
                break;
            }
            String s2=reader2.readLine();
            ss1.add(s1);
            ss2.add(s2);
        }
        reader2.close();
        //The submatrix is extracted from the image to be detected according to the coordinate information
        for(int i=0;i<ss1.size();i++){
            int l=0;
            double[][]B=new double[128][128];
            String s1=ss1.get(i);
            String[] sk=s1.split(" ");
            int tip=Integer.parseInt(String.valueOf(sk[0]));
            //Coordinates in the lower left corner of the matrix
            int x=(int)Double.parseDouble(String.valueOf(sk[1]));
            int y=(int)Double.parseDouble(String.valueOf(sk[2]));
            //Get the submatrix
            if(tip==0){
                for (int j = 0; j < 128; j++) {
                    for (int k = 0; k < 128; k++) {
                        int pixel = BI.getRGB(k+x, j+y);
                        B[j][k] = (pixel & 0xff) ;                                                                      //B channel pixel matrix
                    }
                }

            }else if(tip==1){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(k, BI.getHeight() - 256 + j);
                        tempx[k][256 - 1 - j] = (pixel & 0xff);
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else if(tip==2){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(BI.getWidth() - 256 + k, BI.getHeight() - 256 + j);
                        tempx[256 - j - 1][256 - 1 - k] = (pixel & 0xff) ;
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else if(tip==3){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(BI.getWidth() - 256 + k, j);
                        tempx[256 - 1 - k][j] = (pixel & 0xff) ;
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else if(tip==4){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(k+128, j+128);
                        tempx[j][k] = (pixel & 0xff) ;
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else if(tip==5){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(k+128, BI.getHeight() - 256 + j-128);
                        tempx[k][256 - 1 - j] = (pixel & 0xff);
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else if(tip==6){
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(BI.getWidth() - 256 + k-128, BI.getHeight() - 256 + j-128);
                        tempx[256 - j - 1][256 - 1 - k] = (pixel & 0xff);
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }else{
                double[][]tempx=new double[256][256];
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        int pixel = BI.getRGB(BI.getWidth() - 256 + k-128, j+128);
                        tempx[256 - 1 - k][j] = (pixel & 0xff);
                    }
                }
                for(int ii=x;ii<x+128;ii++){
                    for(int jj=y;jj<y+128;jj++){
                        B[ii-x][jj-y]=tempx[ii][jj];                                                                    //B channel pixel matrix
                    }
                }
            }
            //Extract the zero watermark sequence in the submatrix
            Watermarkextractzero watermarkextractzero=Watermarkextractzero.watermarkextractzero(B);
            double[] binaryFeature=watermarkextractzero.getBinaryFeature();                                             //Extract zero watermark information
            //Compare the extracted watermark sequence with the secret key sequence and the codebook sequence
            for(int j=0;j<key.size();j++){
                int flag=0;
                double[]temp=Xor.xor(key.get(j),binaryFeature);
                int[]end=new int[10];
                for(int k=0;k<13;k++){
                    for(int q=0;q<10;q++){
                        if(temp[q+10*k]!=array_01[q]){
                            flag++;
                            break;
                        }
                    }
                }
                if(flag<4&&l!=1){
                    System.out.println("匹配成功");
                    l=1;
                    fg=1;
                }
            }
        }
        if(fg==0){
            System.out.println("匹配失败!");
        }
    }
    public static void main(String[]s) throws IOException {
        long startTime=System.currentTimeMillis();
        Zerowatermarkexteact zerowatermarkexteact=new Zerowatermarkexteact("C:\\Users\\xd01\\Desktop\\maskedPicture\\t22.jpg");
        long endTime=System.currentTimeMillis();
        System.out.println("零水印提取时间："+(endTime-startTime)+"ms");
    }
}
