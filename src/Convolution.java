import org.apache.commons.math3.complex.*;
import org.apache.commons.math3.transform.*;

public class Convolution {
    public static double[] padArray(double[] array, int length) {
        double[] paddedArray = new double[length];
        for (int i = 0; i < length; i++) {
            if (i < array.length) {
                paddedArray[i] = array[i];
            } else {
                paddedArray[i] = 0;
            }
        }
        return paddedArray;
    }

    public static double[] convolve(double[] input, double[] kernel) {
        int N = input.length; // length of the input
        int M = kernel.length; // length of the kernel
        int L = N + M - 1; // length of the convolution array
        double highestPowerOfTwo = Math.ceil(Math.log(L) / Math.log(2)); // highest power of two that is less than or equal to L
        int length = (int) Math.pow(2, highestPowerOfTwo);

        // pad the input and kernel to a length of 2^k
        double[] paddedInput = padArray(input, length);
        double[] paddedKernel = padArray(kernel, length);

        // take the convolution of the input and kernel using the fast fourier transform
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] inputFrequency = fft.transform(paddedInput, TransformType.FORWARD);

        Complex[] kernelFrequency = fft.transform(paddedKernel, TransformType.FORWARD);

        Complex[] convolvedFrequency = new Complex[kernelFrequency.length];
        for (int i = 0; i < kernelFrequency.length; i++) {
            convolvedFrequency[i] = inputFrequency[i].multiply(kernelFrequency[i]);
        }

        Complex[] convolved = fft.transform(convolvedFrequency, TransformType.INVERSE);

        double[] convolvedReal = new double[L];
        for (int i = 0; i < L; i++) {
            convolvedReal[i] = convolved[i].getReal();
        }
        return convolvedReal;
    }

    public static double[][] convolve(double[][] input, double[][] kernel) {
        int N = input.length; // height of the input
        int M = kernel.length; // height of the kernel
        int H = N + M - 1; // height of the convolution array

        int P = input[0].length; // width of the input
        int Q = kernel[0].length; // width of the kernel
        int W = P + Q - 1; // width of the convolution array

        double[][] convolved = new double[H][W];
        for (int kernelRow = M - 1; kernelRow >= 0; kernelRow--) {
            for (int inputRow = 0; inputRow < N; inputRow++) {
                double[] result = convolve(input[inputRow], kernel[kernelRow]);
                for (int i = 0; i < result.length; i++) {
                    convolved[M - 1 - kernelRow + inputRow][i] += result[i];
                }
            }
        }

        return convolved;
    }
}