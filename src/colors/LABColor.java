package colors;

import util.Mathematics;

public class LABColor extends Colour {
    private final float lightness, aRG, bYB;
    public LABColor(float lightness, float aRG, float bYB, float alpha) {
        super(alpha);
        this.lightness = lightness;
        this.aRG = aRG;
        this.bYB = bYB;
    }

    public RGBColor toRGBColor() {
        float sRed, sGreen, sBlue;

        // Convert LAB to XYZ
        float y = (lightness * 100 + 16) / 116;
        float x = aRG / 500 + y;
        float z = y - bYB / 200;

        if (Math.pow(y, 3) > 0.008856) {
            y = (float) Math.pow(y, 3);
        } else {
            y = (y - 16 / 116) / 7.787f;
        }

        if (Math.pow(x, 3) > 0.008856) {
            x = (float) Math.pow(x, 3);
        } else {
            x = (x - 16 / 116) / 7.787f;
        }

        if (Math.pow(z, 3) > 0.008856) {
            z = (float) Math.pow(z, 3);
        } else {
            z = (z - 16 / 116) / 7.787f;
        }

        x *= 0.95047f;
        y *= 1.00000f;
        z *= 1.08883f;

        // Convert XYZ to RGB
        sRed = x * 3.2406f + y * -1.5372f + z * -0.4986f;
        sGreen = x * -0.9689f + y * 1.8758f + z * 0.0415f;
        sBlue = x * 0.0557f + y * -0.2040f + z * 1.0570f;

        if (sRed > 0.0031308) {
            sRed = 1.055f * (float) Math.pow(sRed, 1 / 2.4) - 0.055f;
        } else {
            sRed = 12.92f * sRed;
        }

        if (sGreen > 0.0031308) {
            sGreen = 1.055f * (float) Math.pow(sGreen, 1 / 2.4) - 0.055f;
        } else {
            sGreen = 12.92f * sGreen;
        }

        if (sBlue > 0.0031308) {
            sBlue = 1.055f * (float) Math.pow(sBlue, 1 / 2.4) - 0.055f;
        } else {
            sBlue = 12.92f * sBlue;
        }

        sRed = (float) Mathematics.clamp(sRed, 0, 1);
        sGreen = (float) Mathematics.clamp(sGreen, 0, 1);
        sBlue = (float) Mathematics.clamp(sBlue, 0, 1);

        return new RGBColor(sRed, sGreen, sBlue, alpha);
    }

    public HSLColor toHSLColor() {
        return this.toRGBColor().toHSLColor();
    }

    public LABColor toLABColor() {
        return this;
    }

    public CMYKColor toCMYKColor() {
        return this.toRGBColor().toCMYKColor();
    }

    public String toString() {
        return String.format("lab(%.2f, %.2f, %.2f)\tOpacity: %.2f%%", lightness * 100, aRG, bYB, alpha * 100);
    }

    public float lightness() {
        return lightness;
    }

    public float a() {
        return aRG;
    }

    public float b() {
    return bYB;
    }
}