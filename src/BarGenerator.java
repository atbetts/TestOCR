
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.recognition.software.jdeskew.ImageDeskew;
import com.sun.java_cup.internal.runtime.Scanner;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

/**
 * Created by abetts on 8/6/15.
 */
public class BarGenerator {
    JFrame barWin;
    double barPercent =.5d;
    int barWidth;
    int barHeight;
    double barMax;

    public static void main(String... args){
        new BarGenerator();
    }


    private class DrawPanel extends JPanel{


        public DrawPanel(){

        }

        @Override
        public void paintComponent(Graphics graphics){

            Graphics2D g2D = (Graphics2D) graphics;
            g2D.getFont().deriveFont(Font.BOLD);
            int width = this.getWidth();
            int height = this.getHeight();
            int x = width/2-barWidth/2;
            int y = height-barHeight;
            g2D.drawRect(x,y,barWidth,barHeight);


            final int half =  (int) (barHeight * barPercent);
            y-=half;
            y+=barHeight;
            g2D.fillRect(x,y,barWidth, (int)(barHeight*barPercent));
            String value = String.format("Current Value: %.3f",barPercent*barMax);
            int msgWidth = (int)g2D.getFontMetrics().getStringBounds(value,g2D).getWidth();
            x-=msgWidth;

            g2D.drawString(value,x,y);

        }

    }

    public BarGenerator(){
        init();

    }
    int blur = 0;

    private void init(){

         barPercent =.5d;
         barWidth=100;
         barHeight=500;
         barMax=100;
        final DrawPanel drawPanel = new DrawPanel();
        JPanel p  = new JPanel(){{add(new JSlider(){{setMaximum(100);setMinimum(0);addChangeListener(act -> {
            barPercent = ((double) getValue()) / 100;
            drawPanel.revalidate();
            drawPanel.repaint();
            blur = getValue();

        });}});add(new JButton("Process Image"){{addActionListener(act -> processImage(true));}});add(new JButton("Process Camera"){{addActionListener(act -> processImage(false));}});}};

        barWin = new JFrame("OCR"){{setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            add(drawPanel);pack();setVisible(true);}};


        new JFrame("Bar"){{setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            add(p);pack();setVisible(true);}};



    }
    private void processImage(boolean frame){

        Tesseract instance = new Tesseract();
        instance.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_CUBE_ONLY);

        instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_BLOCK);
        File imageFile = new File("eurotext_deskew.png");
        BufferedImage bI = null;
        if(frame)
            try {
                bI = ImageIO.read(new File("mount.png"));
            } catch (IOException e) {
                bI = getImage();
            }
        else{
           Webcam w = Webcam.getDefault();
            w.setViewSize(new Dimension(640, 480));
            w.open();
            new JFrame(){{setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                final WebcamPanel webcamPanel = new ImagePane(w);
                webcamPanel.setFPSLimited(false);
                add(webcamPanel);pack();setVisible(true);}};
                JOptionPane.showConfirmDialog(null,"Take picture?");
            bI =  w.getImage();

        }
        new BufferedView(bI);

//
//        String result;
//        try {
//            result = instance.doOCR(bI);
//        } catch (TesseractException e) {
//            e.printStackTrace();
//            result = "Failed";
//        }
//       System.out.println(result);

    }

    private BufferedImage getImage(){
        BufferedImage bi = new BufferedImage(barWin.getWidth(),barWin.getHeight(),BufferedImage.TYPE_INT_RGB);
        barWin.paint(bi.getGraphics());
        return bi;
    }

}
