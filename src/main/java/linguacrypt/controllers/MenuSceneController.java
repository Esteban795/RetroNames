package linguacrypt.controllers;

import java.io.IOException;

import linguacrypt.scenes.EditDecksScene;
import linguacrypt.scenes.NewLoadScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuSceneController {
    private SceneManager sm;

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    
    public MenuSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void handlePlay() {
        sm.pushScene(new NewLoadScene(sm));
    }

    @FXML
    public void handleSettings() {
        System.out.println("Settings button clicked!");
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void handleEditDecks() {
        System.out.println("Edit Decks button clicked!");
        sm.pushScene(new EditDecksScene(sm));
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