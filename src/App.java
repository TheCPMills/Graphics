import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;

import org.javatuples.*;
import colors.*;

public class App {
    public static void main(String[] args) throws IOException {
        // gradient test
        Gradient gradientMap = gradientTest();

        // blend mode test
        blendModeSwatchTest(new RGBColor("FF0065AE"), new RGBColor("FFABCDEF"));

        // image blend test
        ImageIO.write(ImageProcessing.blend("res/input.jpg", "res/alt-input.jpg", BlendMode.MULTIPLY), "png", new File("res/image-blend-sample.png"));

        // color grade test
        ImageIO.write(ImageProcessing.colorGrade("res/input.jpg", gradientMap, BlendMode.SOFT_LIGHT), "png", new File("res/color-grade-sample.png"));

        // filter test
        float[][] kernel = new float[][] {
            { 0,  0,  0,  0, 0},
            { 0, -2, -1,  0, 0},
            { 0, -1,  1,  1, 0},
            { 0,  0,  1,  2, 0},
            { 0,  0,  0,  0, 0}
        };
        ImageIO.write(ImageProcessing.filter("res/alt-input.jpg", kernel, true, true), "png", new File("res/filter-sample.png"));

        // segmenting test
        ImageIO.write(ImageProcessing.segment("res/input.jpg", 20, 15), "png", new File("res/segment-sample.png"));

        // resize test
        ImageIO.write(ImageProcessing.resize("res/alt-input.jpg", 1024, 1500), "png", new File("res/resize-sample.png"));

        // convolution test
        convolutionTest();
    }


    private static Gradient gradientTest() throws IOException {
        Gradient gradient = new Gradient(Arrays.asList(
            new Pair<>(new RGBColor("FF22C1C3"), 0.0f),
            new Pair<>(new RGBColor("FFBD6060"), 0.5f),
            new Pair<>(new RGBColor("FF796FBF"), 0.1f),
            new Pair<>(new RGBColor("FFFDBB2D"), 1.0f)
        ));

        Colour[] swatch = gradient.generateSwatch(20);
        BufferedImage img = new BufferedImage(20, 1, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                img.setRGB(x, y, swatch[x].toIntColor());
            }
        }
        ImageIO.write(img, "png", new File("res/gradient-sample.png"));
        return gradient;
    }

    private static void blendModeSwatchTest(Colour base, Colour source) throws IOException {
        BufferedImage img = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
        BlendMode[] blendModes = BlendMode.values();

        for (int x = 0; x < img.getWidth() / 16; x++) {
            for (int y = 0; y < img.getHeight() / 16; y++) {
                BlendMode mode = blendModes[x + 8 * y];
                Colour blendedColor = mode.blend(base, source);

                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        img.setRGB(16 * x + i, 16 * y + j, blendedColor.toIntColor());
                    }
                }
            }
        }

        ImageIO.write(img, "png", new File("res/blend-mode-swatch-sample.png"));

    }

    private static void convolutionTest() {
        // input array
        double[] input = { 5, 4, 3, 2, 1 };

        // kernel array
        double[] kernel = { 5, 5, 5, 5, 5 };

        // input matrix
        double[][] inputMatrix = new double[][] {
                { 1, 2, 3 },
                { 8, 9, 4 },
                { 7, 6, 5 }
        };

        // kernel matrix
        double[][] kernelMatrix = new double[][] {
                { 0.0625, 0.1250, 0.0625 },
                { 0.1250, 0.2500, 0.1250 },
                { 0.0625, 0.1250, 0.0625 }
        };

        // perform 1D convolution
        Convolution.convolve(input, kernel);

        // perform 2D convolution
        Convolution.convolve(inputMatrix, kernelMatrix);
    }
}