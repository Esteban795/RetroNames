package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import linguacrypt.model.Card;
import linguacrypt.model.Color;
import linguacrypt.model.Game;
import linguacrypt.model.Team;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;

public class GameSceneController {

    // Necessary attributes to display the game
    private final SceneManager sm;
    private Game game;
    private final int size;
    private String currentHint = "";
    private int remainingGuesses = 0;
    private int bonusGuess = 1;

    // FXML attributes for the game grid
    @FXML
    private GridPane gameGrid;
    @FXML
    private Label teamTurnLabel;
    @FXML
    private ProgressBar redTeamProgress;
    @FXML
    private ProgressBar blueTeamProgress;

    // FXML attributes for the hints
    @FXML
    private HBox hintInputBox;
    @FXML
    private HBox hintDisplayBox;
    @FXML
    private TextField hintField;
    @FXML
    private MenuButton numberChoice;
    @FXML
    private Label hintLabel;
    @FXML
    private Label remainingGuessesLabel;
    @FXML
    private Button hintTimerButton;
    @FXML
    private Button endTurnButton;

    // Timer attributes
    @FXML
    private Label hintTimerLabel;
    private AnimationTimer hintTimer;
    SimpleDoubleProperty hintTimerProperty = new SimpleDoubleProperty(0);
    @FXML
    private Label guessTimerLabel;
    private AnimationTimer guessTimer;
    SimpleDoubleProperty guessTimerProperty = new SimpleDoubleProperty(0);

    public GameSceneController(
            SceneManager sm) {
        this.sm = sm;
        this.game = sm.getModel().getGame();

        // Make sure a deck is set
        if (game.getConfig().getCurrentDeck() == null) {
            game.getConfig().setCurrentDeck(sm.getModel().getDeckManager().getRandomDeck());
        }

        // Create the grid for the scene
        game.initGrid();
        this.size = game.getGrid().size();
        System.out.println("GameSceneController initialized with grid size: " + size);
    }

    @FXML
    public void initialize() {
        // Initialize game scene elements
        try {
            setupGameGrid();
            setupTimers();
            setupHintControls();
            initializeProgress();
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Initialize the grid with the cards of the game's deck.
     */
    private void setupGameGrid() {
        if (game == null || game.getGrid() == null) {
            System.err.println("Game or grid is null");
            return;
        }
        game.getGrid().clear();
        game.initGrid();
        System.out.println("Game grid loaded with cards");
        updateGrid();
        updateTurnLabel();
    }

    /*
     * Update the grid with the current state of the game.
     */
    private void updateGrid() {
        // Clear the grid
        gameGrid.getChildren().clear();

        // Set fixed column constraints to have a regular grid
        gameGrid.getColumnConstraints().clear();
        for (int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(100); // Fixed width for each column
            column.setHgrow(Priority.ALWAYS); // Allow growth if needed
            gameGrid.getColumnConstraints().add(column);
        }

        // Add card buttons to the grid
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cardButton = new Button();
                cardButton.setPrefSize(100, 100);
                cardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                Card card = game.getGrid().get(i).get(j);
                cardButton.setText(card.getName());

                // Set button style based on card state
                if (card.isFound()) {
                    switch (card.getColor()) {
                        case RED:
                            cardButton.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #cc0000;");
                            break;
                        case BLUE:
                            cardButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: #0000cc;");
                            break;
                        case WHITE:
                            cardButton.setStyle("-fx-background-color: #e6e6e6; -fx-text-fill: #666666;");
                            break;
                        case BLACK:
                            cardButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
                            break;
                    }
                }

                // Handle card click
                final int row = i;
                final int col = j;
                cardButton.setOnAction(e -> handleCardClick(row, col));
                gameGrid.add(cardButton, j, i);
            }
        }
    }

