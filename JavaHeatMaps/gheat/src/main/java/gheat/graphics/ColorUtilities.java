package gheat.graphics;




import java.awt.*;


class ColorUtilities {
    private ColorUtilities() {
    }


    public static float[] RGBtoHSL(Color color) {
        return RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), null);
    }


    public static float[] RGBtoHSL(Color color, float[] hsl) {
        return RGBtoHSL(color.getRed(), color.getGreen(), color.getBlue(), hsl);
    }


    public static float[] RGBtoHSL(int r, int g, int b) {
        return RGBtoHSL(r, g, b, null);
    }


    public static float[] RGBtoHSL(int r, int g, int b, float[] hsl) {
        if (hsl == null) {
            hsl = new float[3];
        } else if (hsl.length < 3) {
            throw new IllegalArgumentException("hsl array must have a length of" +
                    " at least 3");
        }

        if (r < 0) r = 0;
        else if (r > 255) r = 255;
        if (g < 0) g = 0;
        else if (g > 255) g = 255;
        if (b < 0) b = 0;
        else if (b > 255) b = 255;

        float var_R = (r / 255f);
        float var_G = (g / 255f);
        float var_B = (b / 255f);

        float var_Min;
        float var_Max;
        float del_Max;

        if (var_R > var_G) {
            var_Min = var_G;
            var_Max = var_R;
        } else {
            var_Min = var_R;
            var_Max = var_G;
        }
        if (var_B > var_Max) {
            var_Max = var_B;
        }
        if (var_B < var_Min) {
            var_Min = var_B;
        }

        del_Max = var_Max - var_Min;

        float H, S, L;
        L = (var_Max + var_Min) / 2f;

        if (del_Max - 0.01f <= 0.0f) {
            H = 0;
            S = 0;
        } else {
            if (L < 0.5f) {
                S = del_Max / (var_Max + var_Min);
            } else {
                S = del_Max / (2 - var_Max - var_Min);
            }

            float del_R = (((var_Max - var_R) / 6f) + (del_Max / 2f)) / del_Max;
            float del_G = (((var_Max - var_G) / 6f) + (del_Max / 2f)) / del_Max;
            float del_B = (((var_Max - var_B) / 6f) + (del_Max / 2f)) / del_Max;

            if (var_R == var_Max) {
                H = del_B - del_G;
            } else if (var_G == var_Max) {
                H = (1 / 3f) + del_R - del_B;
            } else {
                H = (2 / 3f) + del_G - del_R;
            }
            if (H < 0) {
                H += 1;
            }
            if (H > 1) {
                H -= 1;
            }
        }

        hsl[0] = H;
        hsl[1] = S;
        hsl[2] = L;

        return hsl;
    }


    public static Color HSLtoRGB(float h, float s, float l) {
        int[] rgb = HSLtoRGB(h, s, l, null);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }


    public static int[] HSLtoRGB(float h, float s, float l, int[] rgb) {
        if (rgb == null) {
            rgb = new int[3];
        } else if (rgb.length < 3) {
            throw new IllegalArgumentException("rgb array must have a length of" +
                    " at least 3");
        }

        if (h < 0) h = 0.0f;
        else if (h > 1.0f) h = 1.0f;
        if (s < 0) s = 0.0f;
        else if (s > 1.0f) s = 1.0f;
        if (l < 0) l = 0.0f;
        else if (l > 1.0f) l = 1.0f;

        int R, G, B;

        if (s - 0.01f <= 0.0f) {
            R = (int) (l * 255.0f);
            G = (int) (l * 255.0f);
            B = (int) (l * 255.0f);
        } else {
            float var_1, var_2;
            if (l < 0.5f) {
                var_2 = l * (1 + s);
            } else {
                var_2 = (l + s) - (s * l);
            }
            var_1 = 2 * l - var_2;

            R = (int) (255.0f * hue2RGB(var_1, var_2, h + (1.0f / 3.0f)));
            G = (int) (255.0f * hue2RGB(var_1, var_2, h));
            B = (int) (255.0f * hue2RGB(var_1, var_2, h - (1.0f / 3.0f)));
        }

        rgb[0] = R;
        rgb[1] = G;
        rgb[2] = B;

        return rgb;
    }

    private static float hue2RGB(float v1, float v2, float vH) {
        if (vH < 0.0f) {
            vH += 1.0f;
        }
        if (vH > 1.0f) {
            vH -= 1.0f;
        }
        if ((6.0f * vH) < 1.0f) {
            return (v1 + (v2 - v1) * 6.0f * vH);
        }
        if ((2.0f * vH) < 1.0f) {
            return (v2);
        }
        if ((3.0f * vH) < 2.0f) {
            return (v1 + (v2 - v1) * ((2.0f / 3.0f) - vH) * 6.0f);
        }
        return (v1);
    }
}