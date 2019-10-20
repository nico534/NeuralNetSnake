package gui;


import game.FakeSnake;
import game.Settings;
import game.Tail;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class GameField extends Canvas {
    private GraphicsContext gc;
    private final int fieldNr = 20;
    private int fieldSize;
    private int fieldSpace = 2;
    private int xOff;
    private int yOff;

    public GameField(double width, double height){
        super(width, height);
        this.gc = this.getGraphicsContext2D();
        this.fieldSize = Math.min((int)(width / fieldNr), (int)(height / fieldNr));
        System.out.println(fieldSize);
        System.out.println(width + " " + height);
        xOff = (int)(width - fieldSize*fieldNr)/2 + fieldSpace/2;
        yOff = (int)(height - fieldSize*fieldNr)/2 + fieldSpace/2;
        this.fieldSize -= fieldSpace;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    public void drawField(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.LIGHTGRAY);
        for(int i = 0; i < fieldNr; i++){
            for(int j =0; j < fieldNr; j++){
                drawFieldRect(i,j);
            }
        }
    }

    public void drawSnake(FakeSnake s){

        // draw head
        gc.setFill(s.getHeadColor());
        drawFieldRect(s.getHeadPoint().x, s.getHeadPoint().y);

        //draw tail
        gc.setFill(s.getTailColor());
        for(Tail t : s.getTails()){
            drawFieldRect(t.getX(), t.getY());
        }

        // draw food
        gc.setFill(s.getFoodColor());
        drawFieldRect(s.getPickup().getX(), s.getPickup().getY());

        //draw vision
        if(Settings.showVision){
            drowVison(s);
        }
    }

    private void drowVison(FakeSnake s){
        Point[][] seenPoints = s.getVision();
        for(int i = 0; i < seenPoints.length; i++){
            for(int j = 0; j < seenPoints[i].length; j++){
                if(seenPoints[i][j] != null){
                    gc.setStroke(getViewColor(s, j, s.getVisionDistance().get(i,j)));
                    drawViewLine(seenPoints[i][j],s.getHeadPoint());
                }
            }
        }
    }

    private Color getViewColor(FakeSnake s, int i, double intensity){
        if(i == 0){
            return new Color(1-intensity, 1-intensity, intensity, 1.0);
        }else if(i == 1){
            return new Color(intensity, 0, 1-intensity, 1.0);
        }else if(i == 2){
            return new Color(1-intensity, intensity, 0, 1.0);
        }
        return null;
    }

    public void drawFieldRect(int i, int j){
        gc.fillRect(i*(fieldSize + fieldSpace) + xOff, j*(fieldSize + fieldSpace) + yOff, fieldSize, fieldSize);
    }

    private void drawViewLine(Point seenPoint, Point headPoint){
        Point seenPos = getFieldPos(seenPoint);
        Point headPpos = getFieldPos(headPoint);

        // put vision in center
        seenPos.x += fieldSize/2;
        seenPos.y += fieldSize/2;
        headPpos.x += fieldSize/2;
        headPpos.y += fieldSize/2;

        gc.strokeLine(headPpos.x, headPpos.y, seenPos.x, seenPos.y);
    }

    private Point getFieldPos(Point p){
        return new Point(p.x*(fieldSize + fieldSpace) + xOff, p.y*(fieldSize + fieldSpace) + yOff);
    }
}
