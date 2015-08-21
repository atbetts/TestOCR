import java.awt.*;
import java.awt.image.Kernel;

/**
 * Created by abetts on 8/10/15.
 */
public class Matrix {
    private double[][] matrix;
    private int rows,cols;

    public Matrix(double [][] matrix){
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

    public Matrix(Matrix m){
        double[][] matrix  = m.getMatrix();
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
        rows = m.getRows();
        cols = m.getCols();
    }

    public Matrix(int rows, int cols){
        this.rows = rows;this.cols = cols;
        matrix = new double[rows][cols];
    }

    public Matrix(double d,int rows, int cols){
        this.rows = rows;this.cols = cols;
        matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j]=d;
            }
        }
    }

    public Matrix(int[][] matrix) {
        final int row = matrix.length;
        final int col = matrix[0].length;
        this.matrix = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
        rows = row;
        cols = col;
    }


    public Matrix(Dimension dimension) {
        this((int) dimension.getWidth(), (int) dimension.getHeight());
    }

    public Kernel getKernel() {
        int k = 0;
        float[] kernel = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                kernel[k++] = (float) matrix[i][j];
            }
        }

        return new Kernel(cols, rows, kernel);
    }

    public Dimension getBounds(){
        return new Dimension(rows,cols);
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public double convolve(Matrix m){
        if (!m.getBounds().equals(getBounds())) {
            throw new IllegalArgumentException(String.format("Bounds do not match! Match[%d][%d]",rows,cols));
        }
        double sum=0;
        double weight=0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum+= matrix[i][j]*m.getValue(i,j);
                weight+=m.getValue(i,j);
            }
        }
        if(weight == 0) {
            weight = 1;
        }

        return sum/weight;
    }

    public double getValue(int x, int y){if(x<0||x>=rows||y<0||y>=cols)throw new IllegalArgumentException(String.format("Out of bounds [%d][%d] not in range [%d][%d]",x,y,rows,cols)); return matrix[x][y];}

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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cast[i][j] = (int)matrix[i][j];
            }
        }

        return cast;
    }

    public void setValue(int x, int y, double val){
        if(x>=rows||y>=cols||x<0||y<0){
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

        for (int i = 0; i < this.matrix.length; i++) {  //Row of Matrix A
            for (int j = 0; j < matrix1[0].length; j++) {  //Col of Matrix B
                for (int k = 0; k < cols; k++) {
                    product[i][j] += matrix[i][k]*matrix1[k][j]; //Common Point

                }
            }
        }

        return new Matrix(product);
    }

    public Matrix getSurroundingValues(int x, int y) {
        Matrix copy = new Matrix(3, 3);
        int r, c;
        r = c = 0;
        final int i = x + 1;
        final int i1 = y + 1;
        final int i2 = x - 1;
        final int i3 = y - 1;

        for (int k = i2; k <= i; k++) {

            for (int l = i3; l <= i1; l++) {
                int wrapR = k;
                int wrapC = l;
                if (k < 0) {
                    wrapR = rows + (i2);
                } else if (k >= rows) {
                    wrapR = i - rows;
                }

                if (l < 0) {
                    wrapC = cols + (i3);
                } else if (l >= cols) {
                    wrapC = i1 - cols;
                }

                copy.setValue(r, c++, this.getValue(wrapR, wrapC));
                if (c > 2) {
                    c = 0;
                    r++;
                }
            }
        }


        return copy;
    }

    public Matrix subMatrix(int x, int y, int i, int j) {
        if (i - x < 0 || j - y < 0) {
            throw new IllegalArgumentException("Can't Have a negative range! Check Bounds");
        }
        Matrix copy = new Matrix(i - x, j - y);
        if (x < 0 || x > rows || i < 0 || i > rows || y > cols || y < 0 || j < 0 || j > cols) {
            throw new IllegalArgumentException("Matrix Does not Contain Entered Range[" + x + ":" + i + "] and range [" + y + ":" + j + "]");
        }
        int r, c;
        r = c = 0;
        for (int k = x; k < i; k++) {
            for (int l = y; l < j; l++) {
                copy.setValue(r, c++, this.getValue(k, l));
                if (c >= j - y) {
                    c = 0;
                    r++;
                }
            }
        }
        return copy;
    }


    public Matrix horzCat(Matrix m) {
        if (rows != m.getRows()) {
            throw new IllegalArgumentException("Rows Do Not Match! " + rows + "!=" + m.rows + " Can't Concatenate");
        }
        Matrix copy = new Matrix(rows, cols + m.getCols());
        for (int i = 0; i < cols + m.getCols(); i++) {
            for (int j = 0; j < rows; j++) {
                if (j < cols) {
                    copy.setValue(i, j, getValue(i, j));
                } else {
                    copy.setValue(i, j, m.getValue(i, j - cols));
                }
            }
        }

        return copy;
    }

    public Matrix vertCat(Matrix m) {
        if (cols != m.getCols()) {
            throw new IllegalArgumentException("Cols Do Not Match! " + cols + "!=" + m.cols + " Can't Concatenate");
        }
        Matrix copy = new Matrix(rows + m.getRows(), cols);
        for (int i = 0; i < rows + m.getRows(); i++) {
            for (int j = 0; j < cols; j++) {
                if (i < rows) {
                    copy.setValue(i, j, getValue(i, j));
                } else {
                    copy.setValue(i, j, m.getValue(i - rows, j));
                }
            }
        }

        return copy;
    }



    public String toString(){
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
               s.append((int) matrix[i][j] + "\t");
            }
            s.append("\n");
        }
        return s.toString();
    }

}
