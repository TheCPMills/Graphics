import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import colors.*;
import util.*;
import java.util.*;

public class ImageProcessing {
    public static BufferedImage blend(String fileName1, String fileName2, BlendMode blendMode) throws IOException, IllegalArgumentException {
        BufferedImage base = ImageIO.read(new File(fileName1));
        BufferedImage source = ImageIO.read(new File(fileName2));
        return blend(base, source, blendMode);
    }
    
    public static BufferedImage blend(BufferedImage base, BufferedImage source, BlendMode blendMode) throws IllegalArgumentException {
        int width1 = base.getWidth();
        int width2 = source.getWidth();
        int height1 = base.getHeight();
        int height2 = source.getHeight();

        if (width1 != width2 || height1 != height2) {
            throw new IllegalArgumentException("Images must be same dimensions.");
        }

        BufferedImage img = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width1; x++) {
            for (int y = 0; y < height1; y++) {
                Colour blendedColor = blendMode.blend(Colour.fromIntColor(base.getRGB(x, y)), Colour.fromIntColor(source.getRGB(x, y)));
                img.setRGB(x, y, blendedColor.toIntColor());
            }
        }
        return img;
    }

    public static BufferedImage colorGrade(String fileName, Gradient gradientMap, BlendMode blendMode) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        return colorGrade(image, gradientMap, blendMode);
    }
    
    private static BufferedImage colorGrade(BufferedImage image, Gradient gradientMap, BlendMode blendMode) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                RGBColor pixelColor = Colour.fromIntColor(image.getRGB(x, y));
                float lightness = pixelColor.toLABColor().lightness();
                Colour mappedColor = gradientMap.getColourAtPercent(lightness);
                Colour blendedColor = blendMode.blend(pixelColor, mappedColor);
                img.setRGB(x, y, blendedColor.toIntColor());
            }
        }
        return img;
    }

    public static BufferedImage filter(String fileName, float[][] kernel, boolean normalizeKernel, boolean constrainColors) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        return filter(image, kernel, normalizeKernel, constrainColors);
    }

    public static BufferedImage filter(BufferedImage image, float[][] kernel, boolean normalizeKernel, boolean constrainColors) throws IllegalArgumentException {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // apply the kernel
                RGBColor color = applyKernel(x, y, image, kernel, normalizeKernel, constrainColors);
                img.setRGB(x, y, color.toIntColor());
            }
        }
        return img;
    }

    public static BufferedImage segment(String fileName, int columns, int rows) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        return segment(image, columns, rows);
    }

    public static BufferedImage segment(BufferedImage image, int columns, int rows) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] heightsMatrix = Segmenter.segment(height, rows);
        int[] widthsMatrix = Segmenter.segment(width, columns);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        float sRed = 0.0f;
        float sGreen = 0.0f;
        float sBlue = 0.0f;
        float alpha = 0.0f;

        for (int row = 0; row < rows; row++) {
            int y = (row == 0) ? 0 : Arrays.stream(heightsMatrix).limit(row).sum();
            for (int column = 0; column < columns; column++) {
                int x = (column == 0) ? 0 : Arrays.stream(widthsMatrix).limit(column).sum();
                for (int w = 0; w < widthsMatrix[column]; w++) {
                    for (int h = 0; h < heightsMatrix[row]; h++) {
                        int a = w + x;
                        int b = h + y;

                        RGBColor color = Colour.fromIntColor(image.getRGB(a, b));
                        sRed += color.sRed();
                        sGreen += color.sGreen();
                        sBlue += color.sBlue();
                        alpha += color.alpha();
                    }
                }

                sRed /= heightsMatrix[row] * widthsMatrix[column];
                sGreen /= heightsMatrix[row] * widthsMatrix[column];
                sBlue /= heightsMatrix[row] * widthsMatrix[column];
                alpha /= heightsMatrix[row] * widthsMatrix[column];

                for (int w = 0; w < widthsMatrix[column]; w++) {
                    for (int h = 0; h < heightsMatrix[row]; h++) {
                        int a = w + x;
                        int b = h + y;
                        img.setRGB(a, b, new RGBColor(sRed, sGreen, sBlue, alpha).toIntColor());
                    }
                }

                sRed = 0.0f;
                sGreen = 0.0f;
                sBlue = 0.0f;
                alpha = 0.0f;
            }
        }
        return img;
    }

    public static BufferedImage resize(String fileName, int targetWidth, int targetHeight) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        return resize(image, targetWidth, targetHeight);
    }

    public static BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resultingImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultingImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        g.dispose();
        return resultingImage;
    }

    private static RGBColor applyKernel(int x, int y, BufferedImage img, float[][] kernel, boolean normalizeKernel, boolean constrainColors) throws IllegalArgumentException {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int kWidth = kernel[0].length;
        int kHeight = kernel.length;

        // if the kernel is even, throw an error
        if (kWidth % 2 == 0 || kHeight % 2 == 0) {
            throw new IllegalArgumentException("Kernel must have odd dimensions");
        }

        int xCropStart = Math.max(0, (kWidth - 1) / 2 - x);
        int xCropEnd = Math.min(kWidth, imgWidth + (kWidth - 1) / 2 - x);

        int yCropStart = Math.max(0, (kHeight - 1) / 2 - y);
        int yCropEnd = Math.min(kHeight, imgHeight + (kHeight - 1) / 2 - y);

        // update starting x and y coordinates
        x -= (kWidth - 1) / 2 - xCropStart;
        y -= (kHeight - 1) / 2 - yCropStart;

        // crop the kernel
        float[][] crop = new float[yCropEnd - yCropStart][xCropEnd - xCropStart];
        float sum = 0;

        for (int ky = yCropStart; ky < yCropEnd; ky++) {
            for (int kx = xCropStart; kx < xCropEnd; kx++) {
                crop[ky - yCropStart][kx - xCropStart] = kernel[ky][kx];
                sum += kernel[kx][ky];
            }
        }

        // normalize the kernel
        if (normalizeKernel) {
            if (sum > 0) {
                for (int ky = 0; ky < crop.length; ky++) {
                    for (int kx = 0; kx < crop[ky].length; kx++) {
                        crop[ky][kx] /= sum;
                    }
                }
            }
        }

        // apply the kernel
        float sRed = 0.0f;
        float sGreen = 0.0f;
        float sBlue = 0.0f;
        float alpha = 0.0f;

        for (int ky = 0; ky < crop.length; ky++) {
            for (int kx = 0; kx < crop[ky].length; kx++) {
                int px = x + kx;
                int py = y + ky;

                if (px >= 0 && px < imgWidth && py >= 0 && py < imgHeight) {
                    RGBColor color = Colour.fromIntColor(img.getRGB(px, py));
                    sRed += color.sRed() * crop[ky][kx];
                    sGreen += color.sGreen() * crop[ky][kx];
                    sBlue += color.sBlue() * crop[ky][kx];
                    alpha += color.alpha() * crop[ky][kx];
                }
            }
        }

        if (constrainColors) {
            sRed = (float) Mathematics.clamp(sRed, 0.0f, 1.0f);
            sGreen = (float) Mathematics.clamp(sGreen, 0.0f, 1.0f);
            sBlue = (float) Mathematics.clamp(sBlue, 0.0f, 1.0f);
            alpha = (float) Mathematics.clamp(alpha, 0.0f, 1.0f);
        }

        return new RGBColor(sRed, sGreen, sBlue, alpha);
    }

}