import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

/**
 * Created by abetts on 8/21/15.
 */
public class BufferedImageFilter {


    public static void dilate(BufferedImage bufferedImage, Matrix structure) {
        binary(bufferedImage, 100);
        Raster raster = bufferedImage.getRaster();
        int rows = bufferedImage.getHeight();
        int cols = bufferedImage.getWidth();

        int numElements = raster.getNumDataElements();

        switch (raster.getTransferType()) {
            case DataBufferByte.TYPE_BYTE:
                byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();
                if (numElements == 3) {
                    for (int i = 0; i < data.length; i += numElements) {
                        int adjustedIndex = i / numElements;
                        int col = adjustedIndex % cols;
                        int row = adjustedIndex / rows;


                    }
                }
                break;
            default:
                break;
        }

    }

    public static void binary(BufferedImage bufferedImage, int threshold) {
        Raster raster = bufferedImage.getRaster();
        int rows = bufferedImage.getHeight();
        int cols = bufferedImage.getWidth();

        int numElements = raster.getNumDataElements();

        switch (raster.getTransferType()) {
            case DataBufferByte.TYPE_BYTE:
                byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();
                if (numElements == 3) {
                    for (int i = 0; i < data.length; i += numElements) {
                        int grey = (int) (.21 * (data[i] & 0xFF) + .72 * (data[i + 1] & 0xFF) + .07 * (data[i + 2] & 0xFF));
                        if (grey < threshold)
                            data[i] = data[i + 1] = data[i + 2] = 0;
                        else
                            data[i] = data[i + 1] = data[i + 2] = (byte) 255;


                    }
                }
                break;
            default:
                break;
        }


    }


    public static void greyScale(BufferedImage bufferedImage) {
        Raster raster = bufferedImage.getRaster();
        int rows = bufferedImage.getHeight();
        int cols = bufferedImage.getWidth();

        int numElements = raster.getNumDataElements();

        switch (raster.getTransferType()) {
            case DataBufferByte.TYPE_BYTE:
                byte[] data = ((DataBufferByte) raster.getDataBuffer()).getData();
                byte[] greyData = new byte[data.length];
                if (numElements == 3) {
                    for (int i = 0; i < data.length; i += numElements) {
                        int grey = (int) (.21 * (data[i] & 0xFF) + .72 * (data[i + 1] & 0xFF) + .07 * (data[i + 2] & 0xFF));
                        greyData[i] = greyData[i + 1] = greyData[i + 2] = (byte) (grey & 0xFF);
                    }
                }
                for (int i = 0; i < data.length; i++) {
                    data[i] = greyData[i];
                }
                break;
            default:
                break;
        }


    }


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
                        for (int j = Math.max(0, r - offsetRow); j < Math.min(rows, r + offsetRow + 1); j += numElements) {
                            for (int k = Math.max(0, c - offsetCol); k < Math.min(cols, c + 1 + offsetCol); k += numElements) {
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
