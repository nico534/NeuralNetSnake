package gui;

import game.GameStart;
import game.Settings;
import game.Snake;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
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
    public static boolean gameRuns = false;

    private Random r = new Random();
    private String file = "Snake";

    private Thread gameThread;

    private AnimationTimer updateGameView;

    private Stage saveStage;
    private Stage settingsStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field = new GameField(gamePane.getPrefWidth(), gamePane.getPrefHeight());
        gamePane.getChildren().add(field);
        field.drawField();
        game = new GameStart();

        sleepTimeL.setText(Settings.sleepTime + "");
        sleepTimeS.setValue(Settings.sleepTime);

        mutValL.setText(Settings.mutationValue + "");
        mutValS.setValue(Settings.mutationValue);

        mutRatL.setText(Settings.mutationRate + "");
        mutRatS.setValue(Settings.mutationRate);
    }

    public void startGame() {
        NewGamePane ngp = null;
        try {
            ngp = new NewGamePane(this);
            settingsStage.setScene(new Scene(ngp));
            settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runGame(){
        gameRuns = true;
        updateGameView =  new AnimationTimer() {
            @Override
            public void handle(long l) {
                field.drawField();
                for(Snake s: game.getSnakes()) {
                    field.drawSnake(s.createFakeSnake());
                }
            }
        };

        gameThread = new Thread(() -> {
            game.runn();
        });

        gameThread.start();
        System.out.println("start at");
        updateGameView.start();

    }

    public void stopGame(){
        gameRuns = false;
        updateGameView.stop();
    }

    public void saveGame(){
        DirectoryChooser saveDirChooser = new DirectoryChooser();
        saveDirChooser.setTitle("Save snakes");
        File saveAs = saveDirChooser.showDialog(saveStage);
        if(saveAs != null) {
            for (Snake s : game.getSnakes()) {
                s.saveNN(saveAs);
            }
        }
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

    public void createStages(Stage mainStage) {

        this.saveStage = mainStage;
        this.settingsStage = new Stage();
        this.settingsStage.initModality(Modality.WINDOW_MODAL);
        this.settingsStage.initOwner(mainStage);
    }

    public void openSettings(){
        Parent settingScene = null;
        try {
            System.out.println("Settings");
            settingScene = new FXMLLoader(getClass().getResource("SettingsScene.fxml")).load();
            this.settingsStage.setScene(new Scene(settingScene));
            this.settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void keyboardPressed(KeyEvent event){
        if(!game.hasSnakes()){
            return;
        }
        switch (event.getCode()){
            case W:
                game.getPlayerSnake().goUp();
                return;
            case A:
                game.getPlayerSnake().goLeft();
                return;
            case S:
                game.getPlayerSnake().goDown();
                return;
            case D:
                game.getPlayerSnake().goRight();
        }
    }

    public GameStart getGame(){
        return this.game;
    }
}