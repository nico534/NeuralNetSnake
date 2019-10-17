package gui;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.net.URL;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field = new GameField(gamePane.getPrefWidth(), gamePane.getPrefHeight());
        gamePane.getChildren().add(field);
        field.drawField();
    }
}
