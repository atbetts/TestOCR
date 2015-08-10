import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.Buffer;

/**
 * Created by abetts on 8/7/15.
 */
public class BufferedView extends JPanel {
    BufferedImage myImage;


    public BufferedView(BufferedImage I){
        myImage = I;
        
        JFrame j = new JFrame("Grey"){{setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);setSize(500,500);setVisible(true);}};
        j.add(this);
        j.repaint();
        j.revalidate();
    }

    public BufferedImage buildPixels(int[][] pixels){
        final int width = pixels[0].length;

        final int height = pixels.length;

        BufferedImage greyImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) greyImage.getData();

        int[]buffer = new int[(width)*(height)];
        int k =0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                buffer[k++]=pixels[i][j];
            }
        }


        raster.setDataElements(0, 0, width, height, buffer);


        return new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB){{setData(raster);}};
    }


    public int[][] sobelMask(){
        int [] [] pixels = getPixels();
        int[][] gX = {
                { -1,  0,  1 },
                { -2,  0,  2 },
                { -1,  0,  1 }
        };

        int[][] gY = {
                {  1,  2,  1 },
                {  0,  0,  0 },
                { -1, -2, -1 }
        };

        int width = pixels[0].length;
        int height = pixels.length;

        width-=1;   //Trim Borders for Filter
        height-=1;

        for (int i = 0; i < height; i++) { //Rows
            for (int j = 0; j < width; j++) {
                //Cols
                int[][] localPixels = new int[3][3];
                for (int x = Math.max(i - 1, 0); x < Math.min(height, i + 2); x++) {    //Get Surrounding Pixels 3x3
                    for (int y = Math.max(j - 1, 0); y < Math.min(width, j + 2); y++) {



                    }
                }


            }
        }

        return pixels;
    }


    public int[][] greyScale(){
      int [] [] pixels = getPixels();
        final int width = pixels[0].length;

        final int height = pixels.length;

        for(int row = 0;row< height;row++){
            for(int col = 0;col< width;col++){
              final int pixel = pixels[row][col];
              // 00000000 00000000 00000000 00000000
              // alpha     red       green  blue
              // Each hex is 4 digits
              //
              int alpha = pixel >> 24;
              int red = pixel >> 16 & 0xFF;
              int green = pixel >> 8 & 0xFF;
              int blue = pixel & 0xFF ;



              final int greyValue = (int) ((.21 * red) + (.07 * blue) + (.72 * green));


              pixels[row][col] = (alpha << 24) + (greyValue << 16) + (greyValue << 8) +greyValue;


          }
      }
        return pixels;
    }


    public int [][] contrast(int [][] source){
        final int width = source[0].length;
        final int height = source.length;
        int [][] hold = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

            }
        }


        return hold;
    }
    public int[][] blur(int[][] pixels, int iter){

            final int width = pixels[0].length;
            final int height = pixels.length;
            int[][] hold = new int[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    int r, b, g;
                    r = g = b = 0;
                    int rc, gc, bc;
                    rc = gc = bc = 0;


                    for (int x = Math.max(i - 1, 0); x < Math.min(height, i + 2); x++) {
                        for (int y = Math.max(j - 1, 0); y < Math.min(width, j + 2); y++) {
                            r += pixels[x][y] >> 16 & 0xFF;
                            g += pixels[x][y] >> 8 & 0xFF;
                            b += pixels[x][y] & 0xFF;
                            rc++;
                            gc++;
                            bc++;
                        }
                    }
                    int alpha = pixels[i][j] >> 24;
                    hold[i][j] = (alpha << 24) + ((r / rc) << 16) + ((g / gc) << 8) + (b / bc);
                }
            }
        if(iter>0)
            return blur(hold,iter-1);


        return hold;
    }
    int [][]getPixels(){
        byte[] rgb =  ((DataBufferByte)myImage.getRaster().getDataBuffer()).getData();
        int[][] result = new int[myImage.getHeight()][myImage.getWidth()];

        boolean hasAlpha = myImage.getAlphaRaster()!=null;
        final int width = myImage.getWidth();
        if(hasAlpha){
            for(int pixel = 0,row = 0,col =0; pixel<rgb.length;pixel+=4){
                int argb = 0;

                final int alpha = rgb[pixel]  << 24;
                final int red = rgb[pixel + 3]  << 16;
                final int green = rgb[pixel + 2]<< 8;
                final int blue = rgb[pixel + 1] ;

                argb += alpha;  //Alpha 31-24

                argb += blue;     // Blue 0-7

                argb += green; //Green 15-8

                argb += red;//Red 23-16


                result[row][col] = argb;
                col++;

                if(col>= width){
                    col=0;
                    row++;
                }
            }
        }else{
            for(int pixel = 0,row = 0,col =0; pixel<rgb.length;pixel+=4) {
                int argb = 0;

                argb += 0xff << 24;  //255 alpha
                argb += rgb[pixel + 1] & 0xff;     // Blue 0-7
                argb += rgb[pixel + 2] & 0xff << 8; //Green 15-8
                argb += rgb[pixel + 3] & 0xff << 16;//Red 23-16



                result[row][col] = argb;
                col++;

                if (col >= width) {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }
    private int blur = 0;
    public void setBlur(int i){
        if(i<=0){
            blur = 0;
        }
        else{
            blur = i;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawImage(myImage,null,0,0);

        final BufferedImage image = buildPixels(blur(getPixels(),blur));
        AffineTransform at = new AffineTransform();
        at.scale(-1,1);

        graphics2D.transform(at);
        graphics2D.drawImage(image,null,-2*myImage.getWidth(),0);



    }
}
