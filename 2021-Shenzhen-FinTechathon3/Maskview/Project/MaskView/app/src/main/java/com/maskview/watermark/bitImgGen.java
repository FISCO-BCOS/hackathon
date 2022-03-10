package com.maskview.watermark;


public class bitImgGen {

    private double[][] bit_img;
    private double[][] max_quan;
    private double[][] min_quan;

    public double[][] getBit_img() {
        return this.bit_img;
    }

    public double[][] getMax_quan() {
        return max_quan;
    }

    public double[][] getMin_quan() {
        return min_quan;
    }

    public bitImgGen(double[][] a, double[][] b, double[][] c) {
        this.bit_img = a;
        this.max_quan = b;
        this.min_quan = c;
    }

    public static bitImgGen BitImgGen(double[][] matrix) {
        int m = 4;
        int n = 4;
        double w1 = 0.2716;
        int w2 = 1;
        int M = matrix.length;
        int N = matrix[0].length;

        int[][] pos = {{3, 2}, {0, 3}, {1, 1}, {2, 3},
                {1, 2}, {2, 0}, {0, 2}, {2, 2},
                {0, 0}, {2, 1}, {3, 3}, {0, 1},
                {1, 3}, {3, 0}, {1, 0}, {3, 1}};
        int[][] prority = {{8, 11, 6, 1},
                {14, 2, 4, 12},
                {5, 9, 7, 3},
                {13, 15, 0, 10}};

        int sub_m = M / m;
        int sub_n = N / n;

        double[][][] sub_img = new double[m][n][(int) (sub_m * sub_n)];
        double[][][] sub_bit = new double[m][n][(int) (sub_m * sub_n)];
        double[][] bit_img = new double[(int) (sub_m * m)][(int) (sub_n * n)];

        double[][][] t = new double[m][n][(int) (sub_m * sub_n)];
        double[][][] e = new double[m][n][(int) (sub_m * sub_n)];
        double[][][] s = new double[m][n][(int) (sub_m * sub_n)];

        double[][][] sumw = new double[m][n][(int) (sub_m * sub_n)];
        double[] avg = new double[(int) (sub_m * sub_n)];
        double[] maxv = new double[(int) (sub_m * sub_n)];
        double[] minv = new double[(int) (sub_m * sub_n)];

        int count = 1;
        for (int i = 0; i < sub_m; ++i) {
            for (int j = 0; j < sub_n; ++j) {

                double[][] tmp_img = new double[m][n];                                  //取块
                for (int k = 0; k < m; ++k) {
                    for (int l = 0; l < n; ++l) {
                        tmp_img[k][l] = matrix[i * m + k][j * n + l];
                    }
                }

                for (int k = 0; k < m; ++k) {
                    for (int l = 0; l < n; ++l) {
                        sub_img[k][l][count - 1] = tmp_img[k][l];
                    }
                }

                avg[count - 1] = operation.mean(tmp_img);
                minv[count - 1] = operation.min(tmp_img);
                maxv[count - 1] = operation.max(tmp_img);

                count++;
            }
        }

        double[][][] v = new double[m][n][(int) (sub_m * sub_n)];
        v = sub_img.clone();

        for (int i = 0; i < pos.length; ++i) {
            int x = pos[i][0];
            int y = pos[i][1];
            int pro = prority[x][y];

            double sum = 0;
            if (x > 0) {
                if (y > 0) {
                    diffuseGen a = diffuseGen.diffusegen(x, y, x - 1, y - 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                    sum = a.getSum();
                    sumw = a.getSumw();
                }
                if (y + 1 < n) {
                    diffuseGen a = diffuseGen.diffusegen(x, y, x - 1, y + 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                    sum = a.getSum();
                    sumw = a.getSumw();
                }
                diffuseGen a = diffuseGen.diffusegen(x, y, x - 1, y, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                sum = a.getSum();
                sumw = a.getSumw();
            }
            if (x + 1 < m) {
                if (y > 0) {
                    diffuseGen a = diffuseGen.diffusegen(x, y, x + 1, y - 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                    sum = a.getSum();
                    sumw = a.getSumw();
                }
                if (y + 1 < n) {
                    diffuseGen a = diffuseGen.diffusegen(x, y, x + 1, y + 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                    sum = a.getSum();
                    sumw = a.getSumw();
                }
                diffuseGen a = diffuseGen.diffusegen(x, y, x + 1, y, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                sum = a.getSum();
                sumw = a.getSumw();
            }
            if (y > 0) {
                diffuseGen a = diffuseGen.diffusegen(x, y, x, y - 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                sum = a.getSum();
                sumw = a.getSumw();
            }
            if (y + 1 < n) {
                diffuseGen a = diffuseGen.diffusegen(x, y, x, y + 1, prority, avg, maxv, minv, v, sum, sumw, w1, e, t);
                sum = a.getSum();
                sumw = a.getSumw();
            }

            for (int j = 0; j < s[0][0].length; ++j) {
                s[x][y][j] = sumw[x][y][j] / sum;
                v[x][y][j] = sub_img[x][y][j] + s[x][y][j];
            }
        }
        for (int i = 0; i < v.length; ++i) {
            for (int j = 0; j < v[0].length; ++j) {
                for (int k = 0; k < v[0][0].length; ++k) {
                    if (v[i][j][k] >= avg[k]) {
                        sub_bit[i][j][k] = 1;
                    }
                }
            }
        }

        count = 1;

        for (int i = 0; i < sub_m; ++i) {
            for (int j = 0; j < sub_n; ++j) {

                for (int k = 0; k < m; ++k) {
                    for (int l = 0; l < n; ++l) {
                        bit_img[i * m + k][j * n + l] = sub_bit[k][l][count];
                    }
                }
            }

            count++;
        }
        double[][] max_quan = new double[sub_n][sub_m];
        double[][] min_quan = new double[sub_n][sub_m];
        max_quan = reshape.transposition(reshape.reshape1(maxv, (int) sub_n, (int) sub_m));
        min_quan = reshape.transposition(reshape.reshape1(minv, (int) sub_n, (int) sub_m));
        return new bitImgGen(bit_img, max_quan, min_quan);
    }
}
