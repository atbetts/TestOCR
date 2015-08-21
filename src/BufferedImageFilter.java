import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * Created by abetts on 8/21/15.
 */
public class BufferedImageFilter {


    public static void filter(BufferedImage bufferedImage, double[][] kernel) {

        if (kernel.length % 2 == 0 || kernel[0].length % 2 == 0)
            return;

        Raster raster = bufferedImage.getRaster();
        int rows = bufferedImage.getHeight();
        int cols = bufferedImage.getWidth();

        int numElements = raster.getNumDataElements();

        switch (raster.getTransferType()) {
            case DataBufferByte.TYPE_BYTE:
                byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();

                if (numElements == 4) {

                } else {
                    for (int i = 0; i < data.length; i += numElements) {
                        int r = (i / numElements) / rows;
                        int c = (i / numElements) % cols;
                        int offsetRow = (kernel.length - 1) / 2;
                        offsetRow *= numElements;
                        int offsetCol = (kernel[0].length - 1) / 2;
                        offsetCol *= numElements;

                        int kR = 0;
                        int kC = 0;
                        double wR, wG, wB;
                        wR = wG = wB = 0;
                        for (int j = Math.max(0, r - offsetRow); j < Math.min(rows, r + offsetRow + 1); j++) {
                            for (int k = Math.max(0, c - offsetCol); k < Math.min(cols, c + 1 + offsetCol); k++) {
                                int base = j * cols + k;
                                wR += (data[base] & 0xFF) * kernel[kR][kC];
                                wG += (data[base] & 0xFF) * kernel[kR][kC];
                                wB += (data[base] & 0xFF) * kernel[kR][kC];
                                kC++;
                                if (kC >= kernel[0].length) {
                                    kC = 0;
                                    kR++;
                                }

                            }
                        }
                        int red = (int) wR;
                        int green = (int) wG;
                        int blue = (int) wB;
                        data[i] = (byte) (red & 0xFF);
                        data[i + 1] = (byte) (green & 0xFF);
                        data[i + 2] = (byte) (blue & 0xFF);

                    }
                }


                break;

            default:
                System.out.println("Unrecognized Data Type");
                break;
        }


    }


}
