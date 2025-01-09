package linguacrypt.controllers;

import java.io.IOException;

import linguacrypt.scenes.EditDecksScene;
import linguacrypt.scenes.NewLoadScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuSceneController {
    private SceneManager sm;

    @FXML
    private Label gameTitle;

    @FXML
    private Pane particlePane;
    private Random random = new Random();
    private List<Circle> particles = new ArrayList<>();

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;

    public MenuSceneController(SceneManager sm) {
        this.sm = sm;
        particlePane = new Pane();
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
    public void handleEditDecks() throws IOException {
        System.out.println("Edit Decks button clicked!");
        sm.pushScene(new EditDecksScene(sm));
    }

    @FXML
    public void handleSaveGame() {
        System.out.println("Save Game button clicked!");
    }

    @FXML
    public void handleQuit() {
        // saveDecks();
        Platform.exit();
    }

    public void saveDecks() {
        // Save decks to file
        System.out.println("Saving decks to file...");
        this.sm.getModel().getDeckManager().saveDeckManager();
    }

    @FXML
    private void initialize() {
        try {
            Font customFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/MonaLisa-Regular.ttf"), 
                14
            );
            if (customFont == null) {
                System.err.println("Error loading custom font");
            }
        } catch (Exception e) {
            System.err.println("Could not load font: " + e.getMessage());
        }
        // Called after FXML is loaded
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
        // Initialize your buttons here
        System.out.println(this.sm.getModel());
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
                particle.setCenterX(particle.getCenterX() + (random.nextDouble() - 0.5) * 2);
                particle.setOpacity(particle.getOpacity() - 0.01);

                if (particle.getOpacity() <= 0) {
                    resetParticle(particle);
                }
            });
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}