package game;

import Brain.NeuralNetwork;
import Mtrix.Matrix;
import actions.Main;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

public class UIHead extends Head {

    private Matrix vision;
    private int[][] drowVision;
    private int[][] prevDVision;
    private Matrix prevVision;
    private NeuralNetwork brain;

    public UIHead() {
        super();
        vision = new Matrix(7, 3, false);
        prevVision = new Matrix(7, 3, false);
        brain = new NeuralNetwork(Settings.NETWORK);
        drowVision = new int[7][3];
        prevDVision = new int[7][3];
    }
    public UIHead(File nnFile) throws IOException {
        super();
        vision = new Matrix(7, 3, false);
        prevVision = new Matrix(7, 3, false);
        brain = new NeuralNetwork(nnFile);
        drowVision = new int[7][3];
        prevDVision = new int[7][3];
    }

    public void inherit(Snake s1, Snake s2) {
        brain.crossover(s1.getHead().brain, s2.getHead().brain, s1.getFitness(), s2.getFitness());
    }

    public void resetPosition() {
        this.setX(GameValues.NR_OF_FIELDS.getValue() / 2);
        this.setY(GameValues.NR_OF_FIELDS.getValue() / 2);
        this.dir = Dir.UP;
    }

    void desideDirektion(byte[][] board) {
        updateVision(board);

        // vision is a Vector for the neural Network
        vision.setShowAsVector(true);
        int turnTo = brain.predict(vision);

        if(Settings.trainWithBackprob){
            turnTo = brain.train(vision, bestChoise(), Settings.learningRate);
        }

        if (Settings.humanPlayer) {
            return;
        } else if (Settings.bot) {
            turnTo = bestChoise().getMaxIndex();
        }

        if (dir == Dir.UP) {
            if (turnTo == 0) {
                dir = Dir.LEFT;
            } else if (turnTo == 2) {
                dir = Dir.RIGHT;
            }
        } else if (dir == Dir.DOWN) {
            if (turnTo == 0) {
                dir = Dir.RIGHT;
            } else if (turnTo == 2) {
                dir = Dir.LEFT;
            }
        } else if (dir == Dir.RIGHT) {
            if (turnTo == 0) {
                dir = Dir.UP;
            } else if (turnTo == 2) {
                dir = Dir.DOWN;
            }
        } else {
            if (turnTo == 0) {
                dir = Dir.DOWN;
            } else if (turnTo == 2) {
                dir = Dir.UP;
            }
        }
    }

    void updateVision(byte[][] board) {
        boolean[] detectWall = new boolean[7];

        int[][] side;
        if (dir == Dir.UP) {
            side = DirMutilayer.UP.getValue();
        } else if (dir == Dir.DOWN) {
            side = DirMutilayer.DOWN.getValue();
        } else if (dir == Dir.RIGHT) {
            side = DirMutilayer.RIGHT.getValue();
        } else {
            side = DirMutilayer.LEFT.getValue();
        }
        this.prevDVision = drowVision;
        this.prevVision = vision.copy();

        this.vision.reset();
        this.drowVision = new int[7][3];

        int c = 1;
        while (notAllDetected(detectWall)) {
            for (int i = 0; i < 7; i++) {
                int checkX = getX() + (side[i][0] * c);
                int checkY = getY() + (side[i][1] * c);
                if (detectWal(side[i][0] * c, side[i][1] * c)) {
                    if (!detectWall[i]) {
                        if (side[i][0] != 0 && side[i][1] != 0 && Settings.goThroughWalls) {
                            checkX = checkX % GameValues.NR_OF_FIELDS.getValue();
                            if (checkX < 0) {
                                checkX += GameValues.NR_OF_FIELDS.getValue();
                            }
                            checkY = checkY % GameValues.NR_OF_FIELDS.getValue();
                            if (checkY < 0) {
                                checkY += GameValues.NR_OF_FIELDS.getValue();
                            }
                            if (checkX == getX() && checkY == getY()) {
                                detectWall[i] = true;
                            } else if (!detectWall[i]) {
                                //Player
                                if (board[checkX][checkY] == 1) {
                                    vision.set(i, 1, (float) (1.0 / Math.pow(c, (2.0 / 3.0))));
                                    drowVision[i][1] = c;
                                    detectWall[i] = true;
                                    //Food
                                } else if (board[checkX][checkY] == 2) {
                                    vision.set(i, 2, (float) (1 / Math.pow(c, 2.0 / 3.0)));
                                    drowVision[i][2] = c;
                                }
                            }
                        }

                        //Wall
                        detectWall[i] = true;
                        vision.set(i, 0, (float) (1 / Math.pow(c, 2.0 / 3.0)));
                        drowVision[i][0] = c;
                    }
                } else {
                    //Player
                    if (board[checkX][checkY] == 1) {
                        vision.set(i, 1, (float) (1 / Math.pow(c, 2.0 / 3.0)));
                        drowVision[i][1] = c;
                        //Food
                    } else if (board[checkX][checkY] == 2) {
                        vision.set(i, 2, (float) (1 / Math.pow(c, 2.0 / 3.0)));
                        drowVision[i][2] = c;
                    }
                }
            }
            c++;
        }
    }

