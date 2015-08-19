import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abetts on 8/10/15.
 */
public class ImageTesting {

    static PixelImage temp;

    public static void test() throws Exception {
        BufferedImage img = ImageIO.read(new File("hardmode.jpg"));
        File imgDir = new File("images");
        imgDir.mkdir();
        String imgPath = "images/";
        temp = new PixelImage(img);

        ArrayList<String> dictionary = new ArrayList<>();
        BufferedReader bR = new BufferedReader(new FileReader(new File("words.english")));
        String temper;
        while ((temper = bR.readLine()) != null) {
            dictionary.add(temper);
        }



        JFrame test = new JFrame("Image Window");
        test.add(new ImgView());
        test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        test.setSize(img.getWidth(), img.getHeight());
        test.setVisible(true);
        Tesseract t = new Tesseract();
        t.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY);
        boolean breaker = false;
        Map<int[], Double> trial = new HashMap<int[], Double>();
        for (int i = 130; i <= 150; i++) {
//            for (int j = 0; j < 5; j++) {
//                for (int k = 0; k < 5; k++) {

            int j = 0;
            int k = 0;

                    System.out.println("binary:" + i + "\tblur:" + j + "\tsharp:" + k);

                    long time = System.nanoTime();
                    temp.testValues(i, j, k);
                    final String s = t.doOCR(temp.getMyImage());
                    final double timer = (float) (System.nanoTime() - time) / 1e9;
                    System.out.println(String.format("%.3f", timer));
                    if (s.contains("UPS") && s.contains("El Camino")) {
                        trial.put(new int[]{i, j, k}, (System.nanoTime() - time) / 1e9);
                        ImageIO.write(temp.getMyImage(), "png", new File(imgPath + s.trim() + "-" + i + "-" + j + "-" + k + "-" + String.format("%.3f", timer) + ".png"));
                    }
            System.out.println(s.replaceAll("[^A-Za-z0-9. \n]", ""));
//                    Arrays.stream(s.split(" ")).forEach(str -> {
//                        if (dictionary.contains(str.replace("[^A-Za-z]", "").toLowerCase())) {
//                            System.out.println(str);
//                        }
//                    });

                    if (!test.isValid()) {
                        breaker = true;
                        break;
                    }
                    test.revalidate();
                    test.repaint();
//                }
//                if (breaker) break;
//            }
//            if (breaker) break;
        }
        trial.entrySet().parallelStream().map(e -> "binary:" + e.getKey()[0] + "\tblur:" + e.getKey()[1] + "\tsharp:" + e.getKey()[2] +
                        "\ttime:" + e.getValue()
        ).forEach(System.out::println);

        test = new JFrame("Image Window");
        test.add(new ImgView());
        test.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        test.setSize(img.getWidth(), img.getHeight());
        test.setVisible(true);

        for (Map.Entry<int[], Double> e : trial.entrySet()) {

            temp.resetPixels();
            temp.buildImage();
            temp.testValues(e.getKey()[0], e.getKey()[1], e.getKey()[2]);

            int i = JOptionPane.showConfirmDialog(null, t.doOCR(temp.getMyImage()));
            if (i == JOptionPane.CANCEL_OPTION) {
                break;
            }
            test.revalidate();
            test.repaint();

        }
    }

    public static void main(String...args) throws Exception{

        test();


    }

    public static class ImgView extends JPanel{


        public void paintComponent(Graphics g){
//            g.drawImage(temp.getMyImage().getScaledInstance(500, 500, BufferedImage.SCALE_DEFAULT), 0, 0, null);
            temp.draw(g, 0, 0);
        }

    }


}
