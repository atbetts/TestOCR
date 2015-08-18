package tempReformat;

import net.sourceforge.tess4j.Tesseract;

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
    static PixelImage temp;
    public static void main(String...args) throws Exception{

        BufferedImage img = ImageIO.read(new File("bear.jpg"));

        temp = new PixelImage(img);
        JFrame test = new JFrame("Image Window");
        test.add(new ImgView());
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        test.setSize(img.getWidth(), img.getHeight());
        test.setVisible(true);
        Tesseract t = new Tesseract();
        long time = System.nanoTime();
        final String s = t.doOCR(temp.getMyImage());
        System.out.println("Text = " + s);
        System.out.println(String.format("%.3f", (float) (System.nanoTime() - time) / 1e9));

    }

    public static class ImgView extends JPanel{


        public void paintComponent(Graphics g){
//            g.drawImage(temp.getMyImage().getScaledInstance(500, 500, BufferedImage.SCALE_DEFAULT), 0, 0, null);
            temp.draw(g, 0, 0);
        }

    }


}
