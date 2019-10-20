package game;

import java.util.Random;

public class PickUp {
    private int x,y;
    private Random rn = new Random();

    private PickUp(int x, int y){
        this.x = x;
        this.y = y;
    }

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

    public PickUp copy(){
        return new PickUp(x, y);
    }
}