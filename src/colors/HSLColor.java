package colors;

import util.Mathematics;

public class HSLColor extends Colour {
    private final float hue, saturation, lightness;
    public HSLColor(float hue, float saturation, float lightness, float alpha) {
        super(alpha);
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    public RGBColor toRGBColor() {
        float sRed, sGreen, sBlue;

        float c = (1 - (float) Mathematics.abs(2 * lightness - 1)) * saturation;
        float x = c * (1 - (float) Mathematics.abs((hue / 60) % 2 - 1));
        float m = lightness - c / 2;

        if (Mathematics.betweenInclusiveExclusive(hue, 0, 60)) { // 
            sRed = c;
            sGreen = x;
            sBlue = 0;
        } else if (Mathematics.betweenInclusiveExclusive(hue, 60, 120)) {
            sRed = x;
            sGreen = c;
            sBlue = 0;
        } else if (Mathematics.betweenInclusiveExclusive(hue, 120, 180)) {
            sRed = 0;
            sGreen = c;
            sBlue = x;
        } else if (Mathematics.betweenInclusiveExclusive(hue, 180, 240)) {
            sRed = 0;
            sGreen = x;
            sBlue = c;
        } else if (Mathematics.betweenInclusiveExclusive(hue, 240, 300)) {
            sRed = x;
            sGreen = 0;
            sBlue = c;
        } else {
            sRed = c;
            sGreen = 0;
            sBlue = x;
        }

        return new RGBColor(sRed + m, sGreen + m, sBlue + m, alpha);
    }

    public HSLColor toHSLColor() {
        return this;
    }

    public LABColor toLABColor() {
        return this.toRGBColor().toLABColor();
    }

    public CMYKColor toCMYKColor() {
        return this.toRGBColor().toCMYKColor();
    }

    public String toString() {
        return String.format("hsl(%.2f, %.2f%%, %.2f%%)\tOpacity: %.2f%%", hue, saturation * 100, lightness * 100, alpha * 100);
    }

    public float hue() {
        return hue;
    }

    public float saturation() {
        return saturation;
    }

    public float lightness() {
        return lightness;
    }
}