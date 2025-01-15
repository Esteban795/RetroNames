package linguacrypt.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import linguacrypt.model.Card;
import linguacrypt.model.Deck;
import linguacrypt.model.Model;
import linguacrypt.scenes.SceneManager;

public class EditDecksSceneController {

    private SceneManager sm;
    private Model model;

    @FXML
    private Button buttonBack;

    @FXML
    private Button deleteDeckButton;

    @FXML
    private Button duplicateDeckButton;

    @FXML
    private TextField cardNameField;

    @FXML
    private Dialog<ButtonType> newDeckDialog;

    @FXML
    private TextField deckNameField;

    private Deck selectedDeck;
    private Button selectedButton; // To track currently selected button
    private Card selectedCard;

    @SuppressWarnings("unused")
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
        deleteDeckButton.setDisable(true); // Disable by default
        cardInfoBox.setVisible(false); // Hide initially
        duplicateDeckButton.setDisable(true); // Disable initially
    }

    @FXML
    public void goBack() {
        if (cardOrDeckAddedOrRemovesViaUI) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/scenes/editDecks/style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog-pane");

            alert.setTitle("Sauvegarder les changements");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez modifié les decks, voulez-vous sauvegarder les changements ?");

            ButtonType saveAndLeave = new ButtonType("Sauvegarder et quitter", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(saveAndLeave, cancel);
            // Style the buttons
            alert.getDialogPane().lookupButton(saveAndLeave).getStyleClass().add("dialog-button");
            alert.getDialogPane().lookupButton(cancel).getStyleClass().add("dialog-button");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == saveAndLeave) {
                    model.getDeckManager().saveDeckManager();
                    sm.popScene();
                }
            }
        } else {
            sm.popScene();
        }
    }

    @FXML
            public void showNewDeckPopup() {
                Dialog<ButtonType> newDeckDialog = new Dialog<>();
                newDeckDialog.setTitle("Nouveau deck");
                newDeckDialog.setHeaderText("Entrez le nom du nouveau deck :");

                TextField deckNameField = new TextField();
                deckNameField.setPromptText("Nom du deck");

                VBox content = new VBox(10);
                content.getChildren().add(deckNameField);
                newDeckDialog.getDialogPane().setContent(content);

                newDeckDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                newDeckDialog.getDialogPane().getStylesheets().add(getClass().getResource("/scenes/editDecks/style.css").toExternalForm());
                newDeckDialog.getDialogPane().getStyleClass().add("dialog-pane");

                Optional<ButtonType> result = newDeckDialog.showAndWait();

                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    String deckName = deckNameField.getText().trim();
                    if (!deckName.isEmpty()) {
                        if (model.getDeckManager().getDeck(deckName) != null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Nom du deck dupliqué");
                            alert.setHeaderText(null);
                            alert.setContentText("Un deck '" + deckName + "' existe déjà !");
                            alert.showAndWait();
                            return;
                        }

                        Deck newDeck = new Deck();
                        newDeck.setDeckName(deckName);
                        model.getDeckManager().addDeck(newDeck);
                        reloadDeckList();
                        cardOrDeckAddedOrRemovesViaUI = true;
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
        deckContainer.setPadding(new Insets(5));
        deckContainer.getStyleClass().add("panel");

        Button deckButton = new Button(deck.getName());
        deckButton.getStyleClass().add("deck-button");
        deckButton.setPrefWidth(200);
        deckButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(deckButton, Priority.ALWAYS);

        deckButton.setOnAction(e -> {
            if (selectedButton != null) {
                selectedButton.getStyleClass().remove("selected");
            }

            selectedDeck = deck;
            selectedButton = deckButton;
            deckButton.getStyleClass().addAll("selected", "deck-button-selected");
            newCardButton.setDisable(false);
            deleteDeckButton.setDisable(false);
            duplicateDeckButton.setDisable(false);

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

        FlowPane cardFlow = new FlowPane();
        cardFlow.setHgap(10);
        cardFlow.setVgap(10);
        cardFlow.setPadding(new Insets(10));
        cardFlow.getStyleClass().add("panel");

        for (Card card : deck.getCardList()) {
            Button cardButton = new Button(card.getName());
            cardButton.getStyleClass().add("card-button");
            cardButton.setPrefWidth(150);
            cardButton.setMinHeight(40);
            cardButton.setOnAction(event -> showCardInfo(card));
            cardFlow.getChildren().add(cardButton);
        }

        cardList.getChildren().add(cardFlow);
    }

    private void showCardInfo(Card card) {
        selectedCard = card;
        cardInfoBox.setVisible(true);
        cardInfoBox.getChildren().clear();

        ArrayList<Deck> decks = model.getCardManager().getDecks(card);
        String deckNames = decks != null
                ? String.join(", ", decks.stream().map(Deck::getName).toList())
                : "Pas de deck";

        VBox infoLabels = new VBox(5);
        infoLabels.getChildren().addAll(
                new Label("Infos de la carte:"),
                new Label("Nom de la carte: " + card.getName()),
                new Label("Decks: " + deckNames));

        HBox buttonBox = new HBox(10);
        infoLabels.setPadding(new Insets(10));
        buttonBox.setPadding(new Insets(10));
        Button deleteCardButton = new Button("Supprimer la carte");
        deleteCardButton.getStyleClass().add("delete-card-button");
        deleteCardButton.setOnAction(e -> {
            deleteCard(selectedCard);
            cardInfoBox.setVisible(false);
        });

        Button addToAnotherDeckButton = new Button("Ajouter à un autre deck");
        addToAnotherDeckButton.getStyleClass().add("card-button");
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
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/scenes/editDecks/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane"); 
        dialog.setTitle("Ajouter à un autre deck");
        dialog.setHeaderText("Sélectionnez un deck pour ajouter la carte :");

        VBox content = new VBox(10);
        content.getStyleClass().add("panel"); 
        ComboBox<String> deckComboBox = new ComboBox<>();
        deckComboBox.setPromptText("Sélectionner un deck");
        deckComboBox.getStyleClass().add("combo-box"); 
        for (Deck deck : model.getDeckManager().getDeckList()) {
            if (deck != selectedDeck && !model.getCardManager().getDecks(selectedCard).contains(deck)) {
                deckComboBox.getItems().add(deck.getName());
            }
        }

        content.getChildren().add(deckComboBox);
        dialog.getDialogPane().setContent(content);
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

    @SuppressWarnings("unused")
    private void deleteCard(Card card, HBox cardContainer) {
        if (selectedDeck != null) {
            selectedDeck.removeCard(card);
            model.getCardManager().deleteCard(card, selectedDeck);
            cardOrDeckAddedOrRemovesViaUI = true;
            cardList.getChildren().remove(cardContainer);
        }
    }

    @FXML
    private void showNewCardPopup() {
        if (selectedDeck == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pas de deck sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Sélectionnez d'abord un deck !");
            alert.showAndWait();
            return;
        }

        // Config dialog
        Dialog<ButtonType> newCardDialog = new Dialog<>();
        newCardDialog.setTitle("Ajouter une nouvelle carte");
        newCardDialog.setHeaderText("Entrez les détails de la carte :");

        ButtonType confirmButtonType = ButtonType.OK;
        ButtonType cancelButtonType = ButtonType.CANCEL;
        newCardDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        // Set dialog style
        newCardDialog.getDialogPane().getStylesheets().add(getClass().getResource("/scenes/editDecks/style.css").toExternalForm());
        newCardDialog.getDialogPane().getStyleClass().add("dialog-pane");
        VBox dialogContent = new VBox(10);

        // Create image selection box
        HBox cardUrlBox = new HBox(10);
        Label cardUrlPath = new Label("Pas d'image sélectionnée.");
        Button openCardUrlButton = new Button("...");
        cardUrlBox.getChildren().addAll(new Label("Image: "), cardUrlPath, openCardUrlButton);

        // Create name input field
        TextField cardNameField = new TextField();
        cardNameField.setPromptText("Entrez le nom de la carte");

        // Add components to dialog
        dialogContent.getChildren().addAll(
                new Label("Nom de la carte :"),
                cardNameField,
                cardUrlBox);

        // Setup image chooser
        openCardUrlButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisissez une image pour la carte :");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

            // Set initial directory if needed
            fileChooser.setInitialDirectory(new File("src/main/resources/images/"));

            File selectedFile = fileChooser.showOpenDialog(sm.getPrimaryStage());
            if (selectedFile != null) {
                cardUrlPath.setText(selectedFile.getAbsolutePath());
            }
        });

        newCardDialog.getDialogPane().setContent(dialogContent);

        // Show dialog and process result
        Optional<ButtonType> result = newCardDialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            String cardName = cardNameField.getText().trim();
            String imagePath = cardUrlPath.getText();

            if (cardName.isEmpty()) {
                showError("Veuillez entrer un nom pour la carte !");
                return;
            }

            // Check for duplicate card
            if (selectedDeck.getCard(cardName) != null) {
                showError("Une carte '" + cardName + "' existe déjà dans ce deck !");
                return;
            }
            if (imagePath.equals("Pas d'image sélectionnée.")) {
                imagePath = null;
            }

            // Create and add card
            Card newCard = new Card(cardName, imagePath);
            selectedDeck.addCard(newCard);
            model.getCardManager().addCard(newCard, selectedDeck);
            showDeckCards(selectedDeck);
            cardOrDeckAddedOrRemovesViaUI = true;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showDuplicateDeckPopup() {
        if (selectedDeck == null) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Dupliquer le deck");
        dialog.setHeaderText("Entrez un nom pour le nouveau deck :");

        TextField nameField = new TextField();
        dialog.getDialogPane().setContent(nameField);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/scenes/editDecks/style.css").toExternalForm());

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newDeckName = nameField.getText().trim();
            if (!newDeckName.isEmpty()) {
                if (model.getDeckManager().getDeck(newDeckName) != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nom du duplicata");
                    alert.setHeaderText(null);
                    alert.setContentText("Un deck '" + newDeckName + "' existe déjà !");
                    alert.showAndWait();
                    return;
                }

                Deck newDeck = new Deck();
                newDeck.setDeckName(newDeckName);

                // Explore every cards of and add them to newDeck
                for (Card card : selectedDeck.getCardList()) {
                    newDeck.addCard(card);
                    model.getCardManager().addCard(card, newDeck);
                }

                model.getDeckManager().addDeck(newDeck);
                reloadDeckList();
                cardOrDeckAddedOrRemovesViaUI = true;
            }
        }

        // Reload card display if a deck is selected
        if (selectedDeck != null) {
            showDeckCards(selectedDeck);
        }
    }

    @FXML
    public boolean wasCardOrDeckAddedOrRemovesViaUI() {
        return cardOrDeckAddedOrRemovesViaUI;
    }
}
