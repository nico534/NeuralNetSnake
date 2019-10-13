package actions;

import clock.GameClock;
import game.Settings;
import game.Snake;
import gui.Gui;

import java.io.IOException;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        Snake[] snakes = new Snake[Settings.population];
        String file = "Snake";


        if(Settings.justBest){
            snakes = new Snake[1];
            snakes[0] = new Snake(0, file);
        }
        for (int i = 0; i < snakes.length; i++) {
            if(Settings.newGeneration){
                snakes[i] = new Snake(i);
            }
            else {
                snakes[i] = new Snake(i, file);
            }
        }

        Snake s = new Snake(1);
        Gui g = new Gui(snakes);
        GameClock gc = new GameClock(snakes);
        g.create();
        gc.start();
    }
}
