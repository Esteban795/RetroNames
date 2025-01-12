package linguacrypt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import linguacrypt.model.Settings;
import linguacrypt.scenes.SceneManager;

public class SettingsSceneController {

    private SceneManager sm;

    @FXML 
    private CheckBox audioCheckbox;

    @FXML
    private Slider volumeSlider;

    @FXML
    private CheckBox fisheyeCheckbox;

    @FXML 
    private CheckBox skylineCheckbox;

    @FXML
    private Button goBackButton;
    
    public SettingsSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        audioCheckbox.setSelected(true);
        Settings settings = Settings.getInstance();
        skylineCheckbox.setSelected(settings.isScanlines());
        fisheyeCheckbox.setSelected(settings.isFisheye());
        volumeSlider.setValue(settings.getSoundLevel());
        volumeSlider.setDisable(false);
    }

    @FXML
    public void handleAudioCheckbox() {
        if (!audioCheckbox.isSelected()) {
            volumeSlider.setValue(0);
            volumeSlider.setDisable(true);
        }  else {
            volumeSlider.setDisable(false);
        }
    }

    @FXML
    public void handleFisheyeCheckbox() {
        Settings settings = Settings.getInstance();

        if (fisheyeCheckbox.isSelected()) {
            settings.setFisheye(true);
        } else {
            settings.setFisheye(false);
        }
    }

    @FXML
    public void handleSkylineCheckbox() {
        Settings settings = Settings.getInstance();

        if (skylineCheckbox.isSelected()) {
            settings.setScanlines(true);
        } else {
            settings.setScanlines(false);
        }
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
    
}
