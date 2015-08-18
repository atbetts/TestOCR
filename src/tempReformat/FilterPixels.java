package tempReformat;

/**
 * Created by abetts on 8/18/15.
 */
public class FilterPixels {


    public static void ApplyKernel(int[][] kernel, Pixel[][] image) {


        Pixel[][] pixels = new Pixel[image.length][image[0].length];

        for (int i = 0; i < pixels.length; i++) {
            System.arraycopy(image[i], 0, pixels[i], 0, image[0].length);
        }


        if (kernel.length != kernel[0].length) {
            System.out.println("Please use a square kernel for now");
            return;
        }
        if (image.length < kernel.length || image[0].length < kernel[0].length) {
            System.out.println("Kernel larger than image");
            return;
        }


        int kernelWeight = 0;
        int kernelCount = 0;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel[0].length; j++) {
                if (kernel[i][j] != 0) {
                    kernelWeight += kernel[i][j];
                    kernelCount++;
                }
            }
        }
        if (kernelWeight != 0) {
            kernelWeight /= kernelCount;
        } else {
            kernelWeight = 1;
        }

        //Consider Temp storing of only kernel height pixels
        //Truncates border for now
        for (int i = 1; i < image.length - 1; i++) {
            for (int j = 1; j < image[0].length - 1; j++) {

                int weighted = 0;
                int count = 0;
                int row = 0;
                for (int x = Math.max(0, i - 1); x < Math.min(image.length, i + 2); x++) {
                    for (int y = Math.max(0, j - 1); y < Math.min(image[0].length, j + 2); y++) {
                        Pixel p = image[x][y];
                        int red = p.getRed();
                        int blue = p.getBlue();
                        int green = p.getGreen();
                        int grey = red + blue + green;
                        grey /= 3;
                        if (count % 3 == 0)
                            row++;
                        weighted += grey * kernel[count][row];
                    }
                }

                image[i][j] = new Pixel(weighted / kernelWeight, Pixel.SOLID_COLOR);

            }
        }


    }

}
