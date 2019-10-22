package gui;

import game.Settings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.Set;

public class NSCControll implements Initializable {
    public Slider populationS;
    public Label populationL;

    public Slider mutValueS;
    public Label mutValueL;

    public Slider mutRateS;
    public Label mutRateL;

    public TextField networkSize;

    public Button create;

    private FXGui gui;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populationS.valueProperty().addListener((obs, oldval, newval) -> populationS.setValue(newval.intValue()));
        populationL.textProperty().bindBidirectional(populationS.valueProperty(), NumberFormat.getNumberInstance());
        populationS.setValue(Settings.population);

        mutValueS.valueProperty().addListener((obs, oldval, newval) -> mutValueS.setValue(newval.intValue()));
        mutValueL.textProperty().bindBidirectional(mutValueS.valueProperty(), NumberFormat.getNumberInstance());
        mutValueS.setValue(Settings.mutationValue);

        mutRateS.valueProperty().addListener((obs, oldval, newval) -> mutRateS.setValue(newval.intValue()));
        mutRateL.textProperty().bindBidirectional(mutRateS.valueProperty(), NumberFormat.getNumberInstance());
        mutRateS.setValue(Settings.mutationRate);

        create.setDisable(true);
    }

    public void setFxGui(FXGui gui){
        this.gui = gui;
    }

    public void networkSizeChanged(){
        if(networkSize.getText().matches("(([1-9]\\d*,)*)([1-9]\\d*)")){
            networkSize.setStyle("-fx-text-inner-color: black;");
            create.setDisable(false);
        }else {
            networkSize.setStyle("-fx-text-inner-color: red;");
            create.setDisable(true);
        }
    }

    public void updateMut(){
        Settings.mutationRate = (int)mutRateS.getValue();
        Settings.mutationValue = (int)mutValueS.getValue();
    }

    public void runGame(){
        String[] netStr = networkSize.getText().split(",");
        int[] netInt = new int[netStr.length];
        for(int i = 0; i < netStr.length; i++){
            netInt[i] = Integer.parseInt(netStr[i]);
        }

        Settings.NETWORK = netInt;
        gui.getGame().init((int)populationS.getValue());
        gui.runGame();
    }
}
