package tempReformat;

/**
 * Created by abetts on 8/11/15.
 */
public class Pixel {
    private int r, g, b;
    private int alpha = 0xFF;

    public Pixel(int argb) {
        alpha = argb >> 24;
        r = argb >> 16 & 0xFF;
        g = argb >> 8 & 0xFF;
        b = argb & 0xFF;
    }

    public Pixel(int red, int green, int blue) {
        r = red;
        b = blue;
        g = green;
    }

    public int getRed() {
        return r;
    }

    public void setRed(int c) {
        r = c;
    }

    public int getGreen() {
        return g;
    }

    public void setGreen(int c) {
        g = c;
    }

    public int getBlue() {
        return b;
    }

    public void setBlue(int c) {
        b = c;
    }

}
