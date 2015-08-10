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

    public Matrix(int rows, int cols){
        this.rows = rows;this.cols = cols;
        matrix = new double[rows][cols];
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

    public int[][] getIntMatrix(){
        int [][] cast = new int[rows][cols];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                cast[i][j] = (int)matrix[i][j];
            }
        }

        return cast;
    }

    public void setValue(int x, int y, double val){
        if(x>rows||y>cols||x<0||y<0){
            throw new IllegalArgumentException(String.format("(%d,%d) not in range of [%d,%d]",x,y,rows,cols));
        }
        matrix[x][y] = val;
    }

    public Matrix multiply(Matrix m){
        final double[][] matrix1 = m.getMatrix();
        if(this.matrix[0].length!= matrix1.length){
            throw new IllegalArgumentException("Matrix Dimensions Don't Match: \n Tried" +
                    " multiplying a ["+ this.matrix.length+","+ this.matrix[0].length+"] by ["
                    +matrix1.length+","+matrix[0].length+"]");
        }

        double [][] product = new double[cols][matrix1.length];

        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                for (int k = 0; k < cols; k++) {
                    product[i][j] = matrix[i][k]*matrix1[k][j];
                }
            }
        }

        return new Matrix(product);
    }


}
