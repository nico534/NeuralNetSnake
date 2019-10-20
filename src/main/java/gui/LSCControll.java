package gui;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class LSCControll implements Initializable {
    public Slider mutValueS;
    public Label mutValueL;

    public Slider mutRateS;
    public Label mutRateL;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mutValueS.valueProperty().addListener((obs, oldval, newval) -> mutValueS.setValue(newval.intValue()));
        mutValueL.textProperty().bindBidirectional(mutValueS.valueProperty(), NumberFormat.getNumberInstance());

        mutRateS.valueProperty().addListener((obs, oldval, newval) -> mutRateS.setValue(newval.intValue()));
        mutRateL.textProperty().bindBidirectional(mutRateS.valueProperty(), NumberFormat.getNumberInstance());
    }
}
