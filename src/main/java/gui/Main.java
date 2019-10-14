package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent mainScene = FXMLLoader.load(getClass().getResource("SnakeScene.fxml"));
        primaryStage.setTitle("Neural Network");
        primaryStage.setScene(new Scene(mainScene, 600, 430));
        primaryStage.show();
    }
}
