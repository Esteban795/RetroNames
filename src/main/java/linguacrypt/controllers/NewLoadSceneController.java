package linguacrypt.controllers;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;
import linguacrypt.scenes.LoadMenuScene;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;

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
}
