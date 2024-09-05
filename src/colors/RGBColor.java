package colors;
import util.*;

public class RGBColor extends Colour {
    private final float sRed, sGreen, sBlue;
    public RGBColor(String hex) {
        this(Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16), Integer.parseInt(hex.substring(6), 16), Integer.parseInt(hex.substring(0, 2), 16) / 255.0f);
    }

    public RGBColor(String hex, float alpha) {
        this(Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4), 16), alpha);
    }

    public RGBColor(int red, int green, int blue, float alpha) {
        super(alpha);
        this.sRed = red / 255.0f;
        this.sGreen = green / 255.0f;
        this.sBlue = blue / 255.0f;
    }

    public RGBColor(float sRed, float sGreen, float sBlue, float alpha) {
        super(alpha);
        this.sRed = sRed;
        this.sGreen = sGreen;
        this.sBlue = sBlue;
    }

    public RGBColor toRGBColor() {
        return this;
    }

    public HSLColor toHSLColor() {
        float hue, saturation, lightness;

        float min = (float) Mathematics.min(sRed, sGreen, sBlue);
        float max = (float) Mathematics.max(sRed, sGreen, sBlue);
        float delta = max - min;

        lightness = (max + min) / 2.0f;

        if (delta == 0) {
            hue = 0;
            saturation = 0;
        } else {
            if (lightness < 0.5f) {
                saturation = delta / (max + min);
            } else {
                saturation = delta / (2 - max - min);
            }

            float deltaRed = (((max - sRed) / 6.0f) + (delta / 2.0f)) / delta;
            float deltaGreen = (((max - sGreen) / 6.0f) + (delta / 2.0f)) / delta;
            float deltaBlue = (((max - sBlue) / 6.0f) + (delta / 2.0f)) / delta;

            if (sRed == max) {
                hue = deltaBlue - deltaGreen;
            } else if (sGreen == max) {
                hue = (1 / 3.0f) + deltaRed - deltaBlue;
            } else {
                hue = (2 / 3.0f) + deltaGreen - deltaRed;
            }

            if (hue < 0) {
                hue += 1;
            }

            if (hue > 1) {
                hue -= 1;
            }
        }
        hue *= 360;

        return new HSLColor(hue, saturation, lightness, alpha);
    }

    public LABColor toLABColor() {
        float lightness, aRG, bYB;

        // Convert RGB to XYZ
        float sR = sRed;
        float sG = sGreen;
        float sB = sBlue;

        if (sR > 0.04045) {
            sR = (float) Math.pow((sR + 0.055) / 1.055, 2.4);
        } else {
            sR = sR / 12.92f;
        }

        if (sG > 0.04045) {
            sG = (float) Math.pow((sG + 0.055) / 1.055, 2.4);
        } else {
            sG = sG / 12.92f;
        }

        if (sB > 0.04045) {
            sB = (float) Math.pow((sB + 0.055) / 1.055, 2.4);
        } else {
            sB = sB / 12.92f;
        }

        sR *= 100;
        sG *= 100;
        sB *= 100;

        float x = sR * 0.4124f + sG * 0.3576f + sB * 0.1805f;
        float y = sR * 0.2126f + sG * 0.7152f + sB * 0.0722f;
        float z = sR * 0.0193f + sG * 0.1192f + sB * 0.9505f;

        // Convert XYZ to LAB
        x /= 95.047f;
        y /= 100.000f;
        z /= 108.883f;

        if (x > 0.008856) {
            x = (float) Math.pow(x, 1 / 3.0);
        } else {
            x = (7.787f * x) + (16 / 116.0f);
        }

        if (y > 0.008856) {
            y = (float) Math.pow(y, 1 / 3.0);
        } else {
            y = (7.787f * y) + (16 / 116.0f);
        }

        if (z > 0.008856) {
            z = (float) Math.pow(z, 1 / 3.0);
        } else {
            z = (7.787f * z) + (16 / 116.0f);
        }

        lightness = (116 * y) - 16;
        aRG = 500 * (x - y);
        bYB = 200 * (y - z);

        return new LABColor(lightness / 100.0f, aRG, bYB, alpha);
    }

    public CMYKColor toCMYKColor() {
        float cyan, magenta, yellow;
        float black = 1.0f - (float) Mathematics.max(sRed, sGreen, sBlue);

        cyan = (1 - sRed - black) / (1 - black);
        magenta = (1 - sGreen - black) / (1 - black);
        yellow = (1 - sBlue - black) / (1 - black);

        return new CMYKColor(cyan, magenta, yellow, black, alpha);
    }

    public String toString() {
        return String.format("rgb(%d, %d, %d)\tOpacity: %.2f%%", Mathematics.round(sRed * 255), Mathematics.round(sGreen * 255), Mathematics.round(sBlue * 255), alpha * 100);
    }

    public float sRed() {
        return sRed;
    }

    public float sGreen() {
        return sGreen;
    }

    public float sBlue() {
        return sBlue;
    }
}