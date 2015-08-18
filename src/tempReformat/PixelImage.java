package tempReformat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

/**
 * Created by abetts on 8/11/15.
 */
public class PixelImage {


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


    public PixelImage(int height, int width) {
        this.height = height;
        this.width = width;
        pixGrid = new Pixel[height][width];
        myImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setMyPixels();
    }


    public PixelImage(Pixel[][] pixels) {
        pixGrid = pixels;
        myImage = buildPixels(pixGrid);
        width = pixGrid[0].length;
        height = pixGrid.length;
    }


    public PixelImage(BufferedImage bufferedImage) {
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();
        pixGrid = new Pixel[height][width];
        setMyPixels();

    }

    public void filterPixels() {



    }

    public BufferedImage getMyImage() {
        return myImage;
    }

    private BufferedImage buildPixels(Pixel[][] pixels) {

        BufferedImage tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) tempImg.getData();

        int[] to1D = new int[width * height];

        int c = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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
        pixGrid[x][y] = new Pixel(val, Pixel.ARGB_PIXEL);
    }


    public void setPixel(int x, int y, Color color) {

        pixGrid[x][y] = new Pixel(color);
    }

    public void setPixel(int x, int y, Pixel p) {
        pixGrid[x][y] = p;
    }

    private void setMyPixels() {


//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//
//                try {
//                    pixGrid[i][j] = new Pixel(myImage.getRGB(j,i));
//                } catch (Exception e) {
//                    System.out.println("i="+i+"|j="+j);
//                    System.exit(1);
//                    e.printStackTrace();
//                }
//            }
//        }


        final WritableRaster raster = myImage.getRaster();
        int numElem;
        System.out.println("Num elements: " + (numElem = raster.getNumDataElements()));
        int dataSwitch = raster.getTransferType();
        switch (dataSwitch) {
            case DataBuffer.TYPE_BYTE:
                System.out.println("DataBuffer.TYPE_BYTE");

                byte[] rgb = new byte[width * height * raster.getNumDataElements()];
                raster.getDataElements(0, 0, width, height, rgb);

                int row, col;
                row = col = 0;
                int alpha = 0;
                int red;
                int green;
                int blue;
                red = green = blue = 0;
                for (int i = 0; i < rgb.length; i += numElem) {


                    if (numElem == 3) {
                        red = rgb[i] & 0xFF;
                        green = rgb[i + 1] & 0xFF;
                        blue = rgb[i + 2] & 0xFF;
                    } else if (numElem == 4) {
                        red = rgb[i] & 0xFF;
                        green = rgb[i + 1] & 0xFF;
                        blue = rgb[i + 2] & 0xFF;
                        alpha = rgb[i + 3] & 0xFF;
                    } else if (numElem == 1) {
                        red = green = blue = 255 * rgb[i];
                    }

                    pixGrid[row][col++] = new Pixel(red, green, blue, alpha);

                    if (col >= width) {
                        col = 0;
                        row++;
                    }

                }


                break;
            case DataBuffer.TYPE_DOUBLE:
                System.out.println("DataBuffer.DOUBLE");
                break;
            case DataBuffer.TYPE_FLOAT:
                System.out.println("DataBuffer.FLOAT");
                break;
            case DataBuffer.TYPE_INT:
                System.out.println("DataBuffer.INT");
                int[] rgbint = new int[width * height * raster.getNumDataElements()];
                raster.getDataElements(0, 0, width, height, rgbint);
                int r, c;
                r = c = 0;
                for (int i = 0; i < rgbint.length; i++) {
                    pixGrid[r][c++] = new Pixel(rgbint[i]);
                    if (c >= width) {
                        c = 0;
                        r++;
                    }

                }
                break;
            case DataBuffer.TYPE_SHORT:
                System.out.println("DataBuffer.SHORT");
                break;
            case DataBuffer.TYPE_UNDEFINED:
                System.out.println("DataBuffer.UNDEFINED");
                break;
            case DataBuffer.TYPE_USHORT:
                System.out.println("DataBuffer.USHORT");
                break;

            default:
                break;
        }
        System.out.println(width + "x" + height);


        myImage = buildPixels(pixGrid);

    }

    public void draw(Graphics g, int x, int y) {
        if (pixGrid == null) {
            setMyPixels();
        }
        g.drawImage(myImage, x, y, null);
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


