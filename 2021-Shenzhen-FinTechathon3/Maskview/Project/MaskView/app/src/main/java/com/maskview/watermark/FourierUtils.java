package com.maskview.watermark;

public class FourierUtils {

    /**
     * 一维离散傅里叶变换DFT
     *
     * @param array 一维数组
     */
    public static Complex[] getDft(double[] array) {
        Complex[] complexArray = Complex.getComplexArray(array);
        return dftProgress(complexArray, -1);
    }

    /**
     * 一维逆离散傅里叶变换IDFT
     *
     * @param complexArray 一维复数数组
     */
    public static double[] getInverseDft(Complex[] complexArray) {
        int length = complexArray.length;
        Complex[] resultArray = dftProgress(complexArray, 1);
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = NumberUtils.getRound(resultArray[i].getReal() / length, 2);
        }
        return array;
    }

    /**
     * 一维离散傅里叶变换DFT和一维逆离散傅里叶变换IDFT计算过程
     *
     * @param array 复数数组
     * @param minus 正负值，DFT=-1，IDFT=1
     */
    private static Complex[] dftProgress(Complex[] array, int minus) {
        int length = array.length;
        Complex[] complexArray = new Complex[length];
        // minus * 2 * PI / N
        double flag = minus * 2 * Math.PI / length;
        for (int i = 0; i < length; i++) {
            Complex sum = new Complex();
            for (int j = 0; j < length; j++) {
                //array[x] * e^((minus * 2 * PI * k / N)i)
                Complex complex = Complex.euler(flag * i * j).mul(array[j]);
                sum = complex.add(sum);
            }
            //累加
            complexArray[i] = sum;
        }
        return complexArray;
    }

    /**
     * 二维离散傅里叶变换DFT
     *
     * @param arrays 二维数组
     */
    public static Complex[][] getDft(double[][] arrays) {
        Complex[][] complexArrays = Complex.getComplexArray(arrays);
        return dftProgress(complexArrays, -1);
    }

    /**
     * 二维逆离散傅里叶变换IDFT
     *
     * @param complexArrays 二维复数数组
     */
    public static double[][] getInverseDft(Complex[][] complexArrays) {
        int row = complexArrays.length;
        int column = complexArrays[0].length;
        int size = row * column;
        complexArrays = dftProgress(complexArrays, 1);
        double[][] arrays = new double[row][column];
        //每个数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                arrays[i][j] = NumberUtils.getRound(complexArrays[i][j].getReal() / size, 2);
            }
        }
        return arrays;
    }

    /**
     * 二维离散傅里叶变换DFT和二维逆离散傅里叶变换IDFT处理过程
     *
     * @param complexArrays 二维复数数组
     * @param minus         正负值，DFT=-1，IDFT=1
     */
    private static Complex[][] dftProgress(Complex[][] complexArrays, int minus) {
        int length = complexArrays.length;
        //对每行进行一维DFT
        for (int i = 0; i < length; i++) {
            complexArrays[i] = dftProgress(complexArrays[i], minus);
        }
        //倒置，即行和列互换
        complexArrays = Complex.transform(complexArrays);
        length = complexArrays.length;
        //对每行进行一维DFT，实际上是对没倒置前数组的列做一维DFT
        for (int i = 0; i < length; i++) {
            complexArrays[i] = dftProgress(complexArrays[i], minus);
        }
        //倒置回来
        complexArrays = Complex.transform(complexArrays);
        return complexArrays;
    }

    /**
     * 一维快速傅里叶变换FFT  当N不是2次幂时，自动补0
     *
     * @param array 一维数组
     */
    public static Complex[] getFft(double[] array) {
        //实际的长度
        int length = array.length;
        //调节过的长度
        int variableLength = (int) NumberUtils.getVariablePow(length, 2);
        Complex[] variableArray = new Complex[variableLength];
        for (int i = 0; i < variableLength; i++) {
            if (i < length) {
                variableArray[i] = new Complex(array[i]);
            } else {
                variableArray[i] = new Complex();
            }
        }
        return fftProgress(variableArray, -1);
    }

    /**
     * 一维逆快速傅里叶变换IFFT  将结果超过realLength的全部移除
     *
     * @param complexArray 一维复数数组
     * @param realLength   返回的数组长度
     */
    public static double[] getInverseFft(Complex[] complexArray, int realLength) {
        int length = complexArray.length;
        Complex[] resultArrays = fftProgress(complexArray, 1);
        double[] array = new double[realLength];
        //每个数都要除以N
        for (int i = 0; i < realLength; i++) {
            array[i] = NumberUtils.getRound(resultArrays[i].getReal() / length, 2);
        }
        return array;
    }

    /**
     * 一维快速傅里叶变换FFT和一维逆快速傅里叶变换IFFT递归过程
     *
     * @param complexArray 一维复数数组
     * @param minus        FFT为1，IFFT为-1
     */
    private static Complex[] fftProgress(Complex[] complexArray, int minus) {
        int length = complexArray.length;
        if (length == 2) {
            //F(0)=f(0)+f(1),F(1)=f(0)-f(1)
            return new Complex[]{
                    complexArray[0].add(complexArray[1]),
                    complexArray[0].sub(complexArray[1]),};
        } else if (length == 1) {
            return complexArray;
        }
        int middle = length / 2;
        //
        Complex[] a = new Complex[middle];//a(x)=f(2x)
        Complex[] b = new Complex[middle];//b(x)=f(2x+1)
        for (int i = 0; i < middle; i++) {
            a[i] = complexArray[2 * i];
            b[i] = complexArray[2 * i + 1];
        }
        //
        Complex[] complexesA = fftProgress(a, minus);//计算G(k)
        Complex[] complexesB = fftProgress(b, minus);//计算P(k)
        Complex[] resultArray = new Complex[length];//F(k)
        double flag = minus * 2 * Math.PI / length;//2Pi*k/N
        for (int i = 0; i < middle; i++) {
            //e^(2Pi*k/N)
            Complex complex = Complex.euler(flag * i).mul(complexesB[i]);
            //F(k)=G(k)+(e^(2Pi*k/N))*P(k)
            resultArray[i] = complexesA[i].add(complex);
            //F(k+(N/2))=G(k)+(e^(2Pi*(k+(N/2))/N))*P(k+(N/2))
            resultArray[i + middle] = complexesA[i].sub(complex);
        }
        return resultArray;
    }

    /**
     * 二维快速傅里叶变换FFT  当N不是2次幂时，自动补0
     *
     * @param arrays 二维数组
     */
    public static Complex[][] getFft(double[][] arrays) {
        //实际的行列
        int row = arrays.length;
        int column = arrays[0].length;
        //调节过的长度
        int variableLength = (int) NumberUtils.getVariablePow(row > column ? row : column, 2);
        Complex[][] complexArrays = new Complex[variableLength][variableLength];
        for (int i = 0; i < variableLength; i++) {
            for (int j = 0; j < variableLength; j++) {
                if (i < row && j < column) {
                    complexArrays[i][j] = new Complex(arrays[i][j]);
                } else {
                    complexArrays[i][j] = new Complex();
                }
            }
        }
        return fftProgress(complexArrays, -1);
    }

    /**
     * 二维逆快速傅里叶变换IFFT  将结果行列分别超过realRow和realColumn的全部移除
     *
     * @param complexArrays 二维复数数组
     */
    public static double[][] getInverseFft(Complex[][] complexArrays, int realRow, int realColumn) {
        int size = complexArrays.length * complexArrays[0].length;
        complexArrays = fftProgress(complexArrays, 1);
        double[][] arrays = new double[realRow][realColumn];
        //每个数
        for (int i = 0; i < realRow; i++) {
            for (int j = 0; j < realColumn; j++) {
                arrays[i][j] = NumberUtils.getRound(complexArrays[i][j].getReal() / size, 2);
            }
        }
        return arrays;
    }

    /**
     * 二维快速傅里叶变换DFT和二维逆快速傅里叶变换IDFT处理过程
     *
     * @param complexArrays 二维复数数组
     * @param minus         正负值，DFT=-1，IDFT=1
     */
    private static Complex[][] fftProgress(Complex[][] complexArrays, int minus) {
        int length = complexArrays.length;
        //对每行进行一维DFT
        for (int i = 0; i < length; i++) {
            complexArrays[i] = fftProgress(complexArrays[i], minus);
        }
        //倒置，即行和列互换
        complexArrays = Complex.transform(complexArrays);
        length = complexArrays.length;
        //对每行进行一维DFT，实际上是对没倒置前数组的列做一维DFT
        for (int i = 0; i < length; i++) {
            complexArrays[i] = fftProgress(complexArrays[i], minus);
        }
        //倒置回来
        complexArrays = Complex.transform(complexArrays);
        return complexArrays;
    }

    public static Complex[][] shift_to_center(Complex[][] a){
        int w = a.length;
        int h = a[0].length;

        Complex[][] x1=new Complex[h/2][w/2];
        Complex[][] x2=new Complex[h/2][w/2];
        Complex[][] x3=new Complex[h/2][w/2];
        Complex[][] x4=new Complex[h/2][w/2];

        for (int i=0;i<h/2;++i){
            for (int j=0;j<w/2;++j){
                x1[i][j]=a[i][j];
                x2[i][j]=a[i][j+w/2];
                x3[i][j]=a[i+h/2][j];
                x4[i][j]=a[i+h/2][j+w/2];
            }
        }

        for (int i=0;i<h/2;++i){
            for (int j=0;j<w/2;++j){
                a[i][j]=x4[i][j];
                a[i][j+w/2]=x3[i][j];
                a[i+h/2][j]=x2[i][j];
                a[i+h/2][j+w/2]=x1[i][j];
            }
        }
        return a;
    }

}