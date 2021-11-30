package com.media.watermark;

/*Periodic expansion of the input sequence by means of replication
 @Time 2020-04-20 14:49:01
 */
public class Wm_gen {
    private double[] wm;                                                                                                //The expanded sequence

    public double[] getWm() {
        return wm;
    }

    public Wm_gen(double[] a){
        this.wm=a;
    }
    /**
     * @param base_wm Initial input sequence
     * @param num The length of the extended sequence
     *@return  this
     */
    public static Wm_gen wm_gen(double[] base_wm,int num){
        int base_len=base_wm.length;
        int n=num/base_len;
        int m=num%base_len;
        double[] wm=new double[num];
        for (int i=0;i<n;++i){
            for (int j=0;j<base_len;++j){
                wm[i*base_len+j]=base_wm[j];
            }
        }
        for (int j=0;j<m;++j){
            wm[n*base_len+j]=base_wm[j];
        }
        return new Wm_gen(wm);
    }
}
