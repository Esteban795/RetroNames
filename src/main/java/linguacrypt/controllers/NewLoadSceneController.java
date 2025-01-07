package linguacrypt.controllers;

import linguacrypt.scenes.SceneManager;

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
    public void newGame() {
        System.out.println("New Game button clicked!");
    }
    
    @FXML
    public void loadGame() {
        System.out.println("Load Game button clicked!");
    }
}
