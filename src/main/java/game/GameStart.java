package game;

import clock.GameClock;
import gui.FXGui;
import gui.Gui;

import java.io.IOException;

public class GameStart {
    private Snake[] snakes;
    private String file = "Snake";
    public GameStart(Snake[] snakes){
        this.snakes = snakes;
    }

    public void runn() {
        System.out.println(FXGui.gameRuns);
        GameClock gc = new GameClock(snakes);
        gc.start();
    }

    public void stop(){

    }
}