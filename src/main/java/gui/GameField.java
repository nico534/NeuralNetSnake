package gui;


import game.Settings;
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

    public GameField(int width, int height){
        super(width, height);
        this.gc = this.getGraphicsContext2D();
        this.fieldSize = (int) Math.min(Math.ceil(((double)width / fieldNr)), Math.ceil(Math.ceil((double)height / fieldNr)));
        xOff = (width - fieldSize*fieldNr)/2;
        yOff = (height - fieldSize*fieldNr)/2;
        this.fieldSize -= fieldSpace;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    public void drawField(){
        gc.setFill(Color.LIGHTGRAY);
        for(int i = 0; i < fieldSize; i++){
            for(int j =0; j < fieldSize; j++){
                gc.fillRect(i*(fieldSize + fieldSpace), j*(fieldSize + fieldSpace), (i+1)*fieldSpace, (j+1)*fieldSpace);
            }
        }
    }
}
