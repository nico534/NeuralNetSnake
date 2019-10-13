package game;

import java.util.Random;

public class PickUp {
    private int x,y;
    private Random rn = new Random();

    public PickUp(){
        reset();
    }

    public void reset(){
        this.x = rn.nextInt(GameValues.NR_OF_FIELDS.getValue());
        this.y = rn.nextInt(GameValues.NR_OF_FIELDS.getValue());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}