package com.media.watermark;

public class FourierUtils {
    /**
     * One dimensional discrete Fourier transform DFT
     * @param array linear array
     */
    public static Complex[] getDft(double[] array) {
        Complex[] complexArray = Complex.getComplexArray(array);
        return dftProgress(complexArray, -1);
    }

    /**
     * One dimensional inverse discrete Fourier transform IDFT
     *
     * @param complexArray One-dimensional complex array
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
     * One-dimensional discrete Fourier transform DFT and one-dimensional
     * inverse discrete Fourier transform IDFT
     *
     * @param array The plural arra
     * @param minus positive and negative values，DFT=-1，IDFT=1
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
            //accumulation
            complexArray[i] = sum;
        }
        return complexArray;
    }

    /**
     * The two-dimensional discrete Fourier transform DFT
     *
     * @param arrays two-dimensional array
     */
    public static Complex[][] getDft(double[][] arrays) {
        Complex[][] complexArrays = Complex.getComplexArray(arrays);
        return dftProgress(complexArrays, -1);
    }

    /**
     * The two-dimensional inverse discrete Fourier transform IDFT
     *
     * @param complexArrays Two-dimensional complex array
     */
    public static double[][] getInverseDft(Complex[][] complexArrays) {
        int row = complexArrays.length;
        int column = complexArrays[0].length;
        int size = row * column;
        complexArrays = dftProgress(complexArrays, 1);
        double[][] arrays = new double[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                arrays[i][j] = NumberUtils.getRound(complexArrays[i][j].getReal() / size, 2);
            }
        }
        return arrays;
    }

    /**
     * Two dimensional discrete Fourier transform DFT and two
     * dimensional inverse discrete Fourier transform IDFT
     *
     * @param complexArrays Two-dimensional complex array
     * @param minus         positive and negative values，DFT=-1，IDFT=1
     */
    private static Complex[][] dftProgress(Complex[][] complexArrays, int minus) {
        int length = complexArrays.length;
        //One dimensional DFT for each row
        for (int i = 0; i < length; i++) {
            complexArrays[i] = dftProgress(complexArrays[i], minus);
        }
        //transposition
        complexArrays = Complex.transform(complexArrays);
        length = complexArrays.length;
        //One dimensional DFT for each row
        for (int i = 0; i < length; i++) {
            complexArrays[i] = dftProgress(complexArrays[i], minus);
        }
        //transposition
        complexArrays = Complex.transform(complexArrays);
        return complexArrays;
    }

    /**
     * One dimensional fast Fourier transform FFT
     *
     * @param array linear array
     */
    public static Complex[] getFft(double[] array) {
        //Actual length
        int length = array.length;
        //Adjusted length
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
     * One dimensional inverse fast Fourier transform IFFT
     *
     * @param complexArray One-dimensional complex array
     * @param realLength   The length of the array returned
     */
    public static double[] getInverseFft(Complex[] complexArray, int realLength) {
        int length = complexArray.length;
        Complex[] resultArrays = fftProgress(complexArray, 1);
        double[] array = new double[realLength];
        //divide everything by N
        for (int i = 0; i < realLength; i++) {
            array[i] = NumberUtils.getRound(resultArrays[i].getReal() / length, 2);
        }
        return array;
    }

    /**
     * One dimensional fast Fourier transform FFT and one dimensional inverse fast Fourier transform IFFT recursion
     *
     * @param complexArray One-dimensional complex array
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
        Complex[] complexesA = fftProgress(a, minus);//calculateG(k)
        Complex[] complexesB = fftProgress(b, minus);//calculateP(k)
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
     * Two-dimensional fast Fourier transform FFT
     *
     * @param arrays two-dimensional array
     */
    public static Complex[][] getFft(double[][] arrays) {
        //Actual column and column
        int row = arrays.length;
        int column = arrays[0].length;
        //Adjusted length
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
     * The two-dimensional inverse fast Fourier transform IFFT
     *
     * @param complexArrays Two-dimensional complex array
     */
    public static double[][] getInverseFft(Complex[][] complexArrays, int realRow, int realColumn) {
        int size = complexArrays.length * complexArrays[0].length;
        complexArrays = fftProgress(complexArrays, 1);
        double[][] arrays = new double[realRow][realColumn];
        for (int i = 0; i < realRow; i++) {
            for (int j = 0; j < realColumn; j++) {
                arrays[i][j] = NumberUtils.getRound(complexArrays[i][j].getReal() / size, 2);
            }
        }
        return arrays;
    }

    /**
     * Two dimensional fast Fourier transform DFT and two dimensional inverse fast Fourier transform IDFT
     *
     * @param complexArrays Two-dimensional complex array
     * @param minus         positive and negative values，DFT=-1，IDFT=1
     */
    private static Complex[][] fftProgress(Complex[][] complexArrays, int minus) {
        int length = complexArrays.length;
        //One dimensional DFT for each row
        for (int i = 0; i < length; i++) {
            complexArrays[i] = fftProgress(complexArrays[i], minus);
        }
        //transposition
        complexArrays = Complex.transform(complexArrays);
        length = complexArrays.length;
        //One dimensional DFT for each row
        for (int i = 0; i < length; i++) {
            complexArrays[i] = fftProgress(complexArrays[i], minus);
        }
        //transposition
        complexArrays = Complex.transform(complexArrays);
        return complexArrays;
    }

    /**
     * centralize the matrix
     *
     * @param a  Two-dimensional complex array
     */
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