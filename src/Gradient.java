import java.util.*;
import org.javatuples.*;
import colors.*;
import util.*;

public class Gradient {
    List<Pair<Colour, Float>> stops;

    public Gradient() {
        this.stops = new SortedList<>(new Comparator<>() {
            public int compare(Pair<Colour, Float> p1, Pair<Colour, Float> p2) {
                return (int) Mathematics.signum(p1.getValue1() - p2.getValue1());
            }
        });
    }

    public Gradient(List<Pair<Colour, Float>> stops) {
        this.stops = new SortedList<>(new Comparator<>() {
            public int compare(Pair<Colour, Float> p1, Pair<Colour, Float> p2) {
                return (int) Mathematics.signum(p1.getValue1() - p2.getValue1());
            }
        });
        
        for (Pair<Colour, Float> stop : stops) {
            addStop(stop);
        }
    }

    public boolean addStop(Colour color, Float percentage) {
        return addStop(new Pair<Colour, Float>(color, percentage));
    }

    public boolean addStop(Pair<Colour, Float> stop) {
        return this.stops.add(stop);
    }

    public RGBColor[] generateSwatch(int steps) {
        RGBColor[] colors = new RGBColor[steps];
        for (int i = 0; i < steps; i++) {
            colors[i] = getColourAtPercent(i / (steps - 1.0f));
        }
        return colors;
    }

    private RGBColor interpolateGradient(RGBColor start, RGBColor end, float percent) {
        float sRed = start.sRed() * (1 - percent) + end.sRed() * percent;
        float sGreen = start.sGreen() * (1 - percent) + end.sGreen() * percent;
        float sBlue = start.sBlue() * (1 - percent) + end.sBlue() * percent;
        float alpha = start.alpha() * (1 - percent) + end.alpha() * percent;

        return new RGBColor(sRed, sGreen, sBlue, alpha);
    }

    public RGBColor getColourAtPercent(float percent) {
        // convert all stop colors to RGB
        for (Pair<Colour, Float> stop : stops) {
            stop.setAt0(stop.getValue0().toRGBColor());
        }
        
        float min = 0.0f, max;
        for (int i = 0; i < stops.size() - 1; i++) {
            min = stops.get(i).getValue1();
            max = stops.get(i+1).getValue1();

            if (Mathematics.betweenInclusive(percent, min, max)) {
                return interpolateGradient((RGBColor) stops.get(i).getValue0(), (RGBColor) stops.get(i+1).getValue0(), (percent - min) / (max - min));
            }

        }
        return null;
    }
}