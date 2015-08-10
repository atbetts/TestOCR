package tempReformat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

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

    }

    private void setMyPixels(){
        byte[] rgb =  ((DataBufferByte)myImage.getRaster().getDataBuffer()).getData();

        Matrix pix = new Matrix(height,width);

        int row,col;
        row=col=0;

        for (int i = 0; i < rgb.length; i+=4) {
            int alpha=0;
            int red;
            int green;
            int blue;
            boolean hasAlpha=true;
            if(myImage.getAlphaRaster()==null){
                alpha = 0xFF;
                hasAlpha=false;
            }
            if(hasAlpha) {
                alpha = rgb[i];
                red = rgb[i+1];
                green = rgb[i+2];
                blue = rgb[i+3];
            }else{

                red = rgb[i+1];
                green = rgb[i+2];
                blue = rgb[i+3];

            }
            int rgbValue = (alpha<<24) + (red<<16) + (green <<8) + (blue);
            pix.setValue(row,col++,rgbValue);
            if(col>=width){
                col = 0;
                row++;
            }
        }

    }

    public PixelImage(BufferedImage bufferedImage){
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();

    }


}
