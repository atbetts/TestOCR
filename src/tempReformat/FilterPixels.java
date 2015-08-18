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


        //Consider Temp storing of only kernel height pixels
        //Truncates border for now
        for (int i = 1; i < image.length - 1; i++) {
            for (int j = 1; j < image[0].length - 1; j++) {

                int red_w = 0;
                int blue_w = 0;
                int green_w = 0;
                int alpha_w = 0;

                int count = 0;
                int row = 0;

                for (int x = Math.max(0, i - 1); x < Math.min(image.length, i + 2); x++) {
                    for (int y = Math.max(0, j - 1); y < Math.min(image[0].length, j + 2); y++) {
                        Pixel p = pixels[x][y];
                        int red = p.getRed();
                        int blue = p.getBlue();
                        int green = p.getGreen();
                        int alpha = p.getAlpha();

                        if (count % 3 == 0) {
                            if (count != 0)
                            row++;
                            count = 0;
                        }
                        final int kernelVal = kernel[row][count++];
                        red_w += red * kernelVal;
                        blue_w += blue * kernelVal;
                        green_w += green * kernelVal;
                        alpha_w += alpha * kernelVal;
                    }
                }

                if (red_w < 0) {
                    red_w = 0;
                }
                if (red_w > 255) {
                    red_w = 255;
                }
                if (green_w < 0) {
                    green_w = 0;
                }
                if (green_w > 255) {
                    green_w = 255;
                }
                if (blue_w < 0) {
                    blue_w = 0;
                }
                if (blue_w > 255) {
                    blue_w = 255;
                }


                final Pixel pixel = new Pixel(red_w, green_w, blue_w, alpha_w);

                image[i][j] = pixel;

            }
        }


    }

}
