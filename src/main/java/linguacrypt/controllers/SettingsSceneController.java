package linguacrypt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import linguacrypt.scenes.SceneManager;

public class SettingsSceneController {

    private SceneManager sm;

    @FXML
    private Button buttonBack;

    public SettingsSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}
