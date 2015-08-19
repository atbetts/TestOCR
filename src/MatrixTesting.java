/**
 * Created by abetts on 8/10/15.
 */
public class MatrixTesting {
    public static void main(String[]args){

        Matrix matrix = new Matrix(new int[][]{{1, 2, 1}, {1, 2, 1}, {1, 2, 1}});
        Matrix matrix1 = new Matrix(new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}});
        System.out.println(matrix.toString());
        System.out.println(matrix1.toString());
        System.out.println(matrix.multiply(matrix1).toString());

        Matrix gaussianBlur = new Matrix(
                new int[][]{
                        {1, 2, 3},
                        {8, 1, 4},
                        {7, 6, 5}}
        );

        System.out.printf("Convolve = %.3f%n", matrix.convolve(gaussianBlur));
        System.out.println(gaussianBlur.getSurroundingValues(0, 0));
        final Matrix x = matrix.vertCat(matrix).horzCat(gaussianBlur.vertCat(gaussianBlur));
        System.out.println(x);
        System.out.println(x.subMatrix(3, 4, 5, 5));
    }


}
