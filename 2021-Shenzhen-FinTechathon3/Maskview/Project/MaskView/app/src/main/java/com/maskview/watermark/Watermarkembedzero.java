package com.maskview.watermark;

import java.util.ArrayList;

public class Watermarkembedzero {
    private double[] binaryFeature;

    public Watermarkembedzero(double[] a){
        this.binaryFeature=a;
    }

    public double[] getBinaryFeature() {
        return binaryFeature;
    }

    public static Watermarkembedzero watermarkembedzero(double[][] matrix){
        int bpf_cbb=8;
        int bpf_cbs=4;
        int chf_len=36;
        int bpf_num=64;
        double[] base_wm={0,1,1,0,1,0,0,0,1,0};
        int num=chf_len*2+bpf_num;
        double[] wm= Wm_gen.wm_gen(base_wm,num).getWm();
        double[][] init_code_book_chf={
                {21.3568},{73.5303},{41.6498},{134.2743},{38.0000},{32.9568},{10.5094},{80.2057},{93.2222},
                {87.0000},{29.4794},{13.8667},{59.0000},{67.0339},{119.2063},{93.5619},{76.0000},{99.5856},
                {17.9243},{51.3074},{59.8443},{66.5140},{12.0000}, {88.6172},{80.1429},{4.5120},{26.4747},
                {35.9627},{16.0000},{2.5544},{24.5306},{83.5974},{107.1321},{75.1401},{44.6483},{56.8366}};
        double[][] init_code_book_bpf={{0.741379310344828,0.155172413793103,0.431034482758621,0.224137931034483,1,0.275862068965517,0.931034482758621,0.568965517241379},
                {0.783783783783784,0.608108108108108,0.459459459459460,0.959459459459459,0.243243243243243,0.567567567567568,0.243243243243243,0},
                {0.948275862068966,0.241379310344828,0.913793103448276,0.0344827586206897,0.413793103448276,0.534482758620690,0.0517241379310345,0.827586206896552},
                {0.571428571428571,0.265306122448980,0.224489795918367,0.489795918367347,0,0.877551020408163,0.734693877551020,0.775510204081633},
                {0.791666666666667,0.333333333333333,1,1,0.958333333333333,0.291666666666667,0.750000000000000,0.854166666666667},
                {0.444444444444444,0.888888888888889,0.277777777777778,0.750000000000000,0.402777777777778,0.597222222222222,0,1},
                {0.279411764705882,0.544117647058824,0.867647058823529,0.117647058823529,0.250000000000000,0.500000000000000,0.852941176470588,0.132352941176471},
                {0.200000000000000,0.988235294117647,0.211764705882353,0.905882352941177,0.870588235294118,0.411764705882353,0.952941176470588,0.341176470588235}};
        int mr=matrix.length;
        int mc=matrix[0].length;
        double[][] Impatch=new double[mr/2][mc/2];
        for (int i=0;i<mr/2;++i){
            for (int j=0;j<mc/2;++j){
                Impatch[i][j]=(matrix[i*2][j*2]+matrix[i*2+1][j*2]+matrix[i*2][j*2+1]+matrix[i*2+1][j*2+1])/4;
            }
        }
        bitImgGen a= bitImgGen.BitImgGen(Impatch);
        GenCHF genCHF=new GenCHF(Impatch,a.getMax_quan(),a.getMin_quan(),init_code_book_chf,chf_len);
        GenBPF genBPF=new GenBPF(a.getBit_img(),init_code_book_bpf,bpf_cbb,bpf_cbs);

        ArrayList<int[][]> gen=genCHF.GenCHF1();
        double[][] bpf=genBPF.GenBPF1();

        int aa=gen.get(0).length;
        int bb=gen.get(0)[0].length;
        double[][] CHF_max=new double[aa][bb];
        double[][] CHF_min=new double[aa][bb];
        for (int i=0;i<aa;++i){
            for (int j=0;j<bb;++j){
                CHF_max[i][j]=(double) gen.get(0)[i][j];
                CHF_min[i][j]=(double) gen.get(1)[i][j];
            }
        }

        double[][] BPF=bpf;

        Genbinaryfeature genbinaryfeature= Genbinaryfeature.genbinaryFeature(CHF_max,CHF_min,BPF,bpf_num,chf_len);
        double[] feature=genbinaryfeature.getBinaryFeature();
        for (int i=0;i<feature.length;++i){
            if(feature[i]==wm[i]){
                feature[i]=1;
            }else{
                feature[i]=0;
            }
        }
        return new Watermarkembedzero(feature);
    }
}
