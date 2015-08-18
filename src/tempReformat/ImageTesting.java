package tempReformat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by abetts on 8/10/15.
 */
public class ImageTesting {
    static PixelImage pixelImage;
    static TempPixelImage temp;
    public static void main(String...args) throws Exception{

        BufferedImage img = ImageIO.read(new File("work.png"));

        temp = new TempPixelImage(img);
        JFrame test = new JFrame("Image Window");
        test.add(new ImgView());
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        test.setSize(img.getWidth(), img.getHeight());
        test.setVisible(true);


    }

    public static class ImgView extends JPanel{


        public void paintComponent(Graphics g){
            temp.draw(g, 0, 0);

        }

    }


}
