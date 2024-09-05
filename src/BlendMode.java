import java.util.*;
import colors.*;
import util.*;

public enum BlendMode {
    NORMAL, DARKEN, MULTIPLY, COLOR_BURN, LINEAR_BURN, DARKER_COLOR, LIGHTEN, SCREEN, COLOR_DODGE, ADD, LIGHTER_COLOR, OVERLAY, SOFT_LIGHT, HARD_LIGHT, VIVID_LIGHT, LINEAR_LIGHT, PIN_LIGHT, HARD_MIX, DIFFERENCE, EXCLUSION, SUBTRACT, DIVIDE, HUE, SATURATION, COLOR, LUMINOSITY, AVERAGE, NEGATION, REFLECT, GLOW, CONTRAST_NEGATE, REFRACT;

    public Colour blend(Colour base, Colour source) {
        float gamma = 1.0f; // 2.2f;
        base = base.toRGBColor();
        source = source.toRGBColor();

        // APPLY INVERSE GAMMA
        float baseRed = (float) Math.pow(((RGBColor) base).sRed(), 1.0f / gamma);
        float baseGreen = (float) Math.pow(((RGBColor) base).sGreen(), 1.0f / gamma);
        float baseBlue = (float) Math.pow(((RGBColor) base).sBlue(), 1.0f / gamma);

        float sourceRed = (float) Math.pow(((RGBColor) source).sRed(), 1.0f / gamma);
        float sourceGreen = (float) Math.pow(((RGBColor) source).sGreen(), 1.0f / gamma);
        float sourceBlue = (float) Math.pow(((RGBColor) source).sBlue(), 1.0f / gamma);

        float outputRed, outputGreen, outputBlue;

        // BLEND
        switch (this) {
            default:
            case NORMAL:
                outputRed = sourceRed;
                outputGreen = sourceGreen;
                outputBlue = sourceBlue;
                break;
            case DARKEN:
                outputRed = (float) Mathematics.min(baseRed, sourceRed);
                outputGreen = (float) Mathematics.min(baseGreen, sourceGreen);
                outputBlue = (float) Mathematics.min(baseBlue, sourceBlue);
                break;
            case MULTIPLY:
                outputRed = baseRed * sourceRed;
                outputGreen = baseGreen * sourceGreen;
                outputBlue = baseBlue * sourceBlue;
                break;
            case COLOR_BURN:
                outputRed = (sourceRed == 0.0f) ? 0.0f: 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseRed) / sourceRed);
                outputGreen = (sourceGreen == 0.0f) ? 0.0f: 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseGreen) / sourceGreen);
                outputBlue = (sourceBlue == 0.0f) ? 0.0f: 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseBlue) / sourceBlue);
                break;
            case LINEAR_BURN:
                outputRed = (float) Mathematics.clamp(baseRed + sourceRed - 1.0f, 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp(baseGreen + sourceGreen - 1.0f, 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp(baseBlue + sourceBlue - 1.0f, 0.0f, 1.0f);
                break;
            case DARKER_COLOR:
                float sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                float baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;

                if (sourceLuminosity < baseLuminosity) {
                    outputRed = sourceRed;
                    outputGreen = sourceGreen;
                    outputBlue = sourceBlue;
                } else {
                    outputRed = baseRed;
                    outputGreen = baseGreen;
                    outputBlue = baseBlue;
                }
                break;
            case LIGHTEN:
                outputRed = (float) Mathematics.max(baseRed, sourceRed);
                outputGreen = (float) Mathematics.max(baseGreen, sourceGreen);
                outputBlue = (float) Mathematics.max(baseBlue, sourceBlue);
                break;
            case SCREEN:
                outputRed = 1.0f - (1.0f - baseRed) * (1.0f - sourceRed);
                outputGreen = 1.0f - (1.0f - baseGreen) * (1.0f - sourceGreen);
                outputBlue = 1.0f - (1.0f - baseBlue) * (1.0f - sourceBlue);
                break;
            case COLOR_DODGE:
                outputRed = (sourceRed == 1.0f) ? 1.0f: (float) Mathematics.min(1.0f, baseRed / (1.0f - sourceRed));
                outputGreen = (sourceGreen == 1.0f) ? 1.0f: (float) Mathematics.min(1.0f, baseGreen / (1.0f - sourceGreen));
                outputBlue = (sourceBlue == 1.0f) ? 1.0f: (float) Mathematics.min(1.0f, baseBlue / (1.0f - sourceBlue));
                break;
            case ADD:
                outputRed = (float) Mathematics.clamp(baseRed + sourceRed, 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp(baseGreen + sourceGreen, 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp(baseBlue + sourceBlue, 0.0f, 1.0f);
                break;
            case LIGHTER_COLOR:
                sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;

                if (sourceLuminosity > baseLuminosity) {
                    outputRed = sourceRed;
                    outputGreen = sourceGreen;
                    outputBlue = sourceBlue;
                } else {
                    outputRed = baseRed;
                    outputGreen = baseGreen;
                    outputBlue = baseBlue;
                }
                break;
            case OVERLAY:
                outputRed = (baseRed <= 0.5f) ? 2.0f * baseRed * sourceRed : 1.0f - (1.0f - (2.0f * baseRed - 1.0f)) * (1.0f - sourceRed);
                outputGreen = (baseGreen <= 0.5f) ? 2.0f * baseGreen * sourceGreen : 1.0f - (1.0f - (2.0f * baseGreen - 1.0f)) * (1.0f - sourceGreen);
                outputBlue = (baseBlue <= 0.5f) ? 2.0f * baseBlue * sourceBlue : 1.0f - (1.0f - (2.0f * baseBlue - 1.0f)) * (1.0f - sourceBlue);
                break;
            case SOFT_LIGHT:
                outputRed = (sourceRed <= 0.5f) ? baseRed - (1.0f - 2.0f * sourceRed) * baseRed * (1.0f - baseRed) : baseRed + (2.0f * sourceRed - 1.0f) * (((baseRed <= 0.25f) ? ((16.0f * baseRed - 12.0f) * baseRed + 4.0f) * baseRed : (float) Math.sqrt(baseRed)) - baseRed);
                outputGreen = (sourceGreen <= 0.5f) ? baseGreen - (1.0f - 2.0f * sourceGreen) * baseGreen * (1.0f - baseGreen) : baseGreen + (2.0f * sourceGreen - 1.0f) * (((baseGreen <= 0.25f) ? ((16.0f * baseGreen - 12.0f) * baseGreen + 4.0f) * baseGreen : (float) Math.sqrt(baseGreen)) - baseGreen);
                outputBlue = (sourceBlue <= 0.5f) ? baseBlue - (1.0f - 2.0f * sourceBlue) * baseBlue * (1.0f - baseBlue) : baseBlue + (2.0f * sourceBlue - 1.0f) * (((baseBlue <= 0.25f) ? ((16.0f * baseBlue - 12.0f) * baseBlue + 4.0f) * baseBlue : (float) Math.sqrt(baseBlue)) - baseBlue);
                break;
            case HARD_LIGHT:
                outputRed = (sourceRed <= 0.5f) ? 2.0f * baseRed * sourceRed : 1.0f - (1.0f - baseRed) * (1.0f - (2.0f * sourceRed - 1.0f));
                outputGreen = (sourceGreen <= 0.5f) ? 2.0f * baseGreen * sourceGreen : 1.0f - (1.0f - baseGreen) * (1.0f - (2.0f * sourceGreen - 1.0f));
                outputBlue = (sourceBlue <= 0.5f) ? 2.0f * baseBlue * sourceBlue : 1.0f - (1.0f - baseBlue) * (1.0f - (2.0f * sourceBlue - 1.0f));
                break;
            case VIVID_LIGHT:
                outputRed = (sourceRed <= 0.5f) ? 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseRed) / (2.0f * sourceRed)) : (float) Mathematics.min(1.0f, baseRed / (2.0f - 2.0f * sourceRed));
                outputGreen = (sourceGreen <= 0.5f) ? 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseGreen) / (2.0f * sourceGreen)) : (float) Mathematics.min(1.0f, baseGreen / (2.0f - 2.0f * sourceGreen));
                outputBlue = (sourceBlue <= 0.5f) ? 1.0f - (float) Mathematics.min(1.0f, (1.0f - baseBlue) / (2.0f * sourceBlue)) : (float) Mathematics.min(1.0f, baseBlue / (2.0f - 2.0f * sourceBlue));
                break;
            case LINEAR_LIGHT:
                outputRed = (float) Mathematics.clamp(baseRed + 2.0f * sourceRed - 1.0f, 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp(baseGreen + 2.0f * sourceGreen - 1.0f, 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp(baseBlue + 2.0f * sourceBlue - 1.0f, 0.0f, 1.0f);
                break;
            case PIN_LIGHT:
                outputRed = (sourceRed <= 0.5f) ? (float) Mathematics.min(baseRed, 2.0f * sourceRed) : (float) Mathematics.max(baseRed, 2.0f * sourceRed - 1.0f);
                outputGreen = (sourceGreen <= 0.5f) ? (float) Mathematics.min(baseGreen, 2.0f * sourceGreen) : (float) Mathematics.max(baseGreen, 2.0f * sourceGreen - 1.0f);
                outputBlue = (sourceBlue <= 0.5f) ? (float) Mathematics.min(baseBlue, 2.0f * sourceBlue) : (float) Mathematics.max(baseBlue, 2.0f * sourceBlue - 1.0f);
                break;
            case HARD_MIX:
                outputRed = (((sourceRed <= 0.5f) ? 1.0f - Mathematics.min(1.0f, (1.0f - baseRed) / (2.0f * sourceRed)) : Mathematics.min(1.0f, baseRed / (2.0f - 2.0f * sourceRed))) <= 0.5f) ? 0.0f : 1.0f;
                outputGreen = (((sourceGreen <= 0.5f) ? 1.0f - Mathematics.min(1.0f, (1.0f - baseGreen) / (2.0f * sourceGreen)) : Mathematics.min(1.0f, baseGreen / (2.0f - 2.0f * sourceGreen))) <= 0.5f) ? 0.0f : 1.0f;
                outputBlue = (((sourceBlue <= 0.5f) ? 1.0f - Mathematics.min(1.0f, (1.0f - baseBlue) / (2.0f * sourceBlue)) : Mathematics.min(1.0f, baseBlue / (2.0f - 2.0f * sourceBlue))) <= 0.5f) ? 0.0f : 1.0f;
                break;
            case DIFFERENCE:
                outputRed = Math.abs(baseRed - sourceRed);
                outputGreen = Math.abs(baseGreen - sourceGreen);
                outputBlue = Math.abs(baseBlue - sourceBlue);
                break;
            case EXCLUSION:
                outputRed = baseRed + sourceRed - 2.0f * baseRed * sourceRed;
                outputGreen = baseGreen + sourceGreen - 2.0f * baseGreen * sourceGreen;
                outputBlue = baseBlue + sourceBlue - 2.0f * baseBlue * sourceBlue;
                break;
            case SUBTRACT:
                outputRed = (float) Mathematics.max(0.0f, baseRed - sourceRed);
                outputGreen = (float) Mathematics.max(0.0f, baseGreen - sourceGreen);
                outputBlue = (float) Mathematics.max(0.0f, baseBlue - sourceBlue);
                break;
            case DIVIDE:
                outputRed =  (sourceRed == baseRed && sourceRed == 0.0f) ? 0.0f : (float) Mathematics.min(1.0f, baseRed / sourceRed);
                outputGreen = (sourceGreen == baseGreen && sourceGreen == 0.0f) ? 0.0f : (float) Mathematics.min(1.0f, baseGreen / sourceGreen);
                outputBlue = (sourceBlue == baseBlue && sourceBlue == 0.0f) ? 0.0f : (float) Mathematics.min(1.0f, baseBlue / sourceBlue);
                break;                
            case HUE:
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;
                float baseSaturation = (float) Mathematics.max(baseRed, baseGreen, baseBlue) - (float) Mathematics.min(baseRed, baseGreen, baseBlue);
                float sourceMin = (float) Mathematics.min(sourceRed, sourceGreen, sourceBlue);
                float sourceMax = (float) Mathematics.max(sourceRed, sourceGreen, sourceBlue);
                int minMaxConfig = Arrays.asList(sourceRed, sourceGreen, sourceBlue).indexOf(sourceMax) * 10 + Arrays.asList(sourceRed, sourceGreen, sourceBlue).indexOf(sourceMin);

                if (sourceMax > sourceMin) {
                    switch (minMaxConfig) {
                        case 1: // max is red, min is green
                            sourceBlue = ((sourceBlue - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceRed = baseSaturation;
                            sourceGreen = 0.0f;
                            break;
                        case 2: // max is red, min is blue
                            sourceGreen = ((sourceGreen - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceRed = baseSaturation;
                            sourceBlue = 0.0f;
                            break;
                        case 10: // max is green, min is red
                            sourceBlue = ((sourceBlue - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceGreen = baseSaturation;
                            sourceRed = 0.0f;
                            break;
                        case 12: // max is green, min is blue
                            sourceRed = ((sourceRed - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceGreen = baseSaturation;
                            sourceBlue = 0.0f;
                            break;
                        case 20: // max is blue, min is red
                            sourceGreen = ((sourceGreen - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceBlue = baseSaturation;
                            sourceRed = 0.0f;
                            break;
                        case 21: // max is blue, min is green
                            sourceRed = ((sourceGreen - sourceMin) * baseSaturation) / (sourceMax - sourceMin);
                            sourceBlue = baseSaturation;
                            sourceGreen = 0.0f;
                            break;
                    }
                } else {
                    sourceRed = 0.0f;
                    sourceGreen = 0.0f;
                    sourceBlue = 0.0f;
                }
                
                sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                float d = baseLuminosity - sourceLuminosity;
                outputRed = sourceRed + d;
                outputGreen = sourceGreen + d;
                outputBlue = sourceBlue + d;

                float n = (float) Mathematics.min(outputRed, outputGreen, outputBlue);
                float x = (float) Mathematics.max(outputRed, outputGreen, outputBlue);
                if (n < 0.0f) {
                    outputRed = sourceLuminosity + (((outputRed - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                    outputGreen = sourceLuminosity + (((outputGreen - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                    outputBlue = sourceLuminosity + (((outputBlue - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                }
                if (x > 1.0f) {
                    outputRed = sourceLuminosity + (((outputRed - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                    outputGreen = sourceLuminosity + (((outputGreen - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                    outputBlue = sourceLuminosity + (((outputBlue - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                }
                break;
            case SATURATION:
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;
                float sourceSaturation = (float) Mathematics.max(sourceRed, sourceGreen, sourceBlue) - (float) Mathematics.min(sourceRed, sourceGreen, sourceBlue);
                float baseMin = (float) Mathematics.min(baseRed, baseGreen, baseBlue);
                float baseMax = (float) Mathematics.max(baseRed, baseGreen, baseBlue);
                minMaxConfig = Arrays.asList(baseRed, baseGreen, baseBlue).indexOf(baseMax) * 10 + Arrays.asList(baseRed, baseGreen, baseBlue).indexOf(baseMin);

                if (baseMax > baseMin) {
                    switch (minMaxConfig) {
                        case 1: // max is red, min is green
                            baseBlue = ((baseBlue - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseRed = sourceSaturation;
                            baseGreen = 0.0f;
                            break;
                        case 2: // max is red, min is blue
                            baseGreen = ((baseGreen - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseRed = sourceSaturation;
                            baseBlue = 0.0f;
                            break;
                        case 10: // max is green, min is red
                            baseBlue = ((baseBlue - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseGreen = sourceSaturation;
                            baseRed = 0.0f;
                            break;
                        case 12: // max is green, min is blue
                            baseRed = ((baseRed - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseGreen = sourceSaturation;
                            baseBlue = 0.0f;
                            break;
                        case 20: // max is blue, min is red
                            baseGreen = ((baseGreen - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseBlue = sourceSaturation;
                            baseRed = 0.0f;
                            break;
                        case 21: // max is blue, min is green
                            baseRed = ((baseGreen - baseMin) * sourceSaturation) / (baseMax - baseMin);
                            baseBlue = sourceSaturation;
                            baseGreen = 0.0f;
                            break;
                    }
                } else {
                    baseRed = 0.0f;
                    baseGreen = 0.0f;
                    baseBlue = 0.0f;
                }

                float newBaseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;
                d = baseLuminosity - newBaseLuminosity;
                outputRed = baseRed + d;
                outputGreen = baseGreen + d;
                outputBlue = baseBlue + d;

                n = (float) Mathematics.min(outputRed, outputGreen, outputBlue);
                x = (float) Mathematics.max(outputRed, outputGreen, outputBlue);
                if (n < 0.0f) {
                    outputRed = newBaseLuminosity + (((outputRed - newBaseLuminosity) * newBaseLuminosity) / (newBaseLuminosity - n));
                    outputGreen = newBaseLuminosity + (((outputGreen - newBaseLuminosity) * newBaseLuminosity) / (newBaseLuminosity - n));
                    outputBlue = newBaseLuminosity + (((outputBlue - newBaseLuminosity) * newBaseLuminosity) / (newBaseLuminosity - n));
                }
                if (x > 1.0f) {
                    outputRed = newBaseLuminosity + (((outputRed - newBaseLuminosity) * (1.0f - newBaseLuminosity)) / (x - newBaseLuminosity));
                    outputGreen = newBaseLuminosity + (((outputGreen - newBaseLuminosity) * (1.0f - newBaseLuminosity)) / (x - newBaseLuminosity));
                    outputBlue = newBaseLuminosity + (((outputBlue - newBaseLuminosity) * (1.0f - newBaseLuminosity)) / (x - newBaseLuminosity));
                }
                break;
            case COLOR:
                sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;

                d = baseLuminosity - sourceLuminosity;
                outputRed = sourceRed + d;
                outputGreen = sourceGreen + d;
                outputBlue = sourceBlue + d;

                n = (float) Mathematics.min(outputRed, outputGreen, outputBlue);
                x = (float) Mathematics.max(outputRed, outputGreen, outputBlue);
                if (n < 0.0f) {
                    outputRed = sourceLuminosity + (((outputRed - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                    outputGreen = sourceLuminosity + (((outputGreen - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                    outputBlue = sourceLuminosity + (((outputBlue - sourceLuminosity) * sourceLuminosity) / (sourceLuminosity - n));
                }
                if (x > 1.0f) {
                    outputRed = sourceLuminosity + (((outputRed - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                    outputGreen = sourceLuminosity + (((outputGreen - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                    outputBlue = sourceLuminosity + (((outputBlue - sourceLuminosity) * (1.0f - sourceLuminosity)) / (x - sourceLuminosity));
                }
                break;
            case LUMINOSITY:
                sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;

                d = sourceLuminosity - baseLuminosity;
                outputRed = baseRed + d;
                outputGreen = baseGreen + d;
                outputBlue = baseBlue + d;

                n = (float) Mathematics.min(outputRed, outputGreen, outputBlue);
                x = (float) Mathematics.max(outputRed, outputGreen, outputBlue);
                if (n < 0.0f) {
                    outputRed = baseLuminosity + (((outputRed - baseLuminosity) * baseLuminosity) / (baseLuminosity - n));
                    outputGreen = baseLuminosity + (((outputGreen - baseLuminosity) * baseLuminosity) / (baseLuminosity - n));
                    outputBlue = baseLuminosity + (((outputBlue - baseLuminosity) * baseLuminosity) / (baseLuminosity - n));
                }
                if (x > 1.0f) {
                    outputRed = baseLuminosity + (((outputRed - baseLuminosity) * (1.0f - baseLuminosity)) / (x - baseLuminosity));
                    outputGreen = baseLuminosity + (((outputGreen - baseLuminosity) * (1.0f - baseLuminosity)) / (x - baseLuminosity));
                    outputBlue = baseLuminosity + (((outputBlue - baseLuminosity) * (1.0f - baseLuminosity)) / (x - baseLuminosity));
                }
                break;
            case AVERAGE:
                outputRed = (baseRed + sourceRed) / 2.0f;
                outputGreen = (baseGreen + sourceGreen) / 2.0f;
                outputBlue = (baseBlue + sourceBlue) / 2.0f;
                break;
            case NEGATION:
                outputRed = 1.0f - Math.abs(1.0f - baseRed - sourceRed);
                outputGreen = 1.0f - Math.abs(1.0f - baseGreen - sourceGreen);
                outputBlue = 1.0f - Math.abs(1.0f - baseBlue - sourceBlue);
                break;
            case REFLECT:
                outputRed = (float) Mathematics.clamp(baseRed * baseRed / (1.0f - sourceRed), 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp(baseGreen * baseGreen / (1.0f - sourceGreen), 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp(baseBlue * baseBlue / (1.0f - sourceBlue), 0.0f, 1.0f);
                break;
            case GLOW:
                outputRed = (float) Mathematics.clamp(sourceRed * sourceRed / (1.0f - baseRed), 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp(sourceGreen * sourceGreen / (1.0f - baseGreen), 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp(sourceBlue * sourceBlue / (1.0f - baseBlue), 0.0f, 1.0f);
                break;
            case CONTRAST_NEGATE:
                sourceLuminosity = 0.3f * sourceRed + 0.59f * sourceGreen + 0.11f * sourceBlue;
                baseLuminosity = 0.3f * baseRed + 0.59f * baseGreen + 0.11f * baseBlue;
                if (sourceLuminosity >= 0.5f) {
                    outputRed = (baseLuminosity < 0.5f) ? sourceRed : 1.0f - sourceRed;
                    outputGreen = (baseLuminosity < 0.5f) ? sourceGreen : 1.0f - sourceGreen;
                    outputBlue = (baseLuminosity < 0.5f) ? sourceBlue : 1.0f - sourceBlue;
                } else {
                    outputRed = (baseLuminosity < 0.5f) ? 1.0f - sourceRed : sourceRed;
                    outputGreen = (baseLuminosity < 0.5f) ? 1.0f - sourceGreen : sourceGreen;
                    outputBlue = (baseLuminosity < 0.5f) ? 1.0f - sourceBlue : sourceBlue;
                }
                break;
            case REFRACT:
                outputRed = (float) Mathematics.clamp((1.0f - sourceRed) / Math.sqrt(baseRed), 0.0f, 1.0f);
                outputGreen = (float) Mathematics.clamp((1.0f - sourceGreen) / Math.sqrt(baseGreen), 0.0f, 1.0f);
                outputBlue = (float) Mathematics.clamp((1.0f - sourceBlue) / Math.sqrt(baseBlue), 0.0f, 1.0f);
                break;
        }

        // COMPOSITE AND ALPHA BLENDING
        float outputAlpha = source.alpha() + base.alpha() * (1.0f - source.alpha());
        outputRed = (source.alpha() * outputRed + base.alpha() * (1.0f - source.alpha()) * baseRed) / outputAlpha;
        outputGreen = (source.alpha() * outputGreen + base.alpha() * (1.0f - source.alpha()) * baseGreen) / outputAlpha;
        outputBlue = (source.alpha() * outputBlue + base.alpha() * (1.0f - source.alpha()) * baseBlue) / outputAlpha;

        // APPLY GAMMA
        outputRed = (float) Math.pow(outputRed, gamma);
        outputGreen = (float) Math.pow(outputGreen, gamma);
        outputBlue = (float) Math.pow(outputBlue, gamma);

        return new RGBColor(outputRed, outputGreen, outputBlue, outputAlpha);
    }
}