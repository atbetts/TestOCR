import com.github.sarxos.webcam.Webcam;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.video.ConverterFactory;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Encoder {


    public static void main(String[] args) throws Exception {


            File file = new File("output.ts");


        BlockingQueue<BufferedImage> cache = new LinkedBlockingQueue<BufferedImage>();
            IMediaWriter writer = ToolFactory.makeWriter(file.getName());
        Dimension size = new Dimension(640, 480);

            writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);

            Webcam webcam = Webcam.getWebcams().get(1);

            webcam.setViewSize(size);
            webcam.open(true);

            long start = System.currentTimeMillis();


        Thread collect = new Thread(() -> {


            int i = 0;
            while (true) {

                i++;
                BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
//                    IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
                cache.add(image);
//                    IVideoPicture framer = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
//                    framer.setKeyFrame(i == 0);
//                    framer.setQuality(0);
//
//                    writer.encodeVideo(0, framer);

                // 60 FPS
                long time = System.nanoTime();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    while (System.nanoTime() - time < 100 * 1e6) ; //manual sleep kappa
                }
            }


        });
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build only; only English data bundled
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        instance.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_DEFAULT);
        instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_WORD);
        instance.setLanguage("eng");

        class imagePanel extends JPanel {
            Rectangle2D captureRegion;
            BufferedImage temp;

            {
                setPreferredSize(size);
                RectDraw region = new RectDraw();
                addMouseListener(region);
                addMouseMotionListener(region);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                try {
                    final BufferedImage take = cache.remove();

                    temp = take;
                    g.drawImage(take.getScaledInstance(this.getWidth(), this.getHeight(), AffineTransformOp.TYPE_BILINEAR), 0, 0, null);

                } catch (Exception e) {
                    if (temp != null) {
                        g.drawImage(temp.getScaledInstance(this.getWidth(), this.getHeight(), AffineTransformOp.TYPE_BILINEAR), 0, 0, null);
                    } else {
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());

                        Color c = g.getColor();
                        g.setColor(Color.white);
                        final String load = "Image Load Failed";
                        g.drawString(load, (int) (this.getWidth() / 2 - (getFontMetrics(getFont()).getStringBounds(load, g).getWidth()) / 2), this.getHeight() / 2);
                        g.setColor(c);
                    }
                }

                final Graphics2D g1 = (Graphics2D) g;
                g1.setStroke(new BasicStroke(5));
                if (captureRegion != null) {
                    Color c = g.getColor();
                    g.setColor(Color.red);
                    g1.draw(captureRegion);
                    g.setColor(c);

                }
            }

            public BufferedImage getImage() {
                BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                paint(bi.getGraphics());

                final BufferedImage bib = bi.getSubimage((int) captureRegion.getMinX(), (int) captureRegion.getMinY(), (int) captureRegion.getWidth(), (int) captureRegion.getHeight());
                new JFrame() {{
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    add(new JPanel() {
                        {
                            setPreferredSize(new Dimension((int) captureRegion.getWidth(), (int) captureRegion.getHeight() + 18));

                        }

                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            BufferedImageFilter.binary(bib, 93);
                            g.drawImage(bib, 0, 0, null);
                            try {
                                g.drawString(instance.doOCR(bib), 0, bib.getHeight() + 14);
                            } catch (TesseractException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    pack();
                    setVisible(true);
                    repaint();
                }};

                return bi;
            }

            class RectDraw implements MouseListener, MouseMotionListener {
                Point rect;
                Point move;

                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == 1) {
                        rect = e.getPoint();
                        System.out.println("rect = " + rect);
                    } else {
                        move = e.getPoint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == 1) {
                        captureRegion = new Rectangle();
                        captureRegion.setFrameFromDiagonal(rect, e.getPoint());
                        rect = null;
                    } else {
                        if (captureRegion != null) {
                            final double width = captureRegion.getWidth();
                            final double height = captureRegion.getHeight();
                            captureRegion = new Rectangle2D.Double(e.getPoint().getX() - width / 2, e.getPoint().getY() - height / 2, width, height);
                        }
                    }
                }


                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == 1) {
                        if (rect != null) {
                            captureRegion = new Rectangle();
                            captureRegion.setFrameFromDiagonal(rect, e.getPoint());
                        }
                    } else {
                        if (captureRegion != null) {
                            final double width = captureRegion.getWidth();
                            final double height = captureRegion.getHeight();
                            captureRegion = new Rectangle2D.Double(e.getPoint().getX() - width / 2, e.getPoint().getY() - height / 2, width, height);
                        }
                    }

                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            }
        }
        final imagePanel comp = new imagePanel();
        JFrame f = new JFrame() {{
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


            add(comp);
            pack();
            setVisible(true);
        }};


        new JFrame() {{
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            add(new JPanel() {{

                add(new JButton("Start Video") {{
                    addActionListener(

                            act -> {

                                collect.start();

                            }
                    );
                }});
                add(new JButton("Start Data Collection") {{
                    addActionListener(

                            act -> {
                                JOptionPane.showMessageDialog(null, "Please Select File to Log Data to");
                                JFileChooser jc = new JFileChooser();
                                jc.showSaveDialog(null);
                                jc.setMultiSelectionEnabled(false);
                                final File logFile = jc.getSelectedFile();
                                new Thread(() -> {
                                    try (BufferedWriter logger = new BufferedWriter(new FileWriter(logFile, true))) {
                                        logger.append();
                                    } catch (IOException io) {

                                    }
                                }).start();

                            }
                    );
                }});

            }});
            pack();
            setVisible(true);
        }};

        new Timer(16, (act) -> f.repaint()).start();


    }

    public static void printOCR(BufferedImage bufferedImage) {


    }

}