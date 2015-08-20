import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;

/**
 * Created by abetts on 8/20/15.
 */
public class WebcamFetch {

    static BufferedImage img;

    public static void main(String[] args) throws Exception {

        img = ImageIO.read(new File("bear.png"));

        float[][] kernel = new float[][]{
                {1, 2, 1}
                , {0, 1, 0}
                , {-1, -2, -1}
        };


        Kernel k = new Kernel(kernel.length, kernel[0].length, convert1D(kernel));
        int edge = ConvolveOp.EDGE_NO_OP;
        ConvolveOp convolveOp = new ConvolveOp(k, edge, null);
        img = convolveOp.filter(img, null);


        JPanel editPanel = new JPanel() {
            {
                setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, null);
            }
        };
        JFrame jFrame = new JFrame() {{
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            add(editPanel);
            pack();
            setVisible(true);
        }};


    }

    public static float[] convert1D(float[][] k) {
        float[] d1d = new float[k.length * k[0].length];
        int r, c;
        r = c = 0;
        for (int i = 0; i < d1d.length; i++) {

            d1d[i] = k[r][c++];
            if (c >= k[0].length) {
                c = 0;
                r++;
            }

        }

        return d1d;
    }


}
