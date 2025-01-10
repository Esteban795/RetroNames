package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;
import linguacrypt.scenes.LoadMenuScene;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;

public class NewLoadSceneController {

    private SceneManager sm;

    @FXML
    private Button buttonBack;

    @FXML
    private Pane particlePane;
    private Random random = new Random();
    private List<Circle> particles = new ArrayList<>();

    @FXML
    private Label gameTitle;
    
    public NewLoadSceneController(SceneManager sm) {
        this.sm = sm;
        particlePane = new Pane();
    }

    @FXML
    public void initialize() {
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

    @FXML
    public void goBack() {
        sm.popScene();
    }

    @FXML
    public void newGame() throws IOException {
        sm.pushScene(new LobbyScene(sm));
        // try {
        // } catch (IOException ex) {
        // }
    }

    @FXML
    public void quickPlay() throws IOException {
        // Create lobby scene but don't show it
        LobbyScene lobbyScene = new LobbyScene(sm);
        LobbySceneController lobbyController = lobbyScene.getController();

        // Add default players
        lobbyController.quickAddPlayers();

        // Select random deck
        DeckManager deckManager = sm.getModel().getDeckManager();
        List<Deck> decks = deckManager.getDeckList();
        int randomIndex = (int) (Math.random() * decks.size());
        Deck randomDeck = decks.get(randomIndex);
    @FXML
    public void quickPlay() throws IOException {
        // Create lobby scene but don't show it
        LobbyScene lobbyScene = new LobbyScene(sm);
        LobbySceneController lobbyController = lobbyScene.getController();

        // Add default players
        lobbyController.quickAddPlayers();

        // Select random deck
        DeckManager deckManager = sm.getModel().getDeckManager();
        List<Deck> decks = deckManager.getDeckList();
        int randomIndex = (int) (Math.random() * decks.size());
        Deck randomDeck = decks.get(randomIndex);

        lobbyController.setSelectedDeck(randomDeck.getName());

        // Setup game with random deck
        System.out.println("Try to setup game with random deck: " + randomDeck.getName());
        lobbyController.lobbyDone();
    }

        lobbyController.setSelectedDeck(randomDeck.getName());

        // Setup game with random deck
        System.out.println("Try to setup game with random deck: " + randomDeck.getName());
        lobbyController.lobbyDone();
    }

    @FXML
    public void loadGame() throws IOException {
        System.out.println("Load Game button clicked!");
        sm.pushScene(new LoadMenuScene(sm));
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

    @FXML
    private void requestFocus(MouseEvent event) {
        Node source = (Node) event.getSource();
        if (source instanceof Button) {
            ((Button) source).requestFocus();
        }
    }
}
