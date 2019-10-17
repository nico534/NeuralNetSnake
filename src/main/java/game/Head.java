package game;

import Brain.NeuralNetwork;
import Mtrix.Matrix;
import actions.Main;

public class Head {
    Dir dir;
    private int x , y;

    Head() {
        this.dir = Dir.UP;
        this.x = GameValues.NR_OF_FIELDS.getValue() / 2;
        this.y = GameValues.NR_OF_FIELDS.getValue() / 2;
    }

    Dir getDir() {
        return dir;
    }

    void setDir(Dir dir) {
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
