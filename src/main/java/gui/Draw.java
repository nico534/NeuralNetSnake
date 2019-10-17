package gui;

import actions.Main;
import game.GameValues;
import game.Settings;
import game.Snake;
import game.Tail;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Draw extends JLabel {
    Point p;
    Snake[] snakes;

    public Draw(Snake[] snakes){
        super();
        this.snakes = snakes;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        //Draw Background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0, GameValues.GAME_WIDTH.getValue(),GameValues.GAME_HEIGHT.getValue());

        //draw Snakes
        for(Snake s: snakes) {
            //s.draw(g);
        }

        //Draw Grid
        g.setColor(Color.GRAY);
        for(int i = 0; i < GameValues.NR_OF_FIELDS.getValue(); i++){
            for(int j = 0; j < GameValues.NR_OF_FIELDS.getValue(); j++){
                g.drawRect(i*GameValues.FIELD_SIZE.getValue()+GameValues.X_OFF.getValue(),
                        j*GameValues.FIELD_SIZE.getValue()+GameValues.Y_OFF.getValue(),
                        GameValues.FIELD_SIZE.getValue(), GameValues.FIELD_SIZE.getValue());
            }
        }

        //Draw Border
        g.setColor(Color.BLACK);
        g.drawRect(GameValues.X_OFF.getValue(), GameValues.Y_OFF.getValue(), GameValues.FIELD_SIZE.getValue() * GameValues.NR_OF_FIELDS.getValue(),GameValues.FIELD_SIZE.getValue() * GameValues.NR_OF_FIELDS.getValue());

        g.drawString(Long.toString(Settings.generation), 300, 15);
        repaint();
    }
}
