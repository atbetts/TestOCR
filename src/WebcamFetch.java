import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by abetts on 8/20/15.
 */
public class WebcamFetch {


    public static void main(String[] args) throws Exception {

        PixelImage pixelImage = new PixelImage();

        Webcam w = Webcam.getDefault();
        w.setViewSize(Arrays.stream(w.getViewSizes()).min(new Comparator<Dimension>() {
            @Override
            public int compare(Dimension o1, Dimension o2) {
                return (int) (o1.getHeight() * o1.getWidth()) - (int) (o2.getHeight() * o2.getWidth());
            }
        }).get());
        w.open();
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage b = w.getImage();
                BufferedImageFilter.greyScale(b);
                g.drawImage(b, 0, 0, null);
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

                    while (System.nanoTime() - time < 20 * 1e6) ;
                    p.repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }).start();

    }


}
