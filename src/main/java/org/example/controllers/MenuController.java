package org.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    
    @FXML
    public void handleButtonPlay() {
        System.out.println("Play button clicked!");
    }

    @FXML
    public void handleButtonSettings() {
        System.out.println("Settings button clicked!");
    }

    @FXML
    public void handleButtonEditDecks() {
        System.out.println("Edit Decks button clicked!");
    }

    @FXML
    public void handleButtonQuitGame() {
        Platform.exit();
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