package linguacrypt.controllers;

import java.util.Optional;

import linguacrypt.scenes.SceneManager;
import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditDecksSceneController {

    private SceneManager sm;
    private DeckManager deckManager;

    @FXML
    private Button buttonBack;

    @FXML
    private TextField cardNameField;

    @FXML
    private Dialog<ButtonType> newCardDialog;

    @FXML
    private VBox deckList;

    public EditDecksSceneController(SceneManager sm) {
        this.sm = sm;
        this.deckManager = new DeckManager();
    }

    @FXML
    public void initialize() {
        // Initialize your deck list here
        // Add your logic to load decks from DeckManager
        for (Deck deck : deckManager.getDeckList()) {
            Button deckButton = new Button(deck.getDeckName());
            deckList.getChildren().add(deckButton);
        }
    }

    @FXML
    public void goBack() {
        // gotta save dekcs before exiting
        sm.popScene();
    }

    @FXML
    public void createDeck() {
        Deck newDeck = new Deck();
        // Add new deck to the list of decks        
        deckManager.addDeck(newDeck);
        Button but = new Button(newDeck.getDeckName());
        deckList.getChildren().add(but);
        System.out.println("Deck created!");
    }

    @FXML
    public void createCard(){

    }

    @FXML
    private void showNewCardPopup() {
        // Reset the text field before showing dialog
        cardNameField.setText("");
        
        // Show the dialog and wait for user response
        Optional<ButtonType> result = newCardDialog.showAndWait();
        
        // Process the result
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String cardName = cardNameField.getText().trim();
            if (!cardName.isEmpty()) {
                // Process the new card name here
                // Add your logic to create a new card
            }
        }
    }

    
}
