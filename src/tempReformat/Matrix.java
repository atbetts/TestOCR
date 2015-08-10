package tempReformat;

/**
 * Created by abetts on 8/10/15.
 */
public class Matrix {
    private double[][] matrix;
    private int rows,cols;

    public Matrix(double [][] matrix){
        this.matrix = matrix;
        rows = matrix.length;
        cols = matrix[0].length;

    }

    public Matrix(int[][]matrix){
        final int row = matrix.length;
        final int col = matrix[0].length;
        this.matrix = new double[row][col];
        for (int i = 0; i <row ; i++) {
            for (int j = 0; j < col; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
        rows = row;
        cols = col;
    }

    public Matrix multiply(double a){
        double[][] m = matrix;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = a*m[i][j];
            }
        }

        return new Matrix(m);
    }

    public double[][] getMatrix(){

        return matrix;
    }

    public Matrix multiply(Matrix m){
        final double[][] matrix1 = m.getMatrix();
        if(this.matrix[0].length!= matrix1.length){
            throw new ArrayIndexOutOfBoundsException("Matrix Dimensions Don't Match: \n Tried" +
                    " multiplying a ["+ this.matrix.length+","+ this.matrix[0].length+"] by ["
                    +matrix1.length+","+matrix[0].length+"]");
        }

        double [][] product = new double[cols][matrix1.length];

        for (int i = 0; i < this.matrix[0].length; i++) {
            for (int j = 0; j < this.matrix.length; j++) {

            }
        }

        return new Matrix(product);
    }


}
