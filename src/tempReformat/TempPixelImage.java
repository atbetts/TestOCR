package tempReformat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

/**
 * Created by abetts on 8/11/15.
 */
public class TempPixelImage {


    private int width, height;
    private BufferedImage myImage;
    private Pixel[][] pixGrid;


    private Matrix gaussianBlur = new Matrix(
            new double[][]{
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}}
    );
    private Matrix sobelY = new Matrix(
            new double[][]{
                    {-1, -2, -1},
                    {0, 0, 0},
                    {1, 2, 1}}
    );
    private Matrix sobelX = new Matrix(
            new double[][]{
                    {-1, 0, 1},
                    {-2, 0, 2},
                    {-1, 0, 1}}
    );


    public TempPixelImage(int height, int width) {
        this.height = height;
        this.width = width;
        myImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setMyPixels();
    }


    public TempPixelImage(Pixel[][] pixels) {
        pixGrid = pixels;
        myImage = buildPixels(pixGrid);
        width = pixGrid[0].length;
        height = pixGrid.length;
    }


    public TempPixelImage(BufferedImage bufferedImage) {
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();
        pixGrid = new Pixel[height][width];
        setMyPixels();

    }

    public void filterPixels() {



    }

    private BufferedImage buildPixels(Pixel[][] pixels) {

        BufferedImage tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) tempImg.getData();

        int[] to1D = new int[pixels.length * pixels[0].length];
        int c = 0;

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                to1D[c++] = pixels[i][j].getARGB();
            }
        }
        raster.setDataElements(0, 0, width, height, to1D);
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB) {{
            setData(raster);
        }};
    }

    public void setPixel(int x, int y, int val) {
        if (val < 0) {
            val = 0;
        }
        if (val > 255) {
            val = 255;
        }
        pixGrid[x][y] = new Pixel(val);
    }

    public void setPixel(int x, int y, Pixel p) {
        pixGrid[x][y] = p;
    }

    private void setMyPixels() {
        boolean intArray = false;
        byte[] rgb = null;
        int[] rgbint = null;
        try {
            rgb = ((DataBufferByte) myImage.getRaster().getDataBuffer()).getData();
            System.out.println(rgb.length / (4 * width) / height);
        } catch (Exception e) {
            rgbint = ((DataBufferInt) myImage.getRaster().getDataBuffer()).getData();
            intArray = true;

        }

        if (!intArray) {


            int row, col;
            row = col = 0;
            int alpha = 0;
            int red;
            int green;
            int blue;
            boolean hasAlpha = true;
            if (myImage.getAlphaRaster() == null) {
                alpha = 0xFF;
                hasAlpha = false;
            }
            for (int i = 0; i < rgb.length; i += 4) {

                if (hasAlpha) {
                    alpha = rgb[i] & 0xFF;
                    red = rgb[i + 3] & 0xFF;
                    green = rgb[i + 2] & 0xFF;
                    blue = rgb[i + 1] & 0xFF;
                } else {

                    red = rgb[i + 3] & 0xFF;
                    green = rgb[i + 2] & 0xFF;
                    blue = rgb[i + 1] & 0xFF;

                }

                pixGrid[row][col++] = new Pixel(red, green, blue, alpha);

                if (col >= width) {
                    col = 0;
                    row++;
                }

            }
        } else {

            int row, col;
            row = col = 0;
            for (int i = 0; i < rgbint.length; i++) {
                pixGrid[row][col++] = new Pixel(rgbint[i]);

                if (col >= width) {

                    col = 0;
                    row++;
                }
            }
        }


    }

    public void draw(Graphics g, int x, int y) {
        if (pixGrid == null) {
            setMyPixels();
        }
        invert();
        g.drawImage(buildPixels(pixGrid), x, y, null);
    }


    public void invert() {
        for (int i = 0; i < pixGrid.length; i++) {
            for (int j = 0; j < pixGrid[0].length; j++) {
                int r, g, b;
                Pixel pixel = pixGrid[i][j];
                r = pixel.getRed();
                g = pixel.getGreen();
                b = pixel.getBlue();
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                pixGrid[i][j] = new Pixel(r, g, b, pixel.getAlpha());
            }
        }


    }


    public void greyScale() {
        final int rows = pixGrid.length;
        final int cols = pixGrid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                pixGrid[i][j] = pixGrid[i][j].greyScale();

            }
        }
    }

}


