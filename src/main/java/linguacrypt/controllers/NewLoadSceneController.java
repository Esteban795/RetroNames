package linguacrypt.controllers;

import linguacrypt.scenes.LoadMenuScene;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NewLoadSceneController {

    private SceneManager sm;

    @FXML
    private Button buttonBack;


    public NewLoadSceneController(SceneManager sm) {
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
    public void newGame() throws IOException {
        System.out.println("New Game button clicked!");
        sm.pushScene(new LobbyScene(sm));
    }
    
    @FXML
    public void loadGame() throws IOException {
        System.out.println("Load Game button clicked!");
        sm.pushScene(new LoadMenuScene(sm));
    }
}
