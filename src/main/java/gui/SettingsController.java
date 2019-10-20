package gui;

import game.Settings;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    public CheckBox humanPlayer;
    public CheckBox goThroughWalls;
    public CheckBox inherit;
    public CheckBox die;
    public CheckBox bot;
    public CheckBox showJustBest;
    public CheckBox stopByCollision;
    public CheckBox bestWaits;
    public CheckBox showVision;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        humanPlayer.setSelected(Settings.humanPlayer);
        humanPlayer.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.humanPlayer = t1);

        goThroughWalls.setSelected(Settings.goThroughWalls);
        goThroughWalls.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.goThroughWalls = t1);

        inherit.setSelected(Settings.inherit);
        inherit.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.inherit = t1);

        die.setSelected(Settings.die);
        die.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.die = t1);

        showJustBest.setSelected(Settings.showJustBest);
        showJustBest.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.showJustBest = t1);

        stopByCollision.setSelected(Settings.stopByCollision);
        stopByCollision.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.stopByCollision = t1);

        bestWaits.setSelected(Settings.bestWaits);
        bestWaits.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.bestWaits = t1);

        showVision.setSelected(Settings.showVision);
        showVision.selectedProperty().addListener((observableValue, aBoolean, t1) -> Settings.showVision = t1);
    }
}
