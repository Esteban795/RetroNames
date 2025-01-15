package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.FloatMap;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import linguacrypt.model.Card;
import linguacrypt.model.Color;
import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Player;
import linguacrypt.model.Settings;
import linguacrypt.model.Team;
import linguacrypt.scenes.GameScene;
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
    private FlowPane pseudoFP;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> decksSelector;

    @FXML
    private ComboBox<String> gridSizeSelector;

    @FXML
    private Label titleWobble;

    public LobbySceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    private Slider timingSlider;

    @FXML
    private CheckBox blitzButton;

    @FXML
    private Label blitzLabel;

    @FXML
    public void initialize() {
        setupDragAndDrop(pseudoFP);
        setupDragAndDrop(blueTeamOperative);
        setupDragAndDrop(blueTeamSpy);
        setupDragAndDrop(redTeamSpy);
        setupDragAndDrop(redTeamOperative);
        setupListeners();

        setupTitle();

        setupDeckChoices();
        setupGridSizeChoices();
    }

    private void setupTitle() {
        // Create a FloatMap for DisplacementMap
        FloatMap floatMap = new FloatMap();
        floatMap.setWidth(200);
        floatMap.setHeight(50); // Adjust to match the size of the Label

        // Create a DisplacementMap using the FloatMap
        DisplacementMap displacementMap = new DisplacementMap(floatMap);

        // Apply the DisplacementMap effect to the Label
        titleWobble.setEffect(displacementMap);

        // Create a Timeline to animate the FloatMap
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(40), event -> {
            // Update the FloatMap dynamically
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 50; y++) {
                    // Create a wobble effect based on time
                    double time = System.currentTimeMillis() / 1000.0;
                    float dy = (float) Math.sin(x * 0.1 + time) * 0.12f;
                    floatMap.setSamples(x, y, 0, dy);
                }
            }
        }));

        // Set the timeline to run indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(javafx.scene.paint.Color.GRAY);
        dropShadow.setRadius(10);

        // Chain the effects: Apply DropShadow after DisplacementMap
        dropShadow.setInput(displacementMap);
        titleWobble.setEffect(dropShadow);
    }

    private void setupListeners() {
        // Permet de gérer l'ajout de pseudo avec la touche entrée
        pseudoTextField.setOnKeyPressed(event -> {
            Settings.getInstance().playClickSound();
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

        timingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            GameConfiguration config = sm.getModel().getGame().getConfig();
            config.setLimitedTime((int) timingSlider.getValue());
            blitzLabel.setText("Temps de réponse : " + (int) timingSlider.getValue() + " secondes");

        });

        blitzButton.setOnAction(event -> {
            GameConfiguration config = sm.getModel().getGame().getConfig();
            if (blitzButton.isSelected()) {
                timingSlider.setDisable(false);
                config.setLimitedTime(60);
                timingSlider.setValue(60);
            } else {
                timingSlider.setDisable(true);
                config.setLimitedTime(-1);
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
        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add("draggable-label");

        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
        });

        label.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                if (label.getParent() instanceof VBox) {
                    VBox parent = (VBox) label.getParent();
                    parent.getChildren().remove(label);
                } else if (label.getParent() instanceof FlowPane) {
                    FlowPane parent = (FlowPane) label.getParent();
                    parent.getChildren().remove(label);
                }
            }
        });

        return label;
    }

    private void setupDragAndDrop(FlowPane pseudoFP) {
        pseudoFP.setOnDragOver(event -> {
            if (event.getGestureSource() != pseudoFP
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        pseudoFP.setOnDragEntered(event -> {
            if (event.getGestureSource() != pseudoFP
                    && event.getDragboard().hasString()) {
            }
        });

        pseudoFP.setOnDragExited(event -> {
            event.consume();
        });

        pseudoFP.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                Label newLabel = createDraggableLabel(db.getString());
                pseudoFP.getChildren().add(newLabel);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });

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

        vbox.setOnDragExited(event -> {
            event.consume();
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
        pseudoFP.getChildren().add(pseudoLabel);
    }

    @FXML
    public void lobbyDone() throws IOException {
        int count = blueTeamOperative.getChildren().size() + blueTeamSpy.getChildren().size()
                + redTeamOperative.getChildren().size() + redTeamSpy.getChildren().size();
        String deckName = decksSelector.getValue();
        GameConfiguration config = sm.getModel().getGame().getConfig();

        if (count < 2) {
            errorLabel.setText("Pas assez de joueurs.");
            return;
        }

        if (count == 2) {
            if (!hasCompleteTeam()) {
                errorLabel.setText("Les deux joueurs doivent être de la même équipe");
            }
            config.setGameMode(true);
            if (blueTeamSpy.getChildren().size() == 1) {
                config.setFirstTeam(false);
                sm.getModel().getGame().switchTeam();
            } else {
                config.setFirstTeam(true);
            }
        } else {
            if (blueTeamSpy.getChildren().size() != 1 || redTeamSpy.getChildren().size() != 1) {
                errorLabel.setText("Il doit y avoir exactement un espion par équipe.");
                return;
            }
        }
        if (deckName == null) {
            errorLabel.setText("Il est nécessaire de sélectionner un deck avant de pouvoir démarrer la partie.");
            return;
        }

        int gridSize = gridSizeSelector.getValue() == null ? 5 : Integer.parseInt(gridSizeSelector.getValue());
        sm.getModel().getGame().getConfig().setGridSize(gridSize);

        addPlayersToTeams();

        DeckManager dm = sm.getModel().getDeckManager();
        Deck deck = dm.getDeck(deckName);

        if (deck.getCardList().size() < gridSize * gridSize) {
            errorLabel.setText("Le deck sélectionné ne contient pas assez de cartes pour la grille.");
            return;
        }

        config.setCurrentDeck(deck);

        sm.pushScene(new GameScene(sm));
    }

    private boolean hasCompleteTeam() {
        return ((blueTeamSpy.getChildren().size() == 1 && blueTeamOperative.getChildren().size() == 1)
                || (redTeamSpy.getChildren().size() == 1 && redTeamOperative.getChildren().size() == 1));
    }

    private void addPlayersToTeams() {
        if (sm.getModel().getGame().getConfig().isDuo()) {
            addPlayersToDuoMode();
        } else {
            addPlayersToNormalMode();
        }
    }

    private void addPlayersToDuoMode() {
        Team activeTeam;
        // Determine which team is being used (the one that has players)
        if (!blueTeamSpy.getChildren().isEmpty()) {
            activeTeam = sm.getModel().getGame().getConfig().getTeamManager().getBlueTeam();
            // Add spy
            Label spy = (Label) blueTeamSpy.getChildren().get(0);
            activeTeam.addPlayer(new Player(spy.getText(), true));
            // Add operative
            Label op = (Label) blueTeamOperative.getChildren().get(0);
            activeTeam.addPlayer(new Player(op.getText(), false));
        } else {
            activeTeam = sm.getModel().getGame().getConfig().getTeamManager().getRedTeam();
            // Add spy
            Label spy = (Label) redTeamSpy.getChildren().get(0);
            activeTeam.addPlayer(new Player(spy.getText(), true));
            // Add operative
            Label op = (Label) redTeamOperative.getChildren().get(0);
            activeTeam.addPlayer(new Player(op.getText(), false));
        }
    }

    private void addPlayersToNormalMode() {
        Team blueTeam = sm.getModel().getGame().getConfig().getTeamManager().getBlueTeam();
        Team redTeam = sm.getModel().getGame().getConfig().getTeamManager().getRedTeam();

        // Adding spies
        Label blueSpy = (Label) blueTeamSpy.getChildren().get(0);
        Label redSpy = (Label) redTeamSpy.getChildren().get(0);
        blueTeam.addPlayer(new Player(blueSpy.getText(), true));
        redTeam.addPlayer(new Player(redSpy.getText(), true));

        // Adding operatives
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

    /*
     * Debugging method to print selected cards
     */
    // private void printSelectedCards(ArrayList<Card> selectedCards) {
    // for (int i = 0; i < selectedCards.size(); i++) {
    // //System.out.println(
    // "Name : " + selectedCards.get(i).getName() + " (color : " +
    // selectedCards.get(i).getColor() + ")");
    // }
    // }

    public void setSelectedDeck(String deckName) {
        decksSelector.setValue(deckName);
    }

}
