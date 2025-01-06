package org.example.controllers;

import org.example.scenes.SceneManager;
import org.example.scenes.SettingsScene;

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
    public void switchScene() {
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}
