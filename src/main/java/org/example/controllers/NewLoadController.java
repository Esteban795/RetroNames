package org.example.controllers;

import org.example.scenes.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NewLoadController {

    private SceneManager sm;

    @FXML
    private Button buttonBack;


    public NewLoadController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void goBack() {
        sm.popScene();
    }

    @FXML
    public void newGame() {
        System.out.println("New Game button clicked!");
    }
    
    @FXML
    public void loadGame() {
        System.out.println("Load Game button clicked!");
    }
}
