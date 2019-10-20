package gui;

import game.GameStart;
import game.Settings;
import game.Snake;
import javafx.animation.AnimationTimer;
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
import java.text.NumberFormat;
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
    public static boolean gameRuns = false;
    private Snake[] snakes;

    private Random r = new Random();
    private String file = "Snake";

    private Thread gameThread;

    private AnimationTimer updateGameView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field = new GameField(gamePane.getPrefWidth(), gamePane.getPrefHeight());
        gamePane.getChildren().add(field);
        field.drawField();

        snakes = new Snake[1];
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
        game = new GameStart(snakes);

        gameThread = new Thread(() -> {
            game.runn();
        });

        updateGameView =  new AnimationTimer() {
            @Override
            public void handle(long l) {
                field.drawField();
                for(Snake s: snakes) {
                    field.drawSnake(s.createFakeSnake());
                }
            }
        };

        sleepTimeL.setText(Settings.sleepTime + "");
        sleepTimeS.setValue(Settings.sleepTime);

        mutValL.setText(Settings.mutationValue + "");
        mutValS.setValue(Settings.mutationValue);

        mutRatL.setText(Settings.mutationRate + "");
        mutRatS.setValue(Settings.mutationRate);
    }

    public void startGame() {
        gameRuns = true;
        gameThread.start();
        System.out.println("start at");
        updateGameView.start();
    }

    public void stopGame(){
        gameRuns = false;
        updateGameView.stop();
    }

    public void updateSleepTime(){
        sleepTimeL.setText((int)sleepTimeS.getValue() + "");
        Settings.sleepTime = (int)sleepTimeS.getValue();
    }
    public void updateMutValue(){
        mutValL.setText((int)mutValS.getValue() + "");
        Settings.mutationValue = (int)mutValS.getValue();
    }
    public void updateMutRate(){
        mutRatL.setText((int)mutRatS.getValue() + "");
        Settings.mutationRate = (int)mutRatS.getValue();
    }
}
