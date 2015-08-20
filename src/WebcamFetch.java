import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by abetts on 8/20/15.
 */
public class WebcamFetch {


    public static void main(String[] args) throws Exception {


        final PixelImage p = new PixelImage(ImageIO.read(new File("bear.jpg")));


        final JPanel comp1 = new JPanel() {
            {

            }

        };

        final JPanel comp = new JPanel() {
            {
                setPreferredSize(new Dimension(p.getMyImage().getWidth(), p.getMyImage().getHeight()));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(p.getMyImage(), 0, 0, null);
            }
        };
        comp1.add(comp);
        final JButton filter = new JButton() {{
            setText("Filter");
            addActionListener((act) -> {
                long time = System.nanoTime();
                p.applyKernel(new int[][]{{-1, 0, -1}, {0, 5, 0}, {-1, 0, -1}});
                System.out.println((System.nanoTime() - time) / 1e6 + " MilliSeconds");
                comp.repaint();
            });
        }};
        comp1.add(filter);
        final JButton filter1 = new JButton() {{
            setText("Filter1");
            addActionListener((act) -> {
                p.resetPixels();
                p.buildImage();
                comp.repaint();
            });
        }};
        comp1.add(filter1);


        JFrame f = new JFrame() {{

            add(comp1);
            pack();
            setVisible(true);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }};

        new Thread(() -> {
            long time = System.nanoTime();
            while (true) {
                try {
                    time = System.nanoTime();
                    while ((System.nanoTime() - time) / 1e6 < 10) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }).start();

    }


}
