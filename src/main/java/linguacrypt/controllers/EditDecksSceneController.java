package linguacrypt.controllers;

import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import linguacrypt.scenes.SceneManager;
import linguacrypt.model.Card;
import linguacrypt.model.Deck;
import linguacrypt.model.Model;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

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
    private Button deleteDeckButton;

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
        deleteDeckButton.setDisable(true); // Disable by default
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
                    System.out.println("Saving changes to decks...");
                    model.getDeckManager().saveDeckManager();
                    sm.popScene();
                } else if (result.get() == leaveWithoutSave) {
                    System.out.println("Leaving without saving changes...");
                    // Restore all deleted cards using CardManager's new HashMap structure
                    HashMap<Card, ArrayList<Deck>> deletedCards = model.getCardManager().getDeletedCards();
                    for (Map.Entry<Card, ArrayList<Deck>> entry : deletedCards.entrySet()) {
                        Card card = entry.getKey();
                        ArrayList<Deck> decks = entry.getValue();
                        for (Deck deck : decks) {
                            System.out.println("Restoring card: " + card.getName() + " to deck: " + deck.getName());
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

    private void revertChanges() {
        HashMap<Card, ArrayList<Deck>> deletedCards = model.getCardManager().getDeletedCards();
        for (Card card : deletedCards.keySet()) {
            ArrayList<Deck> decks = deletedCards.get(card);
            for (Deck deck : decks) {
                System.out.println("Restoring card: " + card.getName() + " to deck: " + deck.getName());
                model.getCardManager().restoreCard(card, deck);
            }
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
                reloadDeckList();
                cardOrDeckAddedOrRemovesViaUI = true;
                System.out.println("Deck created: " + deckName);
            }
        }
    }

    private void reloadDeckList() {
        deckList.getChildren().clear();
        for (Deck deck : model.getDeckManager().getDeckList()) {
            addDeckToUI(deck);
        }
    }

    private void addDeckToUI(Deck deck) {
        HBox deckContainer = new HBox(10);
        deckContainer.setPadding(new Insets(5)); // Add padding around container
        Button deckButton = new Button(deck.getName());
        deckButton.getStyleClass().add("deck-button");

        deckButton.setOnAction(e -> {
            // Reset previous selection
            if (selectedButton != null) {
                selectedButton.getStyleClass().remove("selected");
            }

            // Update selection
            selectedDeck = deck;
            selectedButton = deckButton;
            deckButton.getStyleClass().add("selected");
            newCardButton.setDisable(false);
            deleteDeckButton.setDisable(false);
            showDeckCards(deck);

            // Show cards
            showDeckCards(deck);
        });

        deckContainer.getChildren().add(deckButton);
        deckList.getChildren().add(deckContainer);
    
    }

    @FXML
    public void deleteDeck() {
        if (selectedDeck != null) {
            model.getDeckManager().removeDeck(selectedDeck);
            reloadDeckList();
            cardOrDeckAddedOrRemovesViaUI = true;
            selectedDeck = null;
            newCardButton.setDisable(true);
            deleteDeckButton.setDisable(true);
        }
    }

    private void showDeckCards(Deck deck) {
        cardList.getChildren().clear();
        cardInfoBox.setVisible(false);

        FlowPane cardFlow = new FlowPane();
        cardFlow.setHgap(10); // increased horizontal gap
        cardFlow.setVgap(10); // increased vertical gap
        cardFlow.setPadding(new Insets(10));
        cardFlow.setPrefWrapLength(sm.getPrimaryStage().getWidth() / 2 - 20); // maximize width
        cardFlow.setStyle("-fx-alignment: center;"); // Center alignment both horizontally and vertically

        for (Card card : deck.getCardList()) {
            Button cardButton = new Button(card.getName());
            cardButton.getStyleClass().add("card-button");
            cardButton.setMaxWidth(Double.MAX_VALUE);
            cardButton.setMinHeight(50);
            cardButton.setOnAction(event -> showCardInfo(card));
            cardFlow.getChildren().add(cardButton);
        }

        cardList.getChildren().add(cardFlow);
    }

    private void showCardInfo(Card card) {
        selectedCard = card;
        cardInfoBox.setVisible(true);
        cardInfoBox.getChildren().clear();

        VBox infoLabels = new VBox(5);
        infoLabels.getChildren().addAll(
                new Label("Card Information:"),
                new Label("Card Name: " + card.getName()),
                new Label("Deck: " + selectedDeck.getName()));

        HBox buttonBox = new HBox(10);
        infoLabels.setPadding(new Insets(10));
        buttonBox.setPadding(new Insets(10));
        Button deleteCardButton = new Button("Delete Card");
        deleteCardButton.getStyleClass().add("delete-card-button");
        deleteCardButton.setOnAction(e -> {
            deleteCard(card);
            cardInfoBox.setVisible(false);
        });

        Button addToAnotherDeckButton = new Button("Add to Another Deck");
        addToAnotherDeckButton.setOnAction(e -> showAddToAnotherDeckDialog());

        buttonBox.getChildren().addAll(addToAnotherDeckButton, deleteCardButton);
        cardInfoBox.getChildren().addAll(infoLabels, buttonBox);
    }

    private void deleteCard(Card card) {
        if (selectedDeck != null) {
            selectedDeck.removeCard(card);
            model.getCardManager().deleteCard(card, selectedDeck);
            cardOrDeckAddedOrRemovesViaUI = true;
            showDeckCards(selectedDeck);
        }
    }

    @FXML
    private void showAddToAnotherDeckDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Another Deck");

        ComboBox<String> deckComboBox = new ComboBox<>();
        deckComboBox.setPromptText("Select a deck");
        for (Deck deck : model.getDeckManager().getDeckList()) {
            model.getCardManager().getDecks(selectedCard);
            if (deck != selectedDeck && !model.getCardManager().getDecks(selectedCard).contains(deck)) {
                deckComboBox.getItems().add(deck.getName());
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
            System.out.println("Card deleted from deck: " + selectedDeck.getName());
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
