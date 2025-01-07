package linguacrypt.controllers;

import java.io.IOException;

import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuSceneController {

    private SceneManager sm;

    @FXML
    private Button switchButton;

    @FXML
    private Button buttonBack;

    public MenuSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void switchScene() throws IOException {
        sm.pushScene(new LobbyScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}
