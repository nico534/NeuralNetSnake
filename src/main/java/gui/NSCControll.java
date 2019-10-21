package gui;

import game.Settings;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class NSCControll implements Initializable {
    public Slider populationS;
    public Label populationL;

    public Slider mutValueS;
    public Label mutValueL;

    public Slider mutRateS;
    public Label mutRateL;

    public TextField networkSize;

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
    }

    public void setFxGui(FXGui gui){
        this.gui = gui;
    }
}
