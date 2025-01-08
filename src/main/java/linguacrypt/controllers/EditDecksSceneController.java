package linguacrypt.controllers;

import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
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
    private Card selectedCard;
    private Button addToAnotherDeckButton; // To track currently selected button

    @FXML
    private VBox cardList;
    @FXML
    private Deck currentDeck;

    @FXML
    private VBox deckList;

    @FXML
    private boolean cardOrDeckAddedOrRemovesViaUI;

    @FXML
    private Button newCardButton;

    @FXML
    private VBox cardInfoBox;

    public EditDecksSceneController(SceneManager sm) {
        this.sm = sm;
        this.model = sm.getModel();
        cardOrDeckAddedOrRemovesViaUI = false;
    }

    @FXML
    public void initialize() {
        // Initialize your deck list here
        // Add your logic to load decks from DeckManager
        for (Deck deck : model.getDeckManager().getDeckList()) {
            addDeckToUI(deck);
        }
        newCardButton.setDisable(true); // Disable initially
        cardInfoBox.setVisible(false); // Hide initially
    }

    @FXML
    public void goBack() {
        if (cardOrDeckAddedOrRemovesViaUI) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText(null);
            alert.setContentText("You have made changes to the decks. What would you like to do?");

            ButtonType saveAndLeave = new ButtonType("Save and Leave", ButtonBar.ButtonData.OK_DONE);
            ButtonType leaveWithoutSave = new ButtonType("Leave without saving", ButtonBar.ButtonData.NO);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveAndLeave, leaveWithoutSave, cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveAndLeave) {
                    model.getDeckManager().saveDeckManager();
                    sm.popScene();
                } else if (result.get() == leaveWithoutSave) {
                    // Restore all deleted cards using CardManager
                    for (Card card : model.getCardManager().getDeletedCards()) {
                        ArrayList<Deck> decks = model.getCardManager().getDecks(card);
                        for (Deck deck : decks) {
                            model.getCardManager().restoreCard(card, deck);
                        }
                    }
                    sm.popScene();
                }
            }
        } else {
            sm.popScene();
        }
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
                cardOrDeckAddedOrRemovesViaUI = true;
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
            newCardButton.setDisable(false);

            // Show cards
            showDeckCards(deck);
        });

        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> deleteDeck(deck, deckContainer));

        deckContainer.getChildren().addAll(deckButton, deleteButton);
        deckList.getChildren().add(deckContainer);
    }

    private void deleteDeck(Deck deck, HBox deckContainer) {
        model.getDeckManager().removeDeck(deck);
        deckList.getChildren().remove(deckContainer);
        if (selectedDeck == deck) {
            selectedDeck = null;
            newCardButton.setDisable(true);
        }
        cardOrDeckAddedOrRemovesViaUI = true;
    }

    private void showDeckCards(Deck deck) {
        cardList.getChildren().clear();
        cardInfoBox.setVisible(false); // Hide when switching decks

        for (Card card : deck.getCardList()) {
            HBox cardContainer = new HBox(5);
            Button cardButton = new Button(card.getCardName());

            // Add click handler for card info
            cardButton.setOnAction(e -> {
                showCardInfo(card);
            });

            Button deleteCardButton = new Button("X");
            deleteCardButton.setOnAction(e -> deleteCard(card, cardContainer));

            cardContainer.getChildren().addAll(cardButton, deleteCardButton);
            cardList.getChildren().add(cardContainer);
        }
    }

    private void showCardInfo(Card card) {
        selectedCard = card;
        cardInfoBox.setVisible(true);
        cardInfoBox.getChildren().clear();

        Label details = new Label("Card Information");
        Label nameLabel = new Label("Card Name: " + card.getCardName());
        String decksString = model.getCardManager().toString(model.getCardManager().getDecks(card));
        Label deckLabel = new Label("Deck: " + decksString);
        Button addToAnotherDeckButton = new Button("Add to Another Deck");
        addToAnotherDeckButton.setOnAction(e -> showAddToAnotherDeckDialog());

        cardInfoBox.getChildren().addAll(details, nameLabel, deckLabel, addToAnotherDeckButton);
        cardInfoBox.setSpacing(10);
    }

    @FXML
    private void showAddToAnotherDeckDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Another Deck");

        ComboBox<String> deckComboBox = new ComboBox<>();
        for (Deck deck : model.getDeckManager().getDeckList()) {
            if (deck != selectedDeck) {
                deckComboBox.getItems().add(deck.getDeckName());
            }
        }

        dialog.getDialogPane().setContent(deckComboBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && deckComboBox.getValue() != null) {
            Deck targetDeck = model.getDeckManager().getDeck(deckComboBox.getValue());
            if (targetDeck != null && !targetDeck.containsCard(selectedCard)) {
                model.getCardManager().addCard(selectedCard, targetDeck);
                targetDeck.addCard(selectedCard);
                cardOrDeckAddedOrRemovesViaUI = true;
            }
        }
        showCardInfo(selectedCard);
    }

    private void deleteCard(Card card, HBox cardContainer) {
        if (selectedDeck != null) {
            selectedDeck.removeCard(card);
            model.getCardManager().deleteCard(card, selectedDeck);
            cardOrDeckAddedOrRemovesViaUI = true;
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
                // Check if card exists in current deck
                if (selectedDeck.getCard(cardName) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Card Name");
                    alert.setHeaderText(null);
                    alert.setContentText("A card with name '" + cardName + "' already exists in this deck!");
                    alert.showAndWait();
                    return;
                }

                // Check if card exists in CardManager
                Card existingCard = model.getCardManager().getCard(cardName);
                if (existingCard != null) {
                    selectedDeck.addCard(existingCard);
                    model.getCardManager().addCard(existingCard, selectedDeck);
                    showDeckCards(selectedDeck);
                    cardOrDeckAddedOrRemovesViaUI = true;
                    System.out.println("Existing card added to deck: " + cardName);
                    return;
                }

                Card newCard = new Card(cardName);
                selectedDeck.addCard(newCard);
                model.getCardManager().addCard(newCard, selectedDeck);
                showDeckCards(selectedDeck);
                cardOrDeckAddedOrRemovesViaUI = true;
                System.out.println("Card added: " + cardName);
            }
        }
    }

    @FXML
    public boolean wasCardOrDeckAddedOrRemovesViaUI() {
        return cardOrDeckAddedOrRemovesViaUI;
    }
}
