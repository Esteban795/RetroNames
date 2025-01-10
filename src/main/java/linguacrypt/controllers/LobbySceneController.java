package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import linguacrypt.model.Card;
import linguacrypt.model.Color;
import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Player;
import linguacrypt.model.Team;
import linguacrypt.scenes.GameScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

public class LobbySceneController {

    private final SceneManager sm;

    @FXML
    private TextField pseudoTextField;

    @FXML
    private VBox blueTeamOperative;

    @FXML
    private VBox blueTeamSpy;

    @FXML
    private VBox redTeamSpy;

    @FXML
    private VBox redTeamOperative;

    @FXML
    private VBox pseudoVB;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> decksSelector;

    @FXML
    private ComboBox<String> gridSizeSelector;

    public LobbySceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        setupDragAndDrop(pseudoVB);
        setupDragAndDrop(blueTeamOperative);
        setupDragAndDrop(blueTeamSpy);
        setupDragAndDrop(redTeamSpy);
        setupDragAndDrop(redTeamOperative);
        setupListeners();

        setupDeckChoices();
        setupGridSizeChoices();
    }

    private void setupListeners() {
        // Permet de gérer l'ajout de pseudo avec la touche entrée
        pseudoTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validatePseudo();
            }
        });

        pseudoTextField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() && event.getCode() == KeyCode.X) {
                        quickAddPlayers();
                    }
                });
            }
        });

        pseudoTextField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown()) {
                        if (event.getCode() == KeyCode.X) {
                            quickAddPlayers();
                        } else if (event.getCode() == KeyCode.ENTER) {
                            try {
                                lobbyDone();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }

    private void setupDeckChoices() {
        sm.getModel().getDeckManager().getDeckList().forEach(deck -> {
            Label deckItem = new Label(deck.getName());
            decksSelector.getItems().add(deckItem.getText());
        });
    }

    private void setupGridSizeChoices() {
        for (int i = 3; i <= 9; i++) {
            gridSizeSelector.getItems().add(Integer.toString(i));
        }
    }

    @FXML
    public void switchScene() {
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }

    private Label createDraggableLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 5; -fx-background-color: white; -fx-border-color: black;");

        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
        });

        label.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                VBox parent = (VBox) label.getParent();
                parent.getChildren().remove(label);
            }
        });

        return label;
    }

    private void setupDragAndDrop(VBox vbox) {
        vbox.setOnDragOver(event -> {
            if (event.getGestureSource() != vbox
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        vbox.setOnDragEntered(event -> {
            if (event.getGestureSource() != vbox
                    && event.getDragboard().hasString()) {
            }
        });

        vbox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Label newLabel = createDraggableLabel(db.getString());
                vbox.getChildren().add(newLabel);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    public void validatePseudo() {
        String pseudoString = this.pseudoTextField.getText();
        if (pseudoString.isEmpty()) {
            return;
        }

        pseudoTextField.setText("");

        Label pseudoLabel = createDraggableLabel(pseudoString);
        pseudoVB.getChildren().add(pseudoLabel);
    }

    @FXML
    public void lobbyDone() throws IOException {
        int count = blueTeamOperative.getChildren().size() + blueTeamSpy.getChildren().size()
                + redTeamOperative.getChildren().size() + redTeamSpy.getChildren().size();
        String deckName = decksSelector.getValue();

        if (count < 4) {
            errorLabel.setText("Pas assez de joueurs.");
            return;
        }

        if (blueTeamSpy.getChildren().size() != 1 || redTeamSpy.getChildren().size() != 1) {
            errorLabel.setText("Il doit y avoir exactement un espion par équipe.");
            return;
        }

        if (deckName == null) {
            errorLabel.setText("Il est nécessaire de sélectionner un deck avant de pouvoir démarrer la partie.");
            return;
        }

        int gridSize = gridSizeSelector.getValue() == null ? 5 : Integer.parseInt(gridSizeSelector.getValue());
        sm.getModel().getGame().getConfig().setGridSize(gridSize);

        addPlayersToTeams();

        DeckManager dm = sm.getModel().getDeckManager();
        GameConfiguration config = sm.getModel().getGame().getConfig();
        Deck deck = dm.getDeck(deckName);

        config.setCurrentDeck(deck);

        sm.pushScene(new GameScene(sm));
    }

    private void addPlayersToTeams() {
        Team blueTeam = sm.getModel().getGame().getConfig().getTeamManager().getBlueTeam();
        Team redTeam = sm.getModel().getGame().getConfig().getTeamManager().getRedTeam();

        // Adding spies, they should be alone in their team
        Label blueSpy = (Label) blueTeamSpy.getChildren().get(0);
        Label redSpy = (Label) redTeamSpy.getChildren().get(0);
        blueTeam.addPlayer(new Player(blueSpy.getText(), true));
        redTeam.addPlayer(new Player(redSpy.getText(), true));

        blueTeamOperative.getChildren().forEach(player -> {
            Label label = (Label) player;
            blueTeam.addPlayer(new Player(label.getText(), false));
        });

        redTeamOperative.getChildren().forEach(player -> {
            Label label = (Label) player;
            redTeam.addPlayer(new Player(label.getText(), false));
        });
    }

    public void quickAddPlayers() {
        // Add Red Spymaster
        Label redSpy = createDraggableLabel("RedSpy");
        redTeamSpy.getChildren().add(redSpy);

        // Add Red Operative
        Label redOp = createDraggableLabel("RedOp");
        redTeamOperative.getChildren().add(redOp);

        // Add Blue Spymaster
        Label blueSpy = createDraggableLabel("BlueSpy");
        blueTeamSpy.getChildren().add(blueSpy);

        // Add Blue Operative
        Label blueOp = createDraggableLabel("BlueOp");
        blueTeamOperative.getChildren().add(blueOp);
    }

    private void fillRange(List<Card> cards, int start, int end, Color color) {
        for (int i = start; i < end; i++) {
            cards.get(i).setColor(color);
        }
    }

    public void setupCards(String deckName) {
        DeckManager dm = sm.getModel().getDeckManager();
        GameConfiguration config = sm.getModel().getGame().getConfig();
        Deck deck = dm.getDeck(deckName);

        config.setCurrentDeck(deck);

        int gridSize = sm.getModel().getGame().getConfig().getGridSize();
        ArrayList<Card> cards = new ArrayList<>(deck.getCardList());

        Collections.shuffle(cards);
        ArrayList<Card> selectedCards = new ArrayList<>(cards.subList(0, gridSize * gridSize));
        int step = gridSize * gridSize / 3;

        fillRange(selectedCards, 0, step + 1, Color.RED);
        fillRange(selectedCards, step + 1, 2 * step + 1, Color.BLUE);
        fillRange(selectedCards, 2 * (step + 1) + 1, 3 * step - 1, Color.WHITE);

        selectedCards.get(gridSize * gridSize - 1).setColor(Color.BLACK);

        Collections.shuffle(selectedCards);

        sm.getModel().getGame().setGrid(selectedCards);
    }

    @FXML
    public void goToMenu() {
        sm.goToPreviousSceneType(MenuScene.class);
    }

    /*
     * Debugging method to print selected cards
     */
    // private void printSelectedCards(ArrayList<Card> selectedCards) {
    //     for (int i = 0; i < selectedCards.size(); i++) {
    //         System.out.println(
    //                 "Name : " + selectedCards.get(i).getName() + " (color : " + selectedCards.get(i).getColor() + ")");
    //     }
    // }

    public void setSelectedDeck(String deckName) {
        decksSelector.setValue(deckName);
    }

}
