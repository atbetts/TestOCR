import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


public class TesseractTest {
    static double MINIMUM_DESKEW_THRESHOLD = 0.05d;
        static BufferedImage bi;
    public static void main(String... args)throws Exception{
        Tesseract instance = Tesseract.getInstance();
        System.out.println("doOCR on a skewed PNG image");
        File imageFile = new File("eurotext_deskew.png");
        bi = ImageIO.read(imageFile);
        ImageDeskew id = new ImageDeskew(bi);
        double imageSkewAngle = id.getSkewAngle(); // determine skew angle
        System.out.println("imageSkewAngle = " + imageSkewAngle);
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        }
        ImagePanel i = new ImagePanel(bi);
        JFrame jf = new JFrame();
        jf.add(i);
        jf.pack();
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
        String result = instance.doOCR(bi);
        System.out.println(result);


    }
    static class ImagePanel extends JPanel{
        Image myImage;
        public ImagePanel(BufferedImage b){
            myImage = b;
        }
        public void paintComponent(Graphics graphic){
            super.paintComponent(graphic);
            graphic.drawImage(myImage,0,0,null);
        }

    }
}
