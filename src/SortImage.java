import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by abetts on 8/19/15.
 */
public class SortImage {
    public static void main(String... arg) {
        ArrayList<Double> timeList = new ArrayList<>();
        ArrayList<String> query = new ArrayList<>();
        try {
            Files.walk(new File("images").toPath()).filter(p -> p.toString().toLowerCase().contains("ups")).map(Path::toString).forEach(str -> {
                String[] data = str.split("-");
                int binary = 0;
                int blur = 0;
                int sharp = 0;
                double time = 0;
                try {
                    binary = Integer.parseInt(data[1]);
                    blur = Integer.parseInt(data[2]);
                    sharp = Integer.parseInt(data[3]);
                    time = Double.parseDouble(data[4].substring(0, data[4].lastIndexOf(".")));
                } catch (NumberFormatException e) {
                    System.out.println(str);
                    time = 1000;
                }
                String s = "binary = " + binary + "\t" + "blur = " + blur + "\t" + "sharp = " + sharp + "\t" + "time = " + time;
                query.add(s + "\n" + str);
                timeList.add(time);
                System.out.println(s);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        double d = Collections.min(timeList);
        System.out.println(query.get(timeList.indexOf(d)));


    }
}
