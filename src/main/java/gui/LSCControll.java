package gui;

import game.Settings;
import game.Snake;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class LSCControll implements Initializable {
    public Slider mutValueS;
    public Label mutValueL;

    public Slider mutRateS;
    public Label mutRateL;

    public Button startButton;

    private FXGui gui;
    private File loadetFile;
    private Stage loadStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mutValueS.valueProperty().addListener((obs, oldval, newval) -> mutValueS.setValue(newval.intValue()));
        mutValueL.textProperty().bindBidirectional(mutValueS.valueProperty(), NumberFormat.getNumberInstance());
        mutValueS.setValue(Settings.mutationValue);

        mutRateS.valueProperty().addListener((obs, oldval, newval) -> mutRateS.setValue(newval.intValue()));
        mutRateL.textProperty().bindBidirectional(mutRateS.valueProperty(), NumberFormat.getNumberInstance());
        mutRateS.setValue(Settings.mutationRate);

        startButton.setDisable(true);
    }

    public void setUp(FXGui gui, Stage s){
        this.gui = gui;
        this.loadStage = s;
    }

    public void runGame(){
        this.gui.getGame().init(loadetFile.listFiles().length -1, loadetFile);
        this.gui.runGame();
    }

    public void loadFile(){
        DirectoryChooser saveDirChooser = new DirectoryChooser();
        saveDirChooser.setTitle("Save snakes");
        this.loadetFile = saveDirChooser.showDialog(loadStage);
        if(loadetFile != null && loadetFile.listFiles() != null && loadetFile.listFiles().length > 0){
            System.out.println(loadetFile.listFiles().length);
            startButton.setDisable(false);
        }
    }

    public void updateMut(){
        Settings.mutationRate = (int)mutRateS.getValue();
        Settings.mutationValue = (int)mutValueS.getValue();
    }

}
