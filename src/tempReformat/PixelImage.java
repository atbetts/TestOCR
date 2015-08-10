package tempReformat;

import java.awt.image.BufferedImage;

/**
 * Created by abetts on 8/10/15.
 */
public class PixelImage {

    private int width,height;
    private BufferedImage myImage;


    public PixelImage(int height, int width){
        this.height = height;
        this.width = width;
        myImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }

    public PixelImage(BufferedImage bufferedImage){
        myImage = bufferedImage;
        width = myImage.getWidth();
        height = myImage.getHeight();

    }


}
