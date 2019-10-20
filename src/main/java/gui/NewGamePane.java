package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;


import java.io.IOException;

public class NewGamePane extends BorderPane {
    private Parent newSnakeScene;
    private Parent loadSnakeScene;

    private ChoiceBox<String> switchScenes;

    public NewGamePane() throws IOException {
        switchScenes = new ChoiceBox<>(FXCollections.observableArrayList("Load Snakes", "New Snakes"));
        switchScenes.setValue("New Snakes");
        switchScenes.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if(t1.intValue() == 0){
                setCenter(loadSnakeScene);
            } else {
                setCenter(newSnakeScene);
            }
        });
        newSnakeScene = new FXMLLoader(getClass().getResource("NewSnakeScene.fxml")).load();
        loadSnakeScene = new FXMLLoader(getClass().getResource("LoadSnakeScene.fxml")).load();
        setTop(switchScenes);
        setCenter(newSnakeScene);
    }
}
