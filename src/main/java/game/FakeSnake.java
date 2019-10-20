package game;

import Mtrix.Matrix;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class FakeSnake {
    private Color headColor;
    private Color tailColor;
    private Color foodColor;
    private PickUp pickup;
    private Point headPoint;
    private Point[][] vision;
    private Matrix visionDistance;
    private ArrayList<Tail> tails;

    public FakeSnake(Color headColor, Color tailColor, Color foodColor, PickUp pickup, Point headPoint, Point[][] vision, Matrix visionDistance, ArrayList<Tail> tails) {
        this.headColor = headColor;
        this.tailColor = tailColor;
        this.foodColor = foodColor;
        this.pickup = pickup;
        this.headPoint = headPoint;
        this.vision = vision;
        this.visionDistance = visionDistance;
        this.tails = tails;
    }

    public ArrayList<Tail> getTails() {
        return tails;
    }

    public Color getHeadColor() {
        return headColor;
    }

    public Color getTailColor() {
        return tailColor;
    }

    public Color getFoodColor() {
        return foodColor;
    }

    public PickUp getPickup() {
        return pickup;
    }

    public Point getHeadPoint() {
        return headPoint;
    }

    public Point[][] getVision() {
        return vision;
    }

    public Matrix getVisionDistance() {
        return visionDistance;
    }
}
