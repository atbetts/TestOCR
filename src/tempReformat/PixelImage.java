package tempReformat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * Created by abetts on 8/10/15.
 */
public class PixelImage {

    private int width,height;
    private BufferedImage myImage;
    private Matrix myPixels;

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
        raster.setDataElements(0,0,width,height, to1D);
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
        byte[] rgb =  ((DataBufferByte)myImage.getRaster().getDataBuffer()).getData();

        Matrix pix = new Matrix(height,width);

        int row,col;
        row=col=0;
        int alpha=0;
        int red;
        int green;
        int blue;
        boolean hasAlpha=true;
        if(myImage.getAlphaRaster()==null){
            alpha = 0xFF;
            hasAlpha=false;
            System.out.println("No Alpha");
        }
        for (int i = 0; i < rgb.length; i+=4) {

            if(hasAlpha) {
                alpha = rgb[i];
                red = rgb[i+3];
                green = rgb[i+2];
                blue = rgb[i+1];
            }else{

                red = rgb[i+3];
                green = rgb[i+2];
                blue = rgb[i+1];

            }
            int rgbValue = (alpha<<24) + (red<<16) + (green <<8) + (blue);
            pix.setValue(row,col++,rgbValue);
            if(col>=width){
                col = 0;
                row++;
            }
        }
        myPixels = pix;
    }

    public void draw(Graphics g,int x ,int y){
        if(myPixels==null){
            setMyPixels();
        }
        g.drawImage(greyScale(), x, y, null);
    }

    public PixelImage(BufferedImage bufferedImage){
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();

    }


    public  BufferedImage greyScale(){

        final Matrix copyPixels = new Matrix(this.myPixels);
        final int rows = copyPixels.getRows();
        for (int i = 0; i < rows; i++) {
            final int cols = copyPixels.getCols();
            Matrix m = new Matrix(3,3);
            for (int j = 0; j < cols; j++) {
                // Get neighboring pixels
                int row,col;
                row=col=0;

                for (int k = i-1; k <= i+1 ; k++) {
                    for (int l = j-1; l <= j+1; l++) {
                        //Wrap Edges
                        if(l<0){
                            l = cols +l;
                        }else if(l>= cols){
                            l = l- cols;
                        }
                        if(k<0){
                            k = rows +k;
                        }else if(k>= rows){
                            k = k- rows;
                        }

                        m.setValue(row, col++, copyPixels.getValue(k, l));
                        if(col>2){
                            col=0;
                            row++;
                        }
                    }
                }
                int r,b,g,count;
                r=b=g=count=0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        int color = (int)m.getValue(k,l);
                        r+= color & 0xFF;
                        g+= color >> 8 & 0xFF;
                        b+= color >> 16 & 0xFF;
                        count++;
                    }
                }
                r = r/count;
                b = b/count;
                g = g/count;

                int average = (0xFF<<24)+(b<<16)+(g<<8)+(r);
                copyPixels.setValue(i, j, average);
            }
        }

        return buildPixels(copyPixels);
    }

}
