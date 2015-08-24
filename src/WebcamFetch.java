import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by abetts on 8/20/15.
 */
public class WebcamFetch {


    public static void main(String[] args) throws Exception {

        PixelImage pixelImage = new PixelImage();

        Webcam w = Webcam.getDefault();
        Arrays.stream(w.getViewSizes()).forEach(System.out::println);
        w.setViewSize(new Dimension(320, 240));
        w.open();
        JSlider jS = new JSlider(-255, 0);
        JPanel p1 = new JPanel() {{
            add(jS);
        }};
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage b = w.getImage();
                BufferedImageFilter.binary(b, jS.getValue() * -1);
                g.drawImage(b, 0, 0, null);
            }
        };
        p1.add(p);
        p.setPreferredSize(w.getViewSize());

        new JFrame() {{
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            add(p1);
            pack();
            setVisible(true);
        }};



        new Thread(() -> {
            long time = System.nanoTime();
            while (true) {
                try {
                    time = System.nanoTime();

                    while (System.nanoTime() - time < 20 * 1e6) ;
                    p.repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }).start();

    }


}
