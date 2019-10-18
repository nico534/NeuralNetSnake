package game;

import clock.GameClock;
import gui.Gui;

import java.io.IOException;

public class GameStart {
    Snake[] snakes;
    String file = "Snake";
    public GameStart(Snake[] snakes){
        this.snakes = snakes;
    }

    public void runn() throws IOException {
        if (Settings.justBest) {
            snakes = new Snake[1];
            snakes[0] = new Snake(0, file);
        }
        for (
                int i = 0;
                i < snakes.length; i++) {
            if (Settings.newGeneration) {
                snakes[i] = new Snake(i);
            } else {
                snakes[i] = new Snake(i, file);
            }
        }

        Snake s = new Snake(1);
        GameClock gc = new GameClock(snakes);
        gc.start();
    }
}
