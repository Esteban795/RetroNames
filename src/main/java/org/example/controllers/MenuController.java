package org.example.controllers;

import java.io.IOException;

import org.example.scenes.SceneManager;
import org.example.scenes.SettingsScene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    private SceneManager sm;

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    
    public MenuController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void handlePlay() {
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void handleSettings() {
        System.out.println("Settings button clicked!");
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void handleButtonEditDecks() {
        System.out.println("Edit Decks button clicked!");
    }

    @FXML
    public void handleSaveGame(){
        System.out.println("Save Game button clicked!");
    }

    @FXML
    public void handleQuit(){
        Platform.exit();
    }

    @FXML
    private void initialize() {
        // Called after FXML is loaded
        // Initialize your buttons here
    }
}