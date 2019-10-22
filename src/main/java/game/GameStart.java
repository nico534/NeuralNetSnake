package game;

import clock.GameClock;
import gui.FXGui;
import gui.Gui;

import java.io.File;
import java.io.IOException;

public class GameStart {
    private Snake[] snakes;
    private String file = "Snake";

    public void runn() {
        System.out.println(FXGui.gameRuns);
        GameClock gc = new GameClock(snakes);
        gc.start();
    }

    public void init(int snakeCount){
        snakes = new Snake[snakeCount];
        if (Settings.justBest) {
            snakes = new Snake[1];
            try {
                snakes[0] = new Snake(0, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < snakes.length; i++) {
            if (Settings.newGeneration) {
                snakes[i] = new Snake(i);
            } else {
                try {
                    snakes[i] = new Snake(i, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init(int snakeCount, File file){
        snakes = new Snake[snakeCount];
        if (Settings.justBest) {
            snakes = new Snake[1];
            try {
                snakes[0] = new Snake(0, file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < snakes.length; i++) {
            if (Settings.newGeneration) {
                snakes[i] = new Snake(i);
            } else {
                try {
                    snakes[i] = new Snake(i, file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Snake[] getSnakes(){
        return snakes;
    }

    public Snake getPlayerSnake(){
        if(snakes != null){
            return snakes[snakes.length-1];
        }
        return null;
    }

    public Snake getPlayerSnake2(){
        if(snakes.length < 2){
            return null;
        }
        return snakes[snakes.length -2];
    }

    public boolean hasSnakes(){
        if(snakes == null || snakes[0] == null){
            return false;
        }
        return true;
    }

    public void stop(){

    }
}