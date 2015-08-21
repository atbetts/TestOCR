import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;

/**
 * Created by abetts on 8/20/15.
 */
public class WebcamFetch {


    public static void main(String[] args) throws Exception {

        PixelImage pixelImage = new PixelImage();

        Webcam w = Webcam.getDefault();
        w.open();
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(pixelImage.getMyImage(), 0, 0, null);
            }
        };
        p.setPreferredSize(w.getViewSize());

        new JFrame() {{
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            add(p);
            pack();
            setVisible(true);
        }};



        new Thread(() -> {
            long time = System.nanoTime();
            while (true) {
                try {
                    time = System.nanoTime();
                    pixelImage.setImage(w.getImage());
                    FilterPixels.ApplyKernel(PixelImage.gaussianBlur.getIntMatrix(), pixelImage.getPixels());
                    pixelImage.buildImage();
                    while (System.nanoTime() - time < 20 * 1e6) ;
                    p.repaint();
                    p.revalidate();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }).start();

    }


}
