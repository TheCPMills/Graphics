package colors;

import util.*;

public abstract class Colour {
    protected final float alpha;
    public Colour(float alpha) {
        this.alpha = alpha;
    }

    public abstract RGBColor toRGBColor();
    public abstract HSLColor toHSLColor();
    public abstract LABColor toLABColor();
    public abstract CMYKColor toCMYKColor();

    public float alpha() {
        return alpha;
    }

    public static RGBColor fromIntColor(int color) {
        return new RGBColor(Integer.toHexString(color));

        // int red = (color & 0x00ff0000) >> 16;
        // int green = (color & 0x0000ff00) >> 8;
        // int blue = color & 0x000000ff;
        // int opacity = (color & 0xff000000) >> 24;

        // return new RGBColor(red / 255.0f, green / 255.0f, blue / 255.0f, opacity / 255.0f);
    }

    public int toIntColor() {
        RGBColor rgb = this.toRGBColor();

        int red = (int) Mathematics.round(rgb.sRed() * 255);
        int green = (int) Mathematics.round(rgb.sGreen() * 255);
        int blue = (int) Mathematics.round(rgb.sBlue() * 255);
        int opacity = (int) Mathematics.round(rgb.alpha() * 255);

        return (opacity << 24) + (red << 16) + (green << 8) + (blue);
    }
}