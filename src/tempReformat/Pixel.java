package tempReformat;

import java.awt.*;

/**
 * Created by abetts on 8/11/15.
 */
public class Pixel {
    private int r, g, b;
    private int alpha = 0xFF;

    public Pixel(int argb) {
        setAlpha(alpha = argb >> 24);
        setRed(argb >> 16 & 0xFF);
        setGreen(argb >> 8 & 0xFF);
        setBlue(argb & 0xFF);
    }

    public Pixel(int red, int green, int blue) {
        setRed(red);
        setBlue(blue);
        setGreen(green);
    }

    public Pixel(int red, int green, int blue, int alpha) {
        setRed(red);
        setBlue(blue);
        setGreen(green);
        setAlpha(alpha);
    }

    public Pixel greyScale() {
        return new Pixel((int) (getRed() * .21), (int) (getGreen() * .72), (int) (getBlue() * .07), getAlpha());
    }

    public void setColor(int red, int green, int blue) {
        setRed(red);
        setBlue(blue);
        setGreen(green);
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


    public boolean setAlpha(int c) {
        if (c < 0 || c > 255) {
            return false;
        }
        alpha = c;
        return true;
    }


    public int getARGB() {

        return alpha << 24 + r << 16 + g << 8 + b;

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

}
