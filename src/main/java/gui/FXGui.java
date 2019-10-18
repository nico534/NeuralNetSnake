package gui;

import game.GameStart;
import game.Settings;
import game.Snake;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class FXGui implements Initializable {
    public Button start;
    public Button stop;
    public Button save;

    public Label sleepTimeL;
    public Slider sleepTimeS;
    public Label mutValL;
    public Slider mutValS;
    public Label mutRatL;
    public Slider mutRatS;

    public Pane gamePane;

    private GameField field;
    private GameStart game;
    private boolean running;
    private Snake[] snakes;

    private Random r = new Random();
    private String file = "Snake";

    private Service<String> gameTask = new Service<String>() {
        @Override
        protected Task<String> createTask() {
            System.out.println("start game");
            runnGame();
            return null;
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field = new GameField(gamePane.getPrefWidth(), gamePane.getPrefHeight());
        gamePane.getChildren().add(field);
        field.drawField();

        snakes = new Snake[30];
        if(Settings.justBest){
            snakes = new Snake[1];
            try {
                snakes[0] = new Snake(0, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < snakes.length; i++) {
            if(Settings.newGeneration){
                snakes[i] = new Snake(i);
            }
            else {
                try {
                    snakes[i] = new Snake(i, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void startGame(){
        gameTask.start();
        running = true;
    }

    private void runnGame(){
        while (running){
            try {
                Thread.sleep(Settings.sleepTime);
                for(Snake s: snakes) {
                    s.move();
                    if (s.checkCollision()) {
                        if(Settings.stopByCoolssion) {
                            running = false;
                        } else {
                            Snake[] fittest = getFittest();
                            s.clearSnake(fittest[0], fittest[1]);
                        }
                    }
                    s.pickupCollision();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            KeyFrame update = new KeyFrame(Duration.seconds(0.5), event -> {
                for(Snake s: snakes){
                    field.drawSnake(s);
                }
            });
            Timeline t1 = new Timeline(update);
            t1.setCycleCount(Timeline.INDEFINITE);
            t1.play();
        }
        if(Settings.save) {
            JFileChooser jfc = new JFileChooser("F:/IntelliJProjects/Snake");
            jfc.showOpenDialog(null);
            for (Snake s : snakes) {
                s.saveNN(jfc.getCurrentDirectory());
            }
        }
    }

    private Snake[] getFittest(){
        Snake[] fittest = new Snake[2];

        int[][] snakeFitness = new int[snakes.length][2];

        for(int i = 0; i < snakes.length; i++){
            snakeFitness[i][0] = snakes[i].getFitness();
            snakeFitness[i][1] = i;
        }

        bubblesort(snakeFitness);
        int index = (int)Math.round(Math.pow(r.nextGaussian(),2))%snakes.length;
        fittest[0] = snakes[snakeFitness[index][1]];
        int index2 = (int)Math.round(Math.pow(r.nextGaussian(),2))%snakes.length;
        if(index2 == index){
            index2 = (index2+1)%snakes.length;
        }
        fittest[1] = snakes[snakeFitness[index2][1]];
        return fittest;
    }

    private void bubblesort(int[][] zusortieren) {
        int[] temp;
        for(int i=1; i<zusortieren.length; i++) {
            for(int j=0; j<zusortieren.length-i; j++) {
                if(zusortieren[j][0]<zusortieren[j+1][0]) {
                    temp=zusortieren[j];
                    zusortieren[j]=zusortieren[j+1];
                    zusortieren[j+1]=temp;
                }
            }
        }
    }
}
