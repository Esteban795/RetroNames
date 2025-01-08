package linguacrypt.controllers;

import java.util.ArrayList;
import java.util.Optional;

import linguacrypt.scenes.SceneManager;
import linguacrypt.model.Card;
import linguacrypt.model.Deck;
import linguacrypt.model.Model;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private Deck selectedDeck;
    private Button selectedButton; // To track currently selected button

    @FXML
    private VBox cardList;
    @FXML
    private Deck currentDeck;

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

                if (model.getDeckManager().getDeck(deckName) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Deck Name");
                    alert.setHeaderText(null);
                    alert.setContentText("A deck with name '" + deckName + "' already exists!");
                    alert.showAndWait();
                    return;
                }

                model.getDeckManager().addDeck(newDeck);
                addDeckToUI(newDeck);
                System.out.println("Deck created: " + deckName);
            }
        }
    }

    private void addDeckToUI(Deck deck) {
        HBox deckContainer = new HBox(5);
        Button deckButton = new Button(deck.getDeckName());

        // Style for selected state
        String defaultStyle = deckButton.getStyle();
        String selectedStyle = "-fx-background-color: lightblue;";

        deckButton.setOnAction(e -> {
            // Reset previous selection
            if (selectedButton != null) {
                selectedButton.setStyle(defaultStyle);
            }

            // Update selection
            selectedDeck = deck;
            selectedButton = deckButton;
            deckButton.setStyle(selectedStyle);

            // Show cards
            showDeckCards(deck);
        });

        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> deleteDeck(deck, deckContainer));

        deckContainer.getChildren().addAll(deckButton, deleteButton);
        deckList.getChildren().add(deckContainer);
    }

    private void deleteDeck(Deck deck, HBox deckContainer) {
        // Remove from the model
        model.getDeckManager().removeDeck(deck);

        // Remove from the UI
        deckList.getChildren().remove(deckContainer);

        System.out.println("Deck deleted!");
    }

    private void showDeckCards(Deck deck) {
        cardList.getChildren().clear();

        //Get sorted cards
        ArrayList<Card> sortedCards = model.getDeckManager().getSortedCards(deck);

        // Display each card in the deck
        for (Card card : sortedCards) {
            HBox cardContainer = new HBox(5);
            Button cardButton = new Button(card.getCardName());

            Button deleteCardButton = new Button("X");
            deleteCardButton.setOnAction(e -> deleteCard(card, cardContainer));

            cardContainer.getChildren().addAll(cardButton, deleteCardButton);
            cardList.getChildren().add(cardContainer);
        }
    }

    private void deleteCard(Card card, HBox cardContainer) {
        if (selectedDeck != null) {
            selectedDeck.removeCard(card);
            cardList.getChildren().remove(cardContainer);
            System.out.println("Card deleted from deck: " + selectedDeck.getDeckName());
        } else {
            System.out.println("No deck selected!");
        }
    }

    @FXML
    public void createCard() {

    }

    @FXML
private void showNewCardPopup() {
    if (selectedDeck == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Deck Selected");
        alert.setHeaderText(null);
        alert.setContentText("Please select a deck first!");
        alert.showAndWait();
        return;
    }

    cardNameField.setText("");
    Optional<ButtonType> result = newCardDialog.showAndWait();

    if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
        String cardName = cardNameField.getText().trim();
        if (!cardName.isEmpty()) {
            if (selectedDeck.getCard(cardName) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Card Name");
                alert.setHeaderText(null);
                alert.setContentText("A card with name '" + cardName + "' already exists in this deck!");
                alert.showAndWait();
                return;
            }

            Card newCard = new Card(cardName);
            selectedDeck.addCard(newCard);
            showDeckCards(selectedDeck);
            System.out.println("Card added: " + cardName);
        }
    }
}

}
