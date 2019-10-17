package gui;


import game.Settings;
import game.Snake;
import game.Tail;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        xOff = (int)(width - fieldSize*fieldNr)/2;
        yOff = (int)(height - fieldSize*fieldNr)/2;
        this.fieldSize -= fieldSpace;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    public void drawField(){
        gc.setFill(Color.LIGHTGRAY);
        System.out.println(Color.LIGHTGRAY.getRed() + " " + Color.LIGHTGRAY.getGreen() +" "+ Color.LIGHTGRAY.getBlue() + " " + Color.LIGHTGRAY.getBrightness());
        for(int i = 0; i < fieldNr; i++){
            for(int j =0; j < fieldNr; j++){
                drawFieldRect(i,j);
            }
        }
    }

    public void drawSnake(Snake s){
        gc.setFill(s.getColor());
        for(Tail t : s.getTails()){
            drawFieldRect(t.getX(), t.getY());
        }
        drawFieldRect(s.getHead().getX(), s.getHead().getY());
    }

    public void drawFieldRect(int i, int j){
        gc.fillRect(i*(fieldSize + fieldSpace) + xOff, j*(fieldSize + fieldSpace) + yOff, fieldSize, fieldSize);
    }

    public void drawLine(double v1, double v2, double v3, double v4){
        gc.strokeLine(v1, v2, v3, v4);
    }
}
