package org.example.controllers;

import org.example.scenes.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsController {

    private SceneManager sm;

    @FXML
    private Button buttonBack;

    public SettingsController(SceneManager sm) {
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
