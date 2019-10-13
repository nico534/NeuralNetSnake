package Brain;

import Mtrix.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

public class NeuralNetwork {

    private static final String DEFAULT_SEPERATOR = ",";

    private Matrix[] weights;
    private Matrix[] biases;
    private Matrix lastPredicted;
    private int layerCount;
    private double errorRate;

    private double predictedProb;

    public NeuralNetwork(int[] layerSize) {
        init(layerSize, true);
    }

    public NeuralNetwork(File networkFile) throws IOException {
        loadNeuralNetworkFromFile(networkFile);
    }

    private void init(int[] layerSize, boolean randomize) {
        this.layerCount = layerSize.length;
        this.weights = new Matrix[layerCount - 1];
        this.biases = new Matrix[layerCount - 1];
        for (int i = 0; i < layerCount - 1; i++) {
            this.weights[i] = new Matrix(layerSize[i + 1], layerSize[i], randomize);
            this.biases[i] = new Matrix(layerSize[i + 1], 1, randomize);
        }
    }

    public int predict(Matrix input) {
        for (int i = 0; i < layerCount - 1; i++) {
            input = MatrixCalc.calcPrediction(this.weights[i], input, this.biases[i]);
        }
        this.predictedProb = input.get(input.getMaxIndex());
        this.lastPredicted = input;
        return input.getMaxIndex();
    }

    public double getPredictedProb() {
        return this.predictedProb;
    }

    public void crossover(NeuralNetwork net1, NeuralNetwork net2, int fitness1, int fitness2) {
        int predRate = (fitness1 + fitness2) / 2;
        int predOne = fitness1 / 2;
        for (int i = 0; i < weights.length; i++) {
            weights[i].combineFrom(net1.weights[i], net2.weights[i], predRate, predOne);
        }
        for (int i = 0; i < biases.length; i++) {
            biases[i].combineFrom(net1.biases[i], net2.biases[i], predRate, predOne);
        }
    }


    /**
     * Calculats the Errors of the given predictions and answers per neuronStrang
     *
     * @param predictions - the calculated final predictions
     * @param answers     - the right answers for the predictions
     * @return - all Errors
     */
    private Matrix[] calcError(Matrix predictions, Matrix answers) {

        Matrix[] errors = new Matrix[this.layerCount - 1];
        errors[0] = answers.copy();
        errors[0].subtract(predictions);

        for (int i = 1; i < this.layerCount - 1; i++) {
            errors[i] = MatrixCalc.mtxATransposeMtxB(this.weights[this.layerCount - i - 1], errors[i - 1]);
        }
        ArrayUtils.reverse(errors);
        return errors;
    }


    /**
     * Calculates all Neuron values and returns theme in order of biases
     *
     * @param input - the given startNeurons
     * @return - all predicted neuron vectors
     */
    private Matrix[] feedForward(Matrix input) {
        Matrix[] allLayers = new Matrix[this.layerCount];
        allLayers[0] = input;
        for (int i = 0; i < layerCount - 1; i++) {
            allLayers[i + 1] = MatrixCalc.calcPrediction(this.weights[i], allLayers[i], this.biases[i]);
        }
        return allLayers;
    }

    /**
     * Trains the network with the given parameters
     *
     * @param label        - results
     * @param learningRate - learningRate
     * @return - true by success, otherwise false.
     */
    public int train(Matrix input, Matrix label, double learningRate) {

        Matrix[] allPredictions = feedForward(input);
        // Calculate the Errors with the last Result of the prediction (the output neurons)
        Matrix[] allErrors = calcError(allPredictions[this.layerCount - 1], label);

        /* calculate gradiant, multiply by vector and learningRate and add to the biases
           Afterwords multiply gradiant * Prediction.transpose to get delta_W for the Weights */
        for (int i = 0; i < this.layerCount - 1; i++) {
            Matrix nabla_b = MatrixCalc.calcGradiant(allPredictions[i + 1]);
            nabla_b.multiplyValues(allErrors[i]);
            Matrix nabla_w = MatrixCalc.mtxAMtxBTranspose(nabla_b, allPredictions[i]);

            nabla_b.multiplyScalar(learningRate);
            this.biases[i].addUp(nabla_b);
            nabla_w.multiplyScalar(learningRate);
            this.weights[i].addUp(nabla_w);
        }
        return allPredictions[allPredictions.length-1].getMaxIndex();
    }


    public void saveNeuralNetwork(File f) throws IOException {
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        FileWriter writer = new FileWriter(f);

        //first line is the Network layout
        String nextLine = "";
        for (int i = 0; i < this.weights.length; i++) {
            nextLine += this.weights[i].getCols() + DEFAULT_SEPERATOR;
        }
        nextLine += this.weights[this.weights.length - 1].getRows() + "\n";
        writer.append(nextLine);

        // AfterWords the WeightMatrixes as Lines
        for (Matrix w : this.weights) {
            nextLine = "";
            for (int i = 0; i < w.getRows(); i++) {
                for (int j = 0; j < w.getCols(); j++) {
                    nextLine += (w.get(i, j) + DEFAULT_SEPERATOR);
                }
                nextLine = nextLine.substring(0, nextLine.length() - 1);
                nextLine += "\n";
                writer.append(nextLine);
                nextLine = "";
            }
        }

        // and the Biases as Vector lines
        for (Matrix b : this.biases) {
            nextLine = "";
            for (int i = 0; i < b.getRows(); i++) {
                for (int j = 0; j < b.getCols(); j++) {
                    nextLine += (b.get(i, j) + DEFAULT_SEPERATOR);
                }
            }
            nextLine = nextLine.substring(0, nextLine.length() - 1);
            nextLine += "\n";
            writer.append(nextLine);
        }
        writer.flush();
        writer.close();
        System.out.println("saved " + f.getName() + " successfully");
    }

    private void loadNeuralNetworkFromFile(File f) throws IOException {
        BufferedReader br = null;
        String line = "";
        br = new BufferedReader(new FileReader(f));
        if ((line = br.readLine()) == null) {
            throw new IOException("file is damaged ore missing");
        }
        String[] seperated = line.split(DEFAULT_SEPERATOR);

        //load LayerSize
        int[] layerSize = new int[seperated.length];

        for (int i = 0; i < seperated.length; i++)
            layerSize[i] = Integer.parseInt(seperated[i]);
        init(layerSize, false);

        //load Weight Matrices
        for (Matrix w : this.weights) {
            for (int i = 0; i < w.getRows(); i++) {
                if ((line = br.readLine()) == null) {
                    throw new IOException("file is damaged ore missing");
                }
                seperated = line.split(DEFAULT_SEPERATOR);
                if (seperated.length != w.getCols()) {
                    throw new IOException("file is damaged ore missing");
                }
                for (int j = 0; j < w.getCols(); j++) {
                    w.set(i, j, Float.parseFloat(seperated[j]));
                }
            }
        }

        //load Bias Vectors
        for (Matrix b : this.biases) {
            if ((line = br.readLine()) == null) {
                throw new IOException("file is damaged ore missing");
            }
            seperated = line.split(DEFAULT_SEPERATOR);
            if (seperated.length != b.getRows() * b.getCols()) {
                throw new IOException("file is damaged ore missing");
            }
            for (int i = 0; i < b.getRows(); i++) {
                b.set(i, 0, Float.parseFloat(seperated[i]));
            }
        }
        System.out.println("loadet " + f.getName() + " successfully");
    }

    public double getErrorRate() {
        return errorRate;
    }

    public Matrix[] getWeights(){
        return weights;
    }

    public Matrix[] getBiases(){
        return biases;
    }
}
