package linguacrypt.controllers;

import java.util.Optional;

import linguacrypt.scenes.SceneManager;
import linguacrypt.model.Deck;
import linguacrypt.model.Model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EditDecksSceneController {

    private SceneManager sm;
    private Model model;

    @FXML
    private Button buttonBack;

    @FXML
    private Dialog<ButtonType> newCardDialog;

    @FXML
    private TextField cardNameField;

    @FXML
    private Dialog<ButtonType> newDeckDialog;
    
    @FXML
    private TextField deckNameField;
    

    @FXML
    private VBox deckList;

    public EditDecksSceneController(SceneManager sm) {
        this.sm = sm;
        this.model = sm.getModel();
    }

    @FXML
    public void initialize() {
        // Initialize your deck list here
        // Add your logic to load decks from DeckManager
        for (Deck deck : model.getDeckManager().getDeckList()) {
            addDeckToUI(deck);
        }
    }

    @FXML
    public void goBack() {
        // gotta save dekcs before exiting
        sm.popScene();
    }

    @FXML
    public void showNewDeckPopup() {
        deckNameField.setText("");
        Optional<ButtonType> result = newDeckDialog.showAndWait();
        
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            String deckName = deckNameField.getText().trim();
            if (!deckName.isEmpty()) {
                Deck newDeck = new Deck();
                newDeck.setDeckName(deckName);
                model.getDeckManager().addDeck(newDeck);
                addDeckToUI(newDeck);
                System.out.println("Deck created: " + deckName);
            }
        }
    }

    private void addDeckToUI(Deck deck) {
        // Create an HBox to hold the deck button and delete button
        HBox deckContainer = new HBox(5); // 5 is the spacing between elements

        // Create the deck button
        Button deckButton = new Button(deck.getDeckName());

        // Create the delete button
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> deleteDeck(deck, deckContainer));

        // Add both buttons to the container
        deckContainer.getChildren().addAll(deckButton, deleteButton);

        // Add the container to the deck list
        deckList.getChildren().add(deckContainer);
    }

    private void deleteDeck(Deck deck, HBox deckContainer) {
        // Remove from the model
        model.getDeckManager().removeDeck(deck);

        // Remove from the UI
        deckList.getChildren().remove(deckContainer);

        System.out.println("Deck deleted!");
    }

    @FXML
    public void createCard() {

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
