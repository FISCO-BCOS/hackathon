package com.media.watermark;

/**
 *The complex number class is defined and the constructor of the
 * complex number and the related operation methods are provided
 * @Time 2020-04-20 14:49:01
 */
public class Complex {
    private double real;                                                                                                //real part
    private double image;                                                                                               //imaginary part

    /*
     * @param
     * real            The real part of a complex number
     * image           The imaginary part of a complex number
     */
    public Complex() {
        this.real = 0;
        this.image = 0;
    }

    public Complex(double real) {
        this.real = real;
        this.image = 0;
    }

    public Complex(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public double getRe() {
        return this.real;
    }

    public double getIm() {
        return this.image;
    }

    //multynum:(a+bi)*c=(a*c)+(b*c)i
    public Complex multy(double doubleNum) {
        double real = doubleNum * this.real;
        double imag = doubleNum * this.image;
        return new Complex(real, imag);
    }

    //add：(a+bi)+(c+di)=(a+c)+(b+d)i
    public Complex add(Complex complexNum) {
        double real = complexNum.getReal();
        double image = complexNum.getImage();
        double newReal = this.real + real;
        double newImage = this.image + image;
        return new Complex(newReal, newImage);
    }

    //plus:(a+bi)+(c+di)=(a+c)+(b+d)i
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.real + b.real;
        double imag = a.image + b.image;
        return new Complex(real, imag);
    }

    //subtract：(a+bi)-(c+di)=(a-c)+(b-d)i
    public Complex sub(Complex complex) {
        double real = complex.getReal();
        double image = complex.getImage();
        double newReal = this.real - real;
        double newImage = this.image - image;
        return new Complex(newReal, newImage);
    }

    //minus：(a+bi)-(c+di)=(a-c)+(b-d)i
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.real - b.real;
        double imag = a.image - b.image;
        return new Complex(real, imag);
    }

    //multyComplex：(a+bi)(c+di)=(ac-bd)+(bc+ad)i
    public Complex mul(Complex complex) {
        double real = complex.getReal();
        double image = complex.getImage();
        double newReal = this.real * real - this.image * image;
        double newImage = this.image * real + this.real * image;
        return new Complex(newReal, newImage);
    }

    //multyDouble：a(c+di)=ac+adi
    public Complex mul(double multiplier) {
        return mul(new Complex(multiplier));
    }

    //divide：(a+bi)/(c+di)=(ac+bd)/(c^2+d^2) +((bc-ad)/(c^2+d^2))i
    public Complex div(Complex complex) {
        double real = complex.getReal();
        double image = complex.getImage();
        double denominator = real * real + image * image;
        double newReal = (this.real * real + this.image * image) / denominator;
        double newImage = (this.image * real - this.real * image) / denominator;
        return new Complex(newReal, newImage);
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImage() {
        return image;
    }

    public void setImage(double image) {
        this.image = image;
    }

    @Override
    public String toString() {
        String str = "";
        if (real != 0) {
            str += real;
        } else {
            str += "0";
        }
        if (image < 0) {
            str += image + "i";
        } else if (image > 0) {
            str += "+" + image + "i";
        }
        return str;
    }

    // e^(ix)=cosx+isinx
    public static Complex euler(double x) {
        double newReal = Math.cos(x);
        double newImage = Math.sin(x);
        return new Complex(newReal, newImage);
    }

    /**
     * Compare the equality of complex Numbers and adjust the precision value
     *
     * @param complexa
     * @param complexb
     */
    public static boolean equals(Complex complexa, Complex complexb) {
        return NumberUtils.getRound(complexa.getImage(), 4) == NumberUtils.getRound(complexb.getImage(), 4) &&
                NumberUtils.getRound(complexa.getReal(), 4) == NumberUtils.getRound(complexb.getReal(), 4);
    }

    /**
     * comparing two complex arrays for equality, the decimal precision error is in 6 digits
     *
     * @param ComMatrixa
     * @param comMatrixb
     */
    public static boolean equals(Complex[] ComMatrixa, Complex[] comMatrixb) {
        if (ComMatrixa.length != comMatrixb.length) {
            return false;
        }
        for (int i = 0; i < ComMatrixa.length; i++) {
            if (!equals(ComMatrixa[i], comMatrixb[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts a double array to a complex array
     *
     * @param doubleArray
     */
    public static Complex[] getComplexArray(double[] doubleArray) {
        int length = doubleArray.length;
        Complex[] complexArray = new Complex[length];
        for (int i = 0; i < length; i++) {
            complexArray[i] = new Complex(doubleArray[i]);
        }
        return complexArray;
    }

    /**
     * Converts a double array to a complex array
     *
     * @param doubleArray
     */
    public static Complex[][] getComplexArray(double[][] doubleArray) {
        int length = doubleArray.length;
        Complex[][] complexArray = new Complex[length][];
        for (int i = 0; i < length; i++) {
            complexArray[i] = getComplexArray(doubleArray[i]);
        }
        return complexArray;
    }


    /**
     * Two dimensional complex array transpose
     * @param complexArray Two-dimensional complex array
     */
    public static Complex[][] transform(Complex[][] complexArray) {
        int row = complexArray.length;
        int column = complexArray[0].length;
        Complex[][] result = new Complex[column][row];
        for (int i = 0; i < column; i++)
            for (int j = 0; j < row; j++)
                result[i][j] = complexArray[j][i];
        return result;
    }
}