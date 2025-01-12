package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import linguacrypt.scenes.EditDecksScene;
import linguacrypt.scenes.NewLoadScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

public class MenuSceneController {
    private SceneManager sm;

    @FXML
    private Label gameTitle;
    @FXML
    private Label subtitleLabel;

    @FXML
    private Pane particlePane;
    private Random random = new Random();
    private List<Circle> particles = new ArrayList<>();

    private final String[] subtitles = {
            "Tu connais le jeu \n de la patate chaude ?",
            "Absolute cinema",
            "Baptiste ne pas casser \n le git challenge",
            "C'est Tom ou Claude \n qui l'a écrit ?",
            "On commence pas une \n journée avant 10h...",
            "Que ça veut",
            "Esteban, la merge request !!!!",
            "Va falloir merge...",
            "Baptiste a cloné le repo 52 fois \n (chiffre non contractuel)",
            "T'as save avant de run ?",
            "Merde, j'ai dev sur main, \n je push quand même",
            "Dev toute la nuit sur main \n et s'en rend compte le matin",
            "Baptiste Push sans commit",
            "Baptiste commit sans push",
            "Baptiste dev sans pull",
            "Baptiste pull sans dev",
            "Esteban, RENDS MA GRILLE !!! ",
            "Baptiste, RENDS MES TESTS !!!",
            "Baptiste quand les test ne passent pas : \n supprime les test",
            "On aurai dû prendre la voiture",

    };

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
        // System.out.println("Settings button clicked!");
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void handleEditDecks() throws IOException {
        // System.out.println("Edit Decks button clicked!");
        sm.pushScene(new EditDecksScene(sm));
    }

    @FXML
    public void handleSaveGame() {
        // System.out.println("Save Game button clicked!");
    }

    @FXML
    public void handleQuit() {
        // saveDecks();
        Platform.exit();
    }

    public void saveDecks() {
        // Save decks to file
        // System.out.println("Saving decks to file...");
        this.sm.getModel().getDeckManager().saveDeckManager();
    }

    @FXML
    private void initialize() {
        // Called after FXML is loaded
        // subtitle
        Random random = new Random();
        subtitleLabel.setText(subtitles[random.nextInt(subtitles.length)]);

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

        Timeline subtitleFade = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(subtitleLabel.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(subtitleLabel.opacityProperty(), 0.1)),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(subtitleLabel.opacityProperty(), 1.0)));
        subtitleFade.setCycleCount(Timeline.INDEFINITE);
        subtitleFade.play();

        Random random2 = new Random();

        Timeline subtitleAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(subtitleLabel.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(subtitleLabel.opacityProperty(), 0.0)),
                new KeyFrame(Duration.seconds(2), event -> {
                    subtitleLabel.setText(subtitles[random2.nextInt(subtitles.length)]);
                }),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(subtitleLabel.opacityProperty(), 1.0)));

        subtitleAnimation.setCycleCount(Timeline.INDEFINITE);
        subtitleAnimation.play();

        // glitter effect
        createParticleSystem();
        startParticleAnimation();
        // Initialize your buttons here
        // System.out.println(this.sm.getModel());
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
                particle.setCenterX(particle.getCenterX() + (random.nextDouble() ) * 2);
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