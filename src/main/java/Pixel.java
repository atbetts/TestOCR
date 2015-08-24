import java.awt.*;

/**
 * Created by abetts on 8/11/15.
 */
public class Pixel {
    public static final String ARGB_PIXEL = "ARGTHOLMAO";
    public static final String SOLID_COLOR = "SOLIDCOLORTHOLMAO";
    private int r = 0, g = 0, b = 0;
    private int alpha = 0xFF;

    public Pixel(int argb, String... args) {
        for (String identifier : args)
            switch (identifier) {

                case ARGB_PIXEL:
                    setAlpha(argb >> 24);
                    setRed(argb >> 16 & 0xFF);
                    setGreen(argb >> 8 & 0xFF);
                    setBlue(argb & 0xFF);
                    return;
                case SOLID_COLOR:
                    setColor(argb, argb, argb);
                    return;
                default:
                    break;

            }
    }

    public Pixel(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public Pixel(Color color) {
        this(color.getRGB(), ARGB_PIXEL);
        setAlpha(color.getAlpha());
    }

    public Pixel(int red, int green, int blue, int alpha) {
        setColor(red, green, blue, alpha);
    }

    public Pixel greyScale() {
        final int grey = (int) (getRed() * .21) + (int) (getGreen() * .72) + (int) (getBlue() * .07);
        return new Pixel(grey, grey, grey, getAlpha());
    }

    public void setColor(int red, int green, int blue) {
        setColor(red, green, blue, 255);
    }

    public void setColor(int red, int green, int blue, int alpha) {
        setRed(red);
        setBlue(blue);
        setGreen(green);
        setAlpha(alpha);
    }

    public Color getColor() {
        return new Color(r, g, b, alpha);
    }


    public void setColor(Color c) {
        setColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public boolean setAlpha(int c) {
        if (c < 0 || c > 255) {
            return false;
        }
        alpha = c;
        return true;
    }

    public int getARGB() {

        return (alpha << 24) + (r << 16) + (g << 8) + b;

    }


    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return r;
    }

    public boolean setRed(int c) {
        if (c < 0 || c > 255) {
            return false;
        }
        r = c;
        return true;
    }

    public int getGreen() {
        return g;
    }

    public boolean setGreen(int c) {
        if (c < 0 || c > 255) {
            return false;
        }
        g = c;
        return true;
    }

    public int getBlue() {
        return b;
    }

    public boolean setBlue(int c) {
        if (c < 0 || c > 255) {
            return false;
        }
        b = c;
        return true;
    }

    public String toString() {
        return String.format("Red=%d:Green=%d:Blue=%d:Alpha=%d", r, g, b, alpha);
    }


    public boolean equals(Object p) {
        Pixel pixel = (Pixel) p;

        return r == pixel.r && g == pixel.g && b == pixel.b && alpha == pixel.alpha;
    }

}
