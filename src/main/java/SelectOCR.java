import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by abetts on 9/3/15.
 */
public class SelectOCR {


    Webcam currentWebcam;
    BlockingQueue<BufferedImage> imgBuffer = new LinkedBlockingQueue<>();
    JFrame webViewSize;
    ImageFrame imageFrame;

    public SelectOCR() {
        setupGUI();


    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(SelectOCR::new);

    }

    private JFrame setUpSizeList() {
        List<Dimension> sizeList = Arrays.asList(currentWebcam.getViewSizes());
        DefaultListModel<Dimension> sizeModel = new DefaultListModel<>();
        sizeList.stream().forEach(sizeModel::addElement);
        JList<Dimension> dispList = new JList<>(sizeModel);
        dispList.setVisibleRowCount(sizeList.size());
        dispList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dispList.addListSelectionListener(select -> {
            final Dimension selectedValue = dispList.getSelectedValue();
            currentWebcam.setViewSize(selectedValue);

        });
        JFrame sizeFrame = new JFrame();
        sizeFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel sizePanel = new JPanel();
        sizePanel.add(new JScrollPane(dispList));
        sizeFrame.setContentPane(sizePanel);
        sizeFrame.pack();
        sizeFrame.setVisible(true);
        return sizeFrame;
    }

    private void setUpWebcamList() {
        List<Webcam> webcamList = Webcam.getWebcams();
        DefaultListModel<Webcam> webList = new DefaultListModel<>();
        webcamList.stream().forEach(webList::addElement);
        JList<Webcam> dispList = new JList<>(webList);
        dispList.setVisibleRowCount(webcamList.size());
        dispList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dispList.addListSelectionListener(select -> {
            final Webcam selectedValue = dispList.getSelectedValue();
            System.out.println(selectedValue);
            if (selectedValue != currentWebcam) {
                if (currentWebcam == null)
                    currentWebcam = selectedValue;

                if (currentWebcam.isOpen())
                    currentWebcam.getDevice().close();

                currentWebcam = selectedValue;
                if (webViewSize != null) {
                    webViewSize.dispose();
                }
                webViewSize = setUpSizeList();

            }
        });


        JFrame webcamListFrame = new JFrame();
        webcamListFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel webcamListPanel = new JPanel();
        webcamListPanel.add(new JScrollPane(dispList));
        webcamListFrame.setContentPane(webcamListPanel);
        webcamListFrame.pack();
        webcamListFrame.setVisible(true);
    }

    private void setUpImageView() {
        imageFrame = new ImageFrame();
        if (currentWebcam != null)
            imageFrame.getContentPane().setPreferredSize(currentWebcam.getViewSize());
        imageFrame.pack();
    }

    private void setupGUI() {
        setUpWebcamList();

        setUpImageView();


    }


    private class ImageFrame extends JFrame {

        BufferedImage cache, currentImage;
        ImagePanel myPanel;

        public ImageFrame() {
            setTitle("ImageView");

            myPanel = new ImagePanel();


            if (currentWebcam != null) {
                myPanel.setPreferredSize(currentWebcam.getViewSize());
            }
            setContentPane(myPanel);
            pack();
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(true);

            startWebcamThread();
            new Timer(16, (act) -> repaint()).start();
        }

        private void startWebcamThread() {
            new Thread(() -> {

                while (true) {
                    if (currentWebcam != null) {
                        if (currentWebcam.isOpen()) {
                            currentWebcam.open();
                        }

                        imgBuffer.add(currentWebcam.getImage());
                        System.out.println(imgBuffer.size());
                    }
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }).start();
        }


        private class ImagePanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color tempColor = g.getColor();
                if (currentImage != null)
                    g.drawImage(currentImage, 0, 0, null);
                else if (cache != null) {
                    g.drawImage(cache, 0, 0, null);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.white);
                    g.drawString("Image Not Found\n(Is camera selected?)", getWidth() / 2 - 150, getHeight() / 2);
                }
                cache = currentImage;
                g.setColor(tempColor);
            }
        }


    }

}
