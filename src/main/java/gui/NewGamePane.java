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

    public NewGamePane(FXGui gui) throws IOException {
        switchScenes = new ChoiceBox<>(FXCollections.observableArrayList("Load Snakes", "New Snakes"));
        switchScenes.setValue("New Snakes");
        switchScenes.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if(t1.intValue() == 0){
                setCenter(loadSnakeScene);
            } else {
                setCenter(newSnakeScene);
            }
        });
        FXMLLoader loadNew = new FXMLLoader(getClass().getResource("NewSnakeScene.fxml"));
        newSnakeScene = loadNew.load();
        NSCControll newController = loadNew.getController();
        newController.setFxGui(gui);

        FXMLLoader loadLoad = new FXMLLoader(getClass().getResource("LoadSnakeScene.fxml"));
        loadSnakeScene = loadLoad.load();
        LSCControll loadContrtoller = loadLoad.getController();
        loadContrtoller.setUp(gui);


        setTop(switchScenes);
        setCenter(newSnakeScene);
    }
}
