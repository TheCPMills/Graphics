package colors;

public class CMYKColor extends Colour {
    private final float cyan, magenta, yellow, black;
    public CMYKColor(float cyan, float magenta, float yellow, float black, float alpha) {
        super(alpha);
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.black = black;
    }

    public RGBColor toRGBColor() {
        float sRed, sGreen, sBlue;

        sRed = (1 - cyan) * (1 - black);
        sGreen = (1 - magenta) * (1 - black);
        sBlue = (1 - yellow) * (1 - black);

        return new RGBColor(sRed, sGreen, sBlue, alpha);
    }

    public HSLColor toHSLColor() {
        return this.toRGBColor().toHSLColor();
    }

    public LABColor toLABColor() {
        return this.toRGBColor().toLABColor();
    }

    public CMYKColor toCMYKColor() {
        return this;
    }

    public String toString() {
        return String.format("cmyk(%.2f%%, %.2f%%, %.2f%%, %.2f%%)\tOpacity: %.2f%%", cyan * 100, magenta * 100, yellow * 100, black * 100, alpha * 100);
    }

    public float cyan() {
        return cyan;
    }

    public float magenta() {
        return magenta;
    }

    public float yellow() {
        return yellow;
    }

    public float black() {
        return black;
    }
}
