package linguacrypt.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import linguacrypt.model.Game;
import linguacrypt.scenes.GameScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.visitor.DeserializationVisitor;

public class LoadMenuSceneController {
    
    private final SceneManager sm;
    
    @FXML
    private Button latestGameBtn;
    @FXML
    private VBox savedGamesBox;

    public LoadMenuSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        setupLatestGameButton();
        loadSavedGames();
    }

    private void setupLatestGameButton() {
        latestGameBtn.setOnAction(e -> loadLatestGame());
    }

    private void loadSavedGames() {
        File directory = new File("src/main/resources/saves/");
        File[] files = directory.listFiles((dir, name) -> 
            name.startsWith("game") && name.endsWith(".json"));
        
        if (files != null && files.length > 0) {
            Arrays.sort(files, (f1, f2) -> 
                Long.compare(f2.lastModified(), f1.lastModified()));
            
            for (int i = 0; i < Math.min(3, files.length); i++) {
                final File saveFile = files[i];
                Button saveBtn = new Button(saveFile.getName());
                saveBtn.setPrefWidth(200);
                saveBtn.setOnAction(e -> loadSpecificGame(saveFile.getPath()));
                savedGamesBox.getChildren().add(saveBtn);
            }
        }
    }

    private void loadLatestGame() {
        DeserializationVisitor visitor = new DeserializationVisitor();
        Game loadedGame = visitor.loadLatestGame();
        if (loadedGame != null) {
            System.out.println("Loading latest game...");
            loadGame(loadedGame);
        }
    }

    private void loadSpecificGame(String filePath) {
        DeserializationVisitor visitor = new DeserializationVisitor();
        Game loadedGame = visitor.loadGame(filePath);
        if (loadedGame != null) {
            loadGame(loadedGame);
        }
    }

    private void loadGame(Game loadedGame) {
        sm.getModel().setGame(loadedGame);
        try {
            System.out.println("Loading game scene...");
            sm.pushScene(new GameScene(sm));
        } catch (IOException e) {
            System.err.println("Error loading game scene: " + e.getMessage());
        }
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}