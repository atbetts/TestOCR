package tempReformat;

/**
 * Created by abetts on 8/10/15.
 */
public class MatrixTesting {
    public static void main(String[]args){

        Matrix matrix = new Matrix(new double[][]{{9,8,7},{6,5,4},{3,2,1}});
        Matrix matrix1 = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}});
        System.out.println(matrix.toString());
        System.out.println(matrix1.toString());
        System.out.println(matrix.multiply(matrix1).toString());

        Matrix conv = new Matrix(
                new double[][]{
                        {1, -1, 1},
                        {-1, 10, -1},
                        {1, -1, 1}}
        );
        System.out.println(matrix1.convolve(conv) + "= Conv");

    }


}
