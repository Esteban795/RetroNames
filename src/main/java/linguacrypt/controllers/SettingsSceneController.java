package linguacrypt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import linguacrypt.scenes.SceneManager;

public class SettingsSceneController {

    private SceneManager sm;

    @FXML 
    private CheckBox audioCheckbox;

    @FXML
    private Slider volumeSlider;

    public SettingsSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        audioCheckbox.setSelected(true);
    }

    @FXML
    public void goBack() {
        sm.popScene();
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
}