    int[][] getDrowVision() {
        return drowVision;
    }

    Matrix getVision() {
        return vision;
    }

    private boolean detectWal(int xOff, int yOff) {
        if (this.getX() + xOff < 0 || this.getY() + yOff < 0 || this.getX() + xOff > GameValues.NR_OF_FIELDS.getValue() - 1 || this.getY() + yOff > GameValues.NR_OF_FIELDS.getValue() - 1) {
            return true;
        }
        return false;
    }

    private boolean notAllDetected(boolean[] a) {
        for (boolean i : a) {
            if (!i) {
                return true;
            }
        }
        return false;
    }

    private Matrix bestChoise() {
        Matrix chose = new Matrix(3, false);

        int[] minWall = new int[2];
        int[] minSelf = new int[2];
        int[] minSnack = new int[2];
        minSnack[0] = 21;
        minSelf[0] = 21;
        minWall[0] = 21;
        Random r = new Random();

        for (int i = 0; i < 7; i++) {
            if (drowVision[i][0] != 0 && drowVision[i][0] < minWall[0]) {
                minWall[0] = drowVision[i][0];
                minWall[1] = i;
            }
            if (drowVision[i][1] != 0 && drowVision[i][1] < minSelf[0]) {
                minSelf[0] = drowVision[i][1];
                minSelf[1] = i;
            }
            if (drowVision[i][2] != 0 && drowVision[i][2] < minSnack[0]) {
                minSnack[0] = drowVision[i][1];
                minSnack[1] = i;
            }
        }

        // go to snack if best
        if ((minSnack[1] != minSelf[1] || minSelf[0] == 21) && minSnack[0] != 21 && vision.get(minSelf[1],1) < 1) {
            // snack left go left
            if (minSnack[1] < 2 && (minSelf[1] > 2 || minSnack[0] < minSelf[0])) {
                if(minSelf[1] < 2)
                chose.set(0, 1);
                return chose;
                // snack strait go strait
            } else if  (minSnack[1] < 5  &&   (minSelf[1]<2|| minSelf[1]>5 || minSnack[0] < minSelf[0]) ) {
                chose.set(1, 1);
                return chose;
                // snack right ro right
            } else if(minSelf[1] < 5 || minSnack[0] < minSelf[0]) {
                chose.set(2, 1);
                return chose;
            }
        }

        //direkt hit
        if (minSelf[0] > 0 && minSelf[0] < 3) {
            //frontal
            if (minSelf[1] > 1 && minSelf[1] < 5) {
                if ((vision.get(1, 1) > vision.get(5, 1))) {
                    //go right
                    chose.set(2, 1);
                    return chose;
                } else {
                    //go left
                    chose.set(0, 1);
                    return chose;
                    //pos hit left
                }
                //left
            } else if (minSelf[1] < 2) {
                if (vision.get(3, 1) > vision.get(5, 1)) {
                    //go right
                    chose.set(2, 1);
                    return chose;
                } else {
                    //go Strait
                    chose.set(1, 1);
                    return chose;
                }
            } else {
                if (vision.get(3, 1) > vision.get(1, 1)) {
                    //go left
                    chose.set(0, 1);
                    return chose;
                } else {
                    //go Strait
                    chose.set(1, 1);
                    return chose;
                }
            }
        }

        // does not matter witch direction, go strait
        chose.set(1, 1);
        return chose;
    }