    /*
     * Handle a card click event.
     * Reveals the card and updates the game state.
     */
    private void handleCardClick(int row, int col) {
        if (remainingGuesses > 0 && !game.getGrid().get(row).get(col).isFound()) {
            if (endTurnButton.isDisabled()) {
                endTurnButton.setDisable(false);
            }

            Card card = game.getGrid().get(row).get(col);
            game.revealCard(card);
            remainingGuesses--;
            remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);

            if (remainingGuesses == 0) {
                if (bonusGuess > 0) {
                    remainingGuesses = bonusGuess;
                    bonusGuess = 0;
                    remainingGuessesLabel.setText("Essais Bonus : " + remainingGuesses);
                } else {
                    endTurn();
                }
            }

            updateGrid();
            updateTurnLabel();
            updateProgress();
        }
    }

    @FXML
    private void endTurn() {

        game.switchTeam();
        remainingGuesses = 0;
        bonusGuess = 1;
        hintLabel.setText("");
        remainingGuessesLabel.setText("");
        hintInputBox.setVisible(true);
        endTurnButton.setDisable(true);
        hintDisplayBox.setVisible(false);
        updateTurnLabel();
        guessTimer.stop();
        hintTimerButton.setVisible(true);

    }

    private void updateTurnLabel() {
        Team team = game.getCurrentTeam();
        boolean isRedTeam = team.getColor() == Color.RED;
        String teamName = isRedTeam ? "ÉQUIPE ROUGE" : "ÉQUIPE BLEUE";
        String color = isRedTeam ? "#cc0000" : "#0000cc";
        String bgColor = isRedTeam ? "#ffcccc" : "#ccccff";

        teamTurnLabel.setText(teamName);
        teamTurnLabel.setStyle(String.format(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: %s; " +
                        "-fx-text-fill: %s; " +
                        "-fx-padding: 5px 15px; " +
                        "-fx-background-radius: 5px;",
                bgColor, color));
    }

    private void setupHintControls() {
        // Setup number choices
        numberChoice.getItems().clear();
        for (int i = 1; i <= 9; i++) {
            final String num = String.valueOf(i);
            MenuItem item = new MenuItem(num);
            item.setOnAction(e -> {
                numberChoice.setText(num);
            });
            numberChoice.getItems().add(item);
        }

        hintDisplayBox.setVisible(false);
    }

    @FXML
    public void submitHint() {
        if (hintField == null || numberChoice == null)
            return;

        currentHint = hintField.getText();
        try {
            remainingGuesses = Integer.parseInt(numberChoice.getText());
            hintLabel.setText("Indice : " + currentHint);
            remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);

            hintInputBox.setVisible(false);
            hintDisplayBox.setVisible(true);
            hintTimer.stop();
            guessTimer.start();
        } catch (NumberFormatException e) {
            System.err.println("Invalid number choice");
        }
    }

    private void initializeProgress() {
        if (redTeamProgress != null && blueTeamProgress != null) {
            updateProgress();
        }
    }

    private void updateProgress() {
        int redFound = (int) game.getGrid().stream()
                .flatMap(ArrayList::stream)
                .filter(c -> c.isFound() && c.getColor() == Color.RED)
                .count();

        int blueFound = (int) game.getGrid().stream()
                .flatMap(ArrayList::stream)
                .filter(c -> c.isFound() && c.getColor() == Color.BLUE)
                .count();

        int redTotal = (int) game.getGrid().stream()
                .flatMap(ArrayList::stream)
                .filter(c -> c.getColor() == Color.RED)
                .count();

        int blueTotal = (int) game.getGrid().stream()
                .flatMap(ArrayList::stream)
                .filter(c -> c.getColor() == Color.BLUE)
                .count();

        redTeamProgress.setProgress((double) redFound / redTotal);
        blueTeamProgress.setProgress((double) blueFound / blueTotal);
    }

    private void setupTimers() {
        hintTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private long maxHintTime = 10;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                }
                hintTimerProperty.set(maxHintTime - (now - lastUpdate) / 1000000000.0);
                if (hintTimerProperty.doubleValue() < 0) {
                    lastUpdate = 0;
                    numberChoice.setText("10");
                    hintField.setText("");
                    submitHint();
                    return;
                }
            }

            @Override
            public void stop() {
                super.stop();
                hintTimerProperty.set(10);
            }

            @Override
            public void start() {
                super.start();
                hintTimerProperty.set(10);
                lastUpdate = 0;
            }
        };

        hintTimerLabel.textProperty().bind(hintTimerProperty.asString("Temps restant : %.1f s"));

        System.out.println(game.getConfig().getLimitedTime());
        if (game.getConfig().getLimitedTime() <= 0) { // Standard mode, guess time is a stopwatch
            this.guessTimer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                    }
                    guessTimerProperty.set((now - lastUpdate) / 1000000000.0);
                }

                @Override
                public void stop() {
                    super.stop();
                    // TODO : update guess time stats
                    guessTimerProperty.set(0);
                    lastUpdate = 0;
                }

                @Override
                public void start() {
                    super.start();
                    guessTimerProperty.set(0);
                    lastUpdate = 0;
                }
            };
            guessTimerLabel.textProperty().bind(guessTimerProperty.asString("Temps écoulé : %.1f s"));

        } else { // Blitz mode, guess time is a countdown too
            long maxGuessTime = game.getConfig().getLimitedTime();
            this.guessTimer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                    }
                    guessTimerProperty.set(maxGuessTime - (now - lastUpdate) / 1000000000.0);
                    if (maxGuessTime - (now - lastUpdate) / 1000000000.0 <= 0) {
                        stop();
                        lastUpdate = 0;
                        endTurn();
                    }
                }

                @Override
                public void stop() {
                    super.stop();
                    // TODO : update guess time stats (average time to answer)
                    // guessTimerProperty is remaining time, so you have to save maxGuessTime
                    // - guessTimerProperty
                    guessTimerProperty.set(maxGuessTime);
                    lastUpdate = 0;
                }

                @Override
                public void start() {
                    super.start();
                    guessTimerProperty.set(maxGuessTime);
                    lastUpdate = 0;
                }
            };
            guessTimerLabel.textProperty().bind(guessTimerProperty.asString("Temps restant : %.1f s"));
        }
    }

    @FXML
    private void startHintCountdown() {
        hintTimer.start();
        hintTimerButton.setVisible(false);
    }

    @FXML
    public void switchScene() throws IOException {
        sm.pushScene(new LobbyScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}
