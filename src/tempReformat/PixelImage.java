package tempReformat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

/**
 * Created by abetts on 8/10/15.
 */
public class PixelImage {

    private int width,height;
    private BufferedImage myImage;

    private Matrix myPixels;

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



    public PixelImage(int height, int width){
        this.height = height;
        this.width = width;
        myImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        setMyPixels();
    }

    public PixelImage(Matrix pixels){
        myPixels = pixels;
        myImage = buildPixels(pixels);
        width = pixels.getMatrix()[0].length;
        height = pixels.getMatrix().length;
    }

    public PixelImage(BufferedImage bufferedImage) {
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();

    }

    private BufferedImage buildPixels(Matrix m){
        BufferedImage tempImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) tempImg.getData();
        int[][] intMatrix = m.getIntMatrix();
        int[] to1D = new int[intMatrix.length*intMatrix[0].length];
        int c=0;
        for (int i = 0; i < intMatrix.length; i++) {
            for (int j = 0; j < intMatrix[0].length; j++) {
                to1D[c++] = intMatrix[i][j];
            }
        }
        raster.setDataElements(0, 0, width, height, to1D);
        return new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB){{setData(raster);}};
    }

    public void setPixel(int x, int y,int val){
        if(val<0){
            val = 0;
        }
        if(val>255){
            val = 255;
        }
        myPixels.setValue(x, y, val);
    }

    private void setMyPixels(){
        boolean intArray = false;
        byte[] rgb = null;
        int[] rgbint = null;
        try {
            rgb = ((DataBufferByte) myImage.getRaster().getDataBuffer()).getData();
        } catch (Exception e) {
            rgbint = ((DataBufferInt) myImage.getRaster().getDataBuffer()).getData();
            intArray = true;
        }

        if (!intArray) {
            Matrix pix = new Matrix(height, width);

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
                System.out.println("No Alpha");
            }
            for (int i = 0; i < rgb.length; i += 4) {

                if (hasAlpha) {
                    alpha = rgb[i];
                    red = rgb[i + 3];
                    green = rgb[i + 2];
                    blue = rgb[i + 1];
                } else {

                    red = rgb[i + 3];
                    green = rgb[i + 2];
                    blue = rgb[i + 1];

                }
                int rgbValue = (alpha << 24) + (red << 16) + (green << 8) + (blue);
                pix.setValue(row, col++, rgbValue);
                if (col >= width) {
                    col = 0;
                    row++;
                }
            }
            myPixels = pix;
        } else {
            Matrix pix = new Matrix(height, width);

            int row, col;
            row = col = 0;
            for (int i = 0; i < rgbint.length; i++) {

                pix.setValue(row, col++, rgbint[i]);
                if (col >= width) {
                    col = 0;
                    row++;
                }
            }
            myPixels = pix;
        }
    }

    public void draw(Graphics g,int x ,int y){
        if(myPixels==null){
            setMyPixels();
        }
        invert();

        g.drawImage(buildPixels(myPixels), x, y, null);
    }


    public void invert() {
        for (int i = 0; i < myPixels.getRows(); i++) {
            for (int j = 0; j < myPixels.getCols(); j++) {
                int r, g, b;
                int color = (int) myPixels.getValue(i, j);
                r = color >> 16 & 0xFF;
                g = color >> 8 & 0xFF;
                b = color & 0xFF;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                color = color >> 24;
                color = color << 24 + r << 16 + g << 8 + b;

                myPixels.setValue(i, j, color);
            }
        }


    }

    public BufferedImage filteredImage(Matrix m) {
        final Matrix copyPixels = new Matrix(this.myPixels);
        final int rows = copyPixels.getRows();
        final int cols = copyPixels.getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copyPixels.setValue(i, j, myPixels.getSurroundingValues(i, j).convolve(m));
            }
        }

        return buildPixels(copyPixels);
    }


    public void applyFilter(Matrix m) {
        final Matrix copyPixels = new Matrix(this.myPixels);
        final int rows = copyPixels.getRows();
        final int cols = copyPixels.getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copyPixels.setValue(i, j, myPixels.getSurroundingValues(i, j).convolve(m));
            }
        }

        myPixels = copyPixels;
    }


    public void greyScale() {

        final Matrix copyPixels = new Matrix(this.myPixels);
        final int rows = copyPixels.getRows();
        final int cols = copyPixels.getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                int r, b, g;


                int color = (int) copyPixels.getValue(i, j);
                b = color & 0xFF;
                g = color >> 8 & 0xFF;
                r = color >> 16 & 0xFF;


                int grey = (int) (.21 * r + .07 * b + .72 * g);
                int average = (0xFF << 24) + (grey << 16) + (grey << 8) + (grey);
                copyPixels.setValue(i, j, average);
            }
        }

        myImage = buildPixels(copyPixels);
        setMyPixels();
    }

}