    private Matrix bestChoiseDev() {
        Matrix chose = new Matrix(3, false);

        int[] minWall = new int[2];
        int[] minSelf = new int[2];
        int[] minSnack = new int[2];
        minSnack[0] = 21;
        minSelf[0] = 21;
        minWall[0] = 21;
        Random r = new Random();

        for (int i = 0; i < 7; i++) {
            if (drowVision[i][0] != 0 && drowVision[i][0] < minWall[0]) {
                minWall[0] = drowVision[i][0];
                minWall[1] = i;
            }
            if (drowVision[i][1] != 0 && drowVision[i][1] < minSelf[0]) {
                minSelf[0] = drowVision[i][1];
                minSelf[1] = i;
            }
            if (drowVision[i][2] != 0 && drowVision[i][2] < minSnack[0]) {
                minSnack[0] = drowVision[i][1];
                minSnack[1] = i;
            }
        }

        // go to snack if best
        if ((minSnack[1] != minSelf[1] || minSelf[0] == 21) && minSnack[0] != 21 && vision.get(minSelf[1],1) < 1) {
            System.out.println(vision.get(minSelf[1],1));
            // snack left go left
            if (minSnack[1] < 2 && (minSelf[1] > 2 || minSnack[0] < minSelf[0])) {
                if(minSelf[1] < 2)
                    chose.set(0, 1);
                //return chose;
                System.out.println("left: food");
                return chose;
                // snack strait go strait
            } else if  (minSnack[1] < 5  &&   (minSelf[1]<2|| minSelf[1]>5 || minSnack[0] < minSelf[0]) ) {
                chose.set(1, 1);
                //return chose;
                System.out.println("strait: food");
                return chose;
                // snack right ro right
            } else if(minSelf[1] < 5 || minSnack[0] < minSelf[0]) {
                chose.set(2, 1);
                //return chose;
                System.out.println("right: food");
                return chose;
            }
        }

        //direkt hit
        if (minSelf[0] > 0 && minSelf[0] < 3) {
            //frontal
            if (minSelf[1] > 1 && minSelf[1] < 5) {
                if ((vision.get(1, 1) > vision.get(5, 1))) {
                    //go right
                    chose.set(2, 1);
                    System.out.println("right: hitS");
                    System.out.println(vision.get(1, 1) + "L, "  + vision.get(5, 1) + "R");
                    return chose;
                } else {
                    //go left
                    chose.set(0, 1);
                    System.out.println("left: hitS");
                    System.out.println(vision.get(1, 1) + "L, "  + vision.get(5, 1) + "R");
                    return chose;
                    //pos hit left
                }
                //left
            } else if (minSelf[1] < 2) {
                if (vision.get(3, 1) > vision.get(5, 1)) {
                    //go right
                    chose.set(2, 1);
                    System.out.println("right: hitL");
                    System.out.println(vision.get(3, 1) + "S, "  + vision.get(5, 1) + "R");
                    return chose;
                } else {
                    //go Strait
                    chose.set(1, 1);
                    System.out.println("strait: hitL");
                    System.out.println(vision.get(3, 1) + "S, "  + vision.get(5, 1) + "R");
                    return chose;
                }
            } else {
                if (vision.get(3, 1) > vision.get(1, 1)) {
                    //go left
                    chose.set(0, 1);
                    System.out.println("left: hitR");
                    System.out.println(vision.get(3, 1) + "S, "  + vision.get(1, 1) + "L");
                    return chose;
                } else {
                    //go Strait
                    chose.set(1, 1);
                    System.out.println("strait: hitR");
                    System.out.println(vision.get(3, 1) + "S, "  + vision.get(1, 1) + "L");
                    return chose;
                }
            }
        }

        // does not matter witch direction, go strait
        chose.set(1, 1);
        return chose;
    }

    public NeuralNetwork getNN(){
        return brain;
    }

}
