package linguacrypt.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import linguacrypt.exception.CorruptedSaveException;
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
    @FXML
    private Label errorLabel;
    @FXML
    private Button browseSavesBtn;

    public LoadMenuSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        setupLatestGameButton();
        setupBrowseSavesButton();
        loadSavedGames();
    }

    private void setupBrowseSavesButton() {
        browseSavesBtn.setOnAction(e -> openFileChooser());
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Save File");
        fileChooser.setInitialDirectory(new File("src/main/resources/saves/"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(sm.getPrimaryStage());
        if (selectedFile != null && !isCorruptedSave(selectedFile)) {
            loadSpecificGame(selectedFile.getPath());
        } else if (selectedFile != null) {
            errorLabel.setText("Selected save file is corrupted");
            errorLabel.setVisible(true);
        }
    }

    private void setupLatestGameButton() {
        latestGameBtn.setOnAction(e -> loadLatestGame());
    }

    private boolean isCorruptedSave(File saveFile) {
        try {
            DeserializationVisitor visitor = new DeserializationVisitor();
            visitor.loadGame(saveFile.getPath());
            return false;
        } catch (CorruptedSaveException e) {
            return true;
        }
    }

    private void loadSavedGames() {
        File directory = new File("src/main/resources/saves/");
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));

        ArrayList<String> corruptedFiles = new ArrayList<>();

        if (files != null && files.length > 0) {
            Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

            for (int i = 0, validSaves = 0; i < files.length && validSaves < 3; i++) {
                final File saveFile = files[i];
                if (!isCorruptedSave(saveFile)) {
                    Button saveBtn = new Button(saveFile.getName());
                    saveBtn.getStyleClass().add("menu-button");
                    saveBtn.setOnAction(e -> loadSpecificGame(saveFile.getPath()));
                    savedGamesBox.getChildren().add(saveBtn);
                    validSaves++;
                } else {
                    corruptedFiles.add(saveFile.getName());
                }
            }
        }

        if (!corruptedFiles.isEmpty()) {
            String errorMsg = String.format("Warning: Found %d corrupted save(s):%n%s at %s",
                    corruptedFiles.size(),
                    String.join(", ", corruptedFiles),
                    directory.getPath());
            errorLabel.setText(errorMsg);
            errorLabel.setVisible(true);
        }
    }

    private File getLatestValidSave() {
        File directory = new File("src/main/resources/saves/");
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));

        if (files != null && files.length > 0) {
            Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

            for (File file : files) {
                if (!isCorruptedSave(file)) {
                    return file;
                }
            }
        }
        return null;
    }

    private void loadLatestGame() {
        File latestValid = getLatestValidSave();
        if (latestValid != null) {
            loadSpecificGame(latestValid.getPath());
        } else {
            errorLabel.setText("No valid save files found");
            errorLabel.setVisible(true);
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