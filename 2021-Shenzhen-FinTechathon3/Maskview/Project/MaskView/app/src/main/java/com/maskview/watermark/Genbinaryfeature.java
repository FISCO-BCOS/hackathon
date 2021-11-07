package com.maskview.watermark;

public class Genbinaryfeature {
    private double[] binaryFeature;

    public Genbinaryfeature (double[] a){
        this.binaryFeature=a;
    }

    public double[] getBinaryFeature() {
        return binaryFeature;
    }

    public static Genbinaryfeature genbinaryFeature(double[][] CHF_max_all, double[][] CHF_min_all, double[][] BPF_all, int bpf_num, int chf_num){
        double[][] binary_CHF_max = new double[1][chf_num];
        double[][] binary_CHF_min = new double[1][chf_num];
        double[][] bpf = new double[1][bpf_num];
        int wm_num = bpf_num+chf_num*2;
        double[][] sum_vector=new double[CHF_max_all.length+CHF_min_all.length+BPF_all.length][1];
        for (int i=0;i<chf_num;++i){
            sum_vector[i][0]=CHF_max_all[i][0];
            sum_vector[i+chf_num][0]=CHF_max_all[i][0];
        }
        for (int i=0;i<BPF_all.length;++i){
            sum_vector[i+chf_num*2][0]=BPF_all[i][0];
        }
        double[] binary_sum_vector = new double[wm_num];
        double avg=mean(sum_vector);
        for (int i=0;i<sum_vector.length;++i){
            if(sum_vector[i][0]>avg){
                binary_sum_vector[i]=1;
            }
        }
        return new Genbinaryfeature(binary_sum_vector);
    }

    public static double mean(double[][] a){
        double sum=0;
        double avg=0;
        if (a.length==0){
            System.out.println("this matrix is null at Genbinaryfeature mean");
            return avg;
        }
        for (int i=0;i<a.length;++i){
            for (int j=0;j<a[0].length;++j){
                sum+=a[i][j];
            }
        }
        return sum/(a.length*a[0].length);
    }
}
