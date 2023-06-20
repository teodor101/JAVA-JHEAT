package gheat.graphics;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.RasterFormatException;


public final class BlendComposite implements Composite {

    public enum BlendingMode {
        AVERAGE,
        MULTIPLY,
        SCREEN,
        DARKEN,
        LIGHTEN,
        OVERLAY,
        HARD_LIGHT,
        SOFT_LIGHT,
        DIFFERENCE,
        NEGATION,
        EXCLUSION,
        COLOR_DODGE,
        INVERSE_COLOR_DODGE,
        SOFT_DODGE,
        COLOR_BURN,
        INVERSE_COLOR_BURN,
        SOFT_BURN,
        REFLECT,
        GLOW,
        FREEZE,
        HEAT,
        ADD,
        SUBTRACT,
        STAMP,
        RED,
        GREEN,
        BLUE,
        HUE,
        SATURATION,
        COLOR,
        LUMINOSITY
    }

    public static final BlendComposite Average = new BlendComposite(BlendingMode.AVERAGE);
    public static final BlendComposite Multiply = new BlendComposite(BlendingMode.MULTIPLY);
    public static final BlendComposite Screen = new BlendComposite(BlendingMode.SCREEN);
    public static final BlendComposite Darken = new BlendComposite(BlendingMode.DARKEN);
    public static final BlendComposite Lighten = new BlendComposite(BlendingMode.LIGHTEN);
    public static final BlendComposite Overlay = new BlendComposite(BlendingMode.OVERLAY);
    public static final BlendComposite HardLight = new BlendComposite(BlendingMode.HARD_LIGHT);
    public static final BlendComposite SoftLight = new BlendComposite(BlendingMode.SOFT_LIGHT);
    public static final BlendComposite Difference = new BlendComposite(BlendingMode.DIFFERENCE);
    public static final BlendComposite Negation = new BlendComposite(BlendingMode.NEGATION);
    public static final BlendComposite Exclusion = new BlendComposite(BlendingMode.EXCLUSION);
    public static final BlendComposite ColorDodge = new BlendComposite(BlendingMode.COLOR_DODGE);
    public static final BlendComposite InverseColorDodge = new BlendComposite(BlendingMode.INVERSE_COLOR_DODGE);
    public static final BlendComposite SoftDodge = new BlendComposite(BlendingMode.SOFT_DODGE);
    public static final BlendComposite ColorBurn = new BlendComposite(BlendingMode.COLOR_BURN);
    public static final BlendComposite InverseColorBurn = new BlendComposite(BlendingMode.INVERSE_COLOR_BURN);
    public static final BlendComposite SoftBurn = new BlendComposite(BlendingMode.SOFT_BURN);
    public static final BlendComposite Reflect = new BlendComposite(BlendingMode.REFLECT);
    public static final BlendComposite Glow = new BlendComposite(BlendingMode.GLOW);
    public static final BlendComposite Freeze = new BlendComposite(BlendingMode.FREEZE);
    public static final BlendComposite Heat = new BlendComposite(BlendingMode.HEAT);
    public static final BlendComposite Add = new BlendComposite(BlendingMode.ADD);
    public static final BlendComposite Subtract = new BlendComposite(BlendingMode.SUBTRACT);
    public static final BlendComposite Stamp = new BlendComposite(BlendingMode.STAMP);
    public static final BlendComposite Red = new BlendComposite(BlendingMode.RED);
    public static final BlendComposite Green = new BlendComposite(BlendingMode.GREEN);
    public static final BlendComposite Blue = new BlendComposite(BlendingMode.BLUE);
    public static final BlendComposite Hue = new BlendComposite(BlendingMode.HUE);
    public static final BlendComposite Saturation = new BlendComposite(BlendingMode.SATURATION);
    public static final BlendComposite Color = new BlendComposite(BlendingMode.COLOR);
    public static final BlendComposite Luminosity = new BlendComposite(BlendingMode.LUMINOSITY);

    private final float alpha;
    private final BlendingMode mode;

    private BlendComposite(BlendingMode mode) {
        this(mode, 1.0f);
    }

    private BlendComposite(BlendingMode mode, float alpha) {
        this.mode = mode;

        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException(
                    "alpha must be comprised between 0.0f and 1.0f");
        }
        this.alpha = alpha;
    }


    public static BlendComposite getInstance(BlendingMode mode) {
        return new BlendComposite(mode);
    }


    public static BlendComposite getInstance(BlendingMode mode, float alpha) {
        return new BlendComposite(mode, alpha);
    }


    public BlendComposite derive(BlendingMode mode) {
        return this.mode == mode ? this : new BlendComposite(mode, getAlpha());
    }


    public BlendComposite derive(float alpha) {
        return this.alpha == alpha ? this : new BlendComposite(getMode(), alpha);
    }

    public float getAlpha() {
        return alpha;
    }


    public BlendingMode getMode() {
        return mode;
    }


    @Override
    public int hashCode() {
        return Float.floatToIntBits(alpha) * 31 + mode.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlendComposite)) {
            return false;
        }

        BlendComposite bc = (BlendComposite) obj;
        return mode == bc.mode && alpha == bc.alpha;
    }

    private static boolean checkComponentsOrder(ColorModel cm) {
        if (cm instanceof DirectColorModel &&
                cm.getTransferType() == DataBuffer.TYPE_INT) {
            DirectColorModel directCM = (DirectColorModel) cm;

            return directCM.getRedMask() == 0x00FF0000 &&
                    directCM.getGreenMask() == 0x0000FF00 &&
                    directCM.getBlueMask() == 0x000000FF &&
                    (directCM.getNumComponents() != 4 ||
                            directCM.getAlphaMask() == 0xFF000000);
        }

        return false;
    }

    public CompositeContext createContext(ColorModel srcColorModel,
                                          ColorModel dstColorModel,
                                          RenderingHints hints) {
        if (!checkComponentsOrder(srcColorModel) ||
                !checkComponentsOrder(dstColorModel)) {
            throw new RasterFormatException("Incompatible color models");
        }

        return new BlendingContext(this);
    }
}