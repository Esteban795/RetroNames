package linguacrypt.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
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
    @FXML
    private Pane particlePane;
    private Random random = new Random();
    private List<Circle> particles = new ArrayList<>();

    @FXML
    private Label gameTitle;

    public LoadMenuSceneController(SceneManager sm) {
        this.sm = sm;
        particlePane = new Pane();
    }

    @FXML
    public void initialize() {
        setupLatestGameButton();
        setupBrowseSavesButton();
        loadSavedGames();
        // Create fire effect
        Glow glow = new Glow(0.0);
        gameTitle.setEffect(glow);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(gameTitle.textFillProperty(), Color.ORANGE),
                        new KeyValue(glow.levelProperty(), 0.3)),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(gameTitle.textFillProperty(), Color.YELLOW),
                        new KeyValue(glow.levelProperty(), 0.7)),
                new KeyFrame(Duration.seconds(1.0),
                        new KeyValue(gameTitle.textFillProperty(), Color.RED),
                        new KeyValue(glow.levelProperty(), 0.5)),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(gameTitle.textFillProperty(), Color.ORANGE),
                        new KeyValue(glow.levelProperty(), 0.3)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // glitter effect
        createParticleSystem();
        startParticleAnimation();
    }

    private void setupBrowseSavesButton() {
        browseSavesBtn.setOnAction(e -> openFileChooser());
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionnez une sauvegarde");
        fileChooser.setInitialDirectory(new File("src/main/resources/saves/"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File selectedFile = fileChooser.showOpenDialog(sm.getPrimaryStage());
        if (selectedFile != null && !isCorruptedSave(selectedFile)) {
            loadSpecificGame(selectedFile.getPath());
        } else if (selectedFile != null) {
            errorLabel.setText("Le fichier sélectionné est corrompu");
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
            String errorMsg = String.format("Attention : %d sauvegarde(s) corrompue(s) trouvée(s) : %s dans %s",
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
            errorLabel.setText("Pas de sauvegarde valide trouvée");
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
            sm.pushScene(new GameScene(sm));
        } catch (IOException e) {
            // System.err.println("Error loading game scene: " + e.getMessage());
        }
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }

    private void createParticleSystem() {
        for (int i = 0; i < 500; i++) {
            Circle particle = new Circle(2, Color.WHITE);
            particle.setOpacity(0.7);
            particles.add(particle);
            particlePane.getChildren().add(particle);
            resetParticle(particle);
        }
    }

    private void resetParticle(Circle particle) {
        double titleX = gameTitle.getLayoutX();
        double titleY = gameTitle.getLayoutY();
        double titleWidth = gameTitle.getWidth();
        double titleHeight = gameTitle.getHeight();

        particle.setCenterX(titleX - 20 + random.nextDouble() * (titleWidth + 40));
        particle.setCenterY(titleY - 20 + random.nextDouble() * (titleHeight + 40));
        particle.setOpacity(random.nextDouble() * 0.5 + 0.3);
    }

    private void startParticleAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            particles.forEach(particle -> {
                particle.setCenterY(particle.getCenterY() - random.nextDouble() * 2);
                particle.setCenterX(particle.getCenterX() + (random.nextDouble()) * 2);
                particle.setOpacity(particle.getOpacity() - 0.01);

                if (particle.getOpacity() <= 0) {
                    resetParticle(particle);
                }
            });
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void requestFocus(MouseEvent event) {
        Node source = (Node) event.getSource();
        if (source instanceof Button) {
            ((Button) source).requestFocus();
        }
    }
}