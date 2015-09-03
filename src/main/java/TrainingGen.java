import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

/**
 * Created by abetts on 8/25/15.
 */
public class TrainingGen {


    public static void main(String... args) throws Exception {
//        File tessdata = new File("tessdata");
//        Tesseract tess = new Tesseract();
//        tess.setDatapath(tessdata.getAbsolutePath());
//        tess.setPageSegMode(TessAPI.TessPageSegMode.PSM_SINGLE_BLOCK);
//        tess.setLanguage("spn");
//        System.out.println(tess.doOCR(new File("voltmeter.jpg")));
//        System.getenv().entrySet().parallelStream().forEach(ent->{System.out.println(ent.getKey()+" "+ent.getValue());});

        JPanel canvas = new JPanel() {
            Rectangle2D captureRegion;

            {
                RectDraw rect = new RectDraw();
                addMouseListener(rect);
                addMouseMotionListener(rect);

            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D paintBrush = (Graphics2D) g;
                paintBrush.setStroke(new BasicStroke(10));
                if (captureRegion != null)
                    paintBrush.draw(captureRegion);
            }

            class RectDraw implements MouseListener, MouseMotionListener {
                Point rect;

                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    rect = e.getPoint();
                    System.out.println("rect = " + rect);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    captureRegion = new Rectangle();
                    captureRegion.setFrameFromDiagonal(rect, e.getPoint());
                    rect = null;
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (rect != null) {
                        captureRegion = new Rectangle();
                        captureRegion.setFrameFromDiagonal(rect, e.getPoint());
                    }
                    System.out.println("moved");

                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            }
        };
        JFrame easel = new JFrame() {{
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            add(canvas);
            pack();
            setVisible(true);
        }};

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(16, 600);
                        canvas.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
