package Mtrix;


import actions.Main;
import game.Settings;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class Matrix {

    private float[] matrix;
    private int rows;
    private int cols;
    private boolean transpose = false;
    private boolean showAsVector = false;

    public Matrix(int rows, int cols, boolean random) {
        init(rows, cols, random);
    }

    public Matrix(int rows, boolean random) {
        init(rows, 1, random);
    }

    public Matrix(float[] vector) {
        this.rows = vector.length;
        this.cols = 1;
        this.matrix = vector;
    }

    public Matrix(float[] matrix, int rows) {
        if (matrix.length % rows != 0) {
            throw new IndexOutOfBoundsException("not a valid Matrix");
        }
        this.matrix = matrix;
        this.cols = matrix.length / rows;
        this.rows = rows;
    }

    private void init(int rows, int cols, boolean random) {
        this.rows = rows;
        this.cols = cols;

        matrix = new float[rows * cols];
        if (random) {
            Random r = new Random();
            for (int i = 0; i < matrix.length; i++) {
                matrix[i] = (float) r.nextGaussian();
            }
        }
    }

    /**
     * creates a copy of the Matrix
     *
     * @return - the copy
     */
    public Matrix copy() {

        Matrix copy = new Matrix(ArrayUtils.clone(this.matrix), this.rows);

        //standart is not transposed
        if(transpose){
            copy.transpose();
        }
        return copy;
    }

    /**
     * Combines this matrix with to other
     * @param mtx1
     * @param mtx2
     * @param rate
     * @param choseOne
     */
    public void combineFrom(Matrix mtx1, Matrix mtx2, int rate, int choseOne){
        Random r = new Random();
        for(int i = 0; i < this.matrix.length; i++){

             //if fitness one higher than fitness two, it is more likely that he gets it
            if(r.nextInt(rate)<choseOne){
                this.matrix[i] = mtx1.matrix[i];
                //Mutation less if fitness is high
                if(r.nextInt(rate) < Settings.mutationValue){
                    this.matrix[i] += ((float) r.nextGaussian()/Settings.mutationRate);
                }
            } else {
                this.matrix[i] = mtx2.matrix[i];
                //Mutation less if fitness is high
                if(r.nextInt(rate) < Settings.mutationValue){
                    this.matrix[i] += ((float) r.nextGaussian()/Settings.mutationRate);
                }
            }
        }
    }

    /**
     *
     * @return - the index withe the highest Values, if there are 2 maxValues it returns the firs
     */
    public int getMaxIndex() {
        double max = matrix[0];
        int maxIndex = 0;
        for (int i = 1; i < rows * cols; i++) {
            if (matrix[i] > max) {
                max = matrix[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * adds the given Matrix to the Matrix
     *
     * @param toAdd - the matrix to addition
     */
    public void addUp(Matrix toAdd) {

        if (toAdd.getRows() != this.getRows() || toAdd.getCols() != this.getCols()) {
            throw new IndexOutOfBoundsException("the Matrices hav not the same shape: (" +
                    toAdd.getRows() + "," + toAdd.getCols() + ") != (" + this.getRows() + "," + this.getCols()+")");
        }

        for (int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getCols(); j++){
                this.set(i, j, this.get(i,j) + toAdd.get(i,j));
            }
        }
    }

    public void subtract(Matrix toSubtract) {
        if (toSubtract.getRows() != this.getRows() || toSubtract.getCols() != this.getCols()) {
            throw new IndexOutOfBoundsException("the Matrices hav not the same shape");
        }

        for (int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getCols(); j++){
                set(i, j, get(i,j) - toSubtract.get(i,j));
            }
        }
    }

    public void multiplyValues(Matrix toMultiply) {
        if (toMultiply.getRows() != this.getRows() || toMultiply.getCols() != this.getCols()) {
            throw new IndexOutOfBoundsException("Can not multiply these Vectors, not the same length");
        }

        for (int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getCols(); j++){
                set(i, j, get(i,j) * toMultiply.get(i,j));
            }
        }
    }

    /**
     * applys sigmoid to every value
     */
    public void sigmoid() {
        for (int i = 0; i < rows * cols; i++) {
            matrix[i] = (float) (1.0 / (1.0 + Math.exp(-matrix[i])));
        }
    }

    /**
     * multiplys matrix by scalar
     *
     * @param scalar - a scalar
     */
    public void multiplyScalar(double scalar) {
        for (int i = 0; i < rows * cols; i++) {
            matrix[i] *= (float)scalar;
        }
    }

    public boolean getIsVector() {
        return (getCols() == 1) || showAsVector;
    }

    public void print() {
        if (getRows() > 10) {
            for (int i = 0; i < 5; i++) {
                printRow(i);
            }
            printSeperator("   ...   ");
            for (int i = getRows() - 5; i < getRows(); i++) {
                printRow(i);
            }
        } else {
            for (int i = 0; i < getRows(); i++) {
                printRow(i);
            }
        }
        printSeperator("---------");
    }

    private void printSeperator(String seperator) {
        if(cols > 10){
            for(int i = 0; i < 11; i++){
                System.out.print(seperator);
                if((i)%5 != 0){
                    System.out.print("  ");
                }
            }
        } else {
            for(int i = 0; i < cols; i++){
                System.out.print(seperator);
                if(i + 1 != cols){
                    System.out.print("  ");
                }
            }
        }
        System.out.println();
    }

    private void printRow(int row) {
        if (getCols() > 10) {
            for (int i = 0; i < 5; i++) {
                System.out.print(setFixPrintLength(Float.toString(get(row, i)), 9));
                if (i < 4) {
                    System.out.print("  ");
                }
            }
            System.out.print("   ...   ");
            for (int i = getCols() - 5; i < getCols(); i++) {
                System.out.print(setFixPrintLength(Float.toString(get(row, i)), 9));
                if (i < getCols() - 1) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        } else {
            for (int i = 0; i < getCols(); i++) {
                System.out.print(setFixPrintLength(Float.toString(get(row, i)), 9));
                if (i < getCols() - 1) {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private String setFixPrintLength(String input, int length) {
        StringBuilder printer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i < input.length()) {
                printer.append(input.charAt(i));
            } else {
                printer.append(' ');
            }
        }
        return printer.toString();
    }

    public float get(int row, int col) {
        if(showAsVector){
            return this.matrix[row];
        }
        if(transpose){
            return this.matrix[col * this.cols + row % this.cols];
        } else {
            return this.matrix[row * this.cols + col % this.cols];
        }
    }

    public void transpose(){
        this.transpose = ! this.transpose;
    }

    /**
     * returns the value at that index
     *
     * @param index - the float[] matrix index, if it is a Vector the rowValue
     * @return - the value at that index
     */
    public float get(int index) {
        return this.matrix[index];
    }

    public void set(int row, int col, float value) {
        if(transpose){
            this.matrix[col * this.cols + row % this.cols] = value;
        } else {
            this.matrix[row * this.cols + col % this.cols] = value;
        }
    }

    public void set(int row, float value) {
            this.matrix[row] = value;
    }

    public int getRows() {
        if(showAsVector){
            return rows * cols;
        }
        if(transpose){
            return cols;
        }
        return rows;
    }

    public int getCols() {
        if(showAsVector){
            return 1;
        }
        if(transpose){
            return rows;
        }
        return cols;
    }

    public void reset(){
        this.matrix = new float[this.matrix.length];
    }

    public void setShowAsVector(boolean value){
        this.showAsVector = value;
    }

}
