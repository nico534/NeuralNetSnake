package Mtrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MatrixCalc {
    private static final int parallelLimit = 300;


    public static Matrix[] multiplyParallel(final Matrix[] mtxA, final Matrix[] mtxB) throws InterruptedException {
        // if Matrices to small linear multiplication is faster
        if (mtxA[0].getRows() + mtxA[0].getCols() < parallelLimit) {
            return multiplyLinear(mtxA, mtxB);
        }
        if (mtxA.length != mtxB.length) {
            System.err.println("Cant multiply Matrices, different numbers");
            return null;
        }
        final ExecutorService threadPool = Executors.newFixedThreadPool(mtxA.length);

        final Matrix[] allErgs = new Matrix[mtxA.length];

        List<Callable<Object>> allCallable = new ArrayList<Callable<Object>>();
        for (int i = 0; i < mtxA.length; i++) {
            final int pos = i;
            allErgs[i] = new Matrix(mtxA[i].getRows(), mtxB[i].getCols(), false);
            allCallable.add(Executors.callable(new Runnable() {
                public void run() {
                    multiply(mtxA[pos], mtxB[pos], allErgs[pos]);
                }
            }));
        }
        threadPool.invokeAll(allCallable);
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        return allErgs;
    }

    private static Matrix[] multiplyLinear(Matrix[] mtxA, Matrix[] mtxB) {
        Matrix[] ergMtx = new Matrix[mtxA.length];
        for (int i = 0; i < mtxA.length; i++) {
            ergMtx[i] = multiply(mtxA[i], mtxB[i]);
        }
        return ergMtx;
    }

    private static void multiply(Matrix mtxA, Matrix mtxB, Matrix mtxC) {
        for (int i = 0; i < mtxA.getRows(); i++) {
            for (int k = 0; k < mtxB.getCols(); k++) {
                float sum = 0;
                for (int j = 0; j < mtxB.getRows(); j++) {
                    sum = sum + mtxA.get(i, j) * mtxB.get(j, k);
                }
                mtxC.set(i, k, sum);
            }
        }
    }

    /**
     * multipllys mtxA * mtxB with ikj alg.
     *
     * @param mtxA - mtxA
     * @param mtxB - mtxB
     * @return - mtxA * mtxB
     */
    public static Matrix multiply(Matrix mtxA, Matrix mtxB) {
        Matrix erg = new Matrix(mtxA.getRows(), mtxB.getCols(), false);
        multiply(mtxA, mtxB, erg);
        return erg;
    }


    /**
     * multiptys mtxA.transpose * mtxB
     *
     * @param mtxA - mtxA
     * @param mtxB - mtxB
     * @return - mtxA.transpose * mtxB
     * @throws InterruptedException -
     */
    public static Matrix[] mtxATransposeMtxB(final Matrix mtxA, final Matrix[] mtxB) throws InterruptedException {

        // if Matrices to small linear multiplication is faster
        if (mtxA.getRows() + mtxA.getCols() < parallelLimit) {
            return mtxATransposeMtxBLinear(mtxA, mtxB);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(mtxB.length);
        final Matrix[] allErgs = new Matrix[mtxB.length];

        List<Callable<Object>> allCallable = new ArrayList<Callable<Object>>();
        for (int i = 0; i < mtxB.length; i++) {
            final int pos = i;
            allErgs[i] = new Matrix(mtxA.getCols(), mtxB[i].getCols(), false);
            allCallable.add(Executors.callable(new Runnable() {
                public void run() {
                    mtxATransposeMtxB(mtxA, mtxB[pos], allErgs[pos]);
                }
            }));
        }
        threadPool.invokeAll(allCallable);
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        return allErgs;
    }

    private static Matrix[] mtxATransposeMtxBLinear(final Matrix mtxA, final Matrix[] mtxB) {
        Matrix[] allErgs = new Matrix[mtxB.length];
        for (int i = 0; i < mtxB.length; i++) {
            allErgs[i] = new Matrix(mtxA.getCols(), mtxB[i].getCols(), false);
            mtxATransposeMtxB(mtxA, mtxB[i], allErgs[i]);
        }
        return allErgs;
    }

    public static Matrix mtxATransposeMtxB(final Matrix mtxA, final Matrix mtxB) {
        Matrix allErgs = new Matrix(mtxA.getCols(), mtxB.getCols(), false);;
        mtxATransposeMtxB(mtxA, mtxB, allErgs);
        return allErgs;
    }


    /**
     * Multiply transpose(mtxA) * mtxB without actually transposing
     *
     * @param mtxA - mtxA
     * @param mtxB - mtxB
     * @return - mtxA.transpose * mtxB
     */
    private static void mtxATransposeMtxB(Matrix mtxA, Matrix mtxB, Matrix mtxC) {
        mtxA.transpose();
        multiply(mtxA, mtxB, mtxC);
        //transpose back
        mtxA.transpose();
    }

    /**
     *
     * Multiply mtxA * transpose(mtxB) without actually transposing
     *
     * @param mtxA - mtxA
     * @param mtxB - mtxB
     * @return - mtxA * mtxB.transpose
     * @throws InterruptedException -
     */
    public static Matrix[] mtxAMtxBTranspose(final Matrix[] mtxA, final Matrix[] mtxB) throws InterruptedException {

        if (mtxA[0].getRows() + mtxB[0].getRows() < parallelLimit) {
            return mtxAMtxBTransposeLinear(mtxA, mtxB);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(mtxB.length);

        final Matrix[] allErgs = new Matrix[mtxB.length];

        List<Callable<Object>> allCallable = new ArrayList<Callable<Object>>();
        for (int i = 0; i < mtxB.length; i++) {
            final int pos = i;
            allErgs[i] = new Matrix(mtxA[i].getRows(), mtxB[i].getRows(), false);
            allCallable.add(Executors.callable(new Runnable() {
                public void run() {
                    mtxAMtxBTranspose(mtxA[pos], mtxB[pos], allErgs[pos]);
                }
            }));
        }
        threadPool.invokeAll(allCallable);
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        return allErgs;
    }

    private static Matrix[] mtxAMtxBTransposeLinear(final Matrix[] mtxA, final Matrix[] mtxB) {
        Matrix[] allErgs = new Matrix[mtxB.length];
        for (int i = 0; i < mtxB.length; i++) {
            allErgs[i] = new Matrix(mtxA[i].getRows(), mtxB[i].getRows(), false);
            mtxAMtxBTranspose(mtxA[i], mtxB[i], allErgs[i]);
        }
        return allErgs;
    }

    public static Matrix mtxAMtxBTranspose(final Matrix mtxA, final Matrix mtxB) {
        Matrix allErgs = new Matrix(mtxA.getRows(), mtxB.getRows(), false);
        mtxAMtxBTranspose(mtxA, mtxB, allErgs);
        return allErgs;
    }

    private static void mtxAMtxBTranspose(Matrix mtxA, Matrix mtxB, Matrix mtxC) {
        mtxB.transpose();
        multiply(mtxA, mtxB, mtxC);
        //transpose back
        mtxB.transpose();
    }

    /**
     * Calculates sigmoid(mtxA * mtxB + biases)
     *
     * @param mtxA - mtxA
     * @param mtxB - mtxB
     * @param biases - biases
     * @return - sigmoid(mtxA * mtxB + biases)
     * @throws InterruptedException -
     */
    public static Matrix[] calcPrediction(final Matrix mtxA, final Matrix[] mtxB, final Matrix biases) throws InterruptedException {

        if(mtxA.getRows() + mtxA.getCols() < 7 * parallelLimit){
            return calcPredictionLinear(mtxA, mtxB, biases);
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(mtxB.length);

        final Matrix[] allErgs = new Matrix[mtxB.length];

        List<Callable<Object>> allCallable = new ArrayList<Callable<Object>>();
        for (int i = 0; i < mtxB.length; i++) {
            final int pos = i;
            allErgs[i] = new Matrix(mtxA.getRows(), mtxB[i].getCols(), false);
            allCallable.add(Executors.callable(new Runnable() {
                public void run() {
                    multiplyPlusBias(mtxA, mtxB[pos], allErgs[pos], biases);
                }
            }));
        }
        threadPool.invokeAll(allCallable);
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        return allErgs;
    }

    private static Matrix[] calcPredictionLinear(final Matrix mtxA, final Matrix[] mtxB, final Matrix biases) {
        Matrix[] allErgs = new Matrix[mtxB.length];
        for (int i = 0; i < mtxB.length; i++) {
            allErgs[i] = new Matrix(mtxA.getRows(), mtxB[i].getCols(), false);
            multiplyPlusBias(mtxA, mtxB[i], allErgs[i], biases);
        }
        return allErgs;
    }

    public static Matrix calcPrediction(final Matrix mtxA, final Matrix mtxB, final Matrix biases) {
        Matrix ergMtx = new Matrix(mtxA.getRows(), mtxB.getCols(), false);
        multiplyPlusBias(mtxA, mtxB, ergMtx, biases);
        return ergMtx;
    }

    private static void multiplyPlusBias(Matrix mtxA, Matrix mtxB, Matrix mtxC, Matrix bias) {
        multiply(mtxA, mtxB, mtxC);
        mtxC.addUp(bias);
        mtxC.sigmoid();
    }

    /**
     * Calculates the gradient parallel
     *
     * @param mtx
     * @return
     * @throws InterruptedException
     */
    public static Matrix[] calcGradiant(final Matrix[] mtx) throws InterruptedException {
        if (!mtx[0].getIsVector()) {
            return null;
        }
        final ExecutorService threadPool = Executors.newFixedThreadPool(mtx.length);
        final Matrix[] allErgs = new Matrix[mtx.length];
        List<Callable<Object>> allCallable = new ArrayList<Callable<Object>>();
        for (int i = 0; i < mtx.length; i++) {
            final int pos = i;
            allErgs[i] = new Matrix(mtx[i].getRows(), false);
            allCallable.add(Executors.callable(new Runnable() {
                public void run() {
                    calcGradiant(mtx[pos], allErgs[pos]);
                }
            }));
        }
        threadPool.invokeAll(allCallable);
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        return allErgs;
    }

    public static Matrix calcGradiant(Matrix mtx){
        if (!mtx.getIsVector()) {
            return null;
        }
        Matrix erg =  new Matrix(mtx.getRows(), false);
        calcGradiant(mtx, erg);
        return erg;
    }

    private static void calcGradiant(Matrix mtxA, Matrix mtxB) {
        if (mtxA.getIsVector()) {
            for (int i = 0; i < mtxA.getRows(); i++) {
                mtxB.set(i, 1, mtxA.get(i) * (1 - mtxA.get(i)));
            }
        }
    }
}
