package linguacrypt.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import linguacrypt.QRCode.QRCodeGenerator;
import linguacrypt.exception.CorruptedSaveException;
import linguacrypt.model.Card;
import linguacrypt.model.Color;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.model.Team;
import linguacrypt.scenes.EndGameScene;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;
import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;

public class GameSceneController {

    // Necessary attributes to display the game
    private final SceneManager sm;
    private Game game;
    private final int size;
    private String currentHint = "";
    private int remainingGuesses = 0;
    private int bonusGuess = 1;

    @FXML
    private BorderPane mainBorderPane;

    // FXML attributes for the game grid
    @FXML
    private GridPane gameGrid;
    @FXML
    private Label teamTurnLabel;
    @FXML
    private Pane redTeamPanel;
    @FXML
    private Pane blueTeamPanel;
    
    private final DoubleProperty redTeamProgress = new SimpleDoubleProperty(0);
    private final DoubleProperty blueTeamProgress = new SimpleDoubleProperty(0);
    @FXML
    private Label redTeamSpymaster;
    @FXML
    private VBox redTeamOperators;
    @FXML
    private Label blueTeamSpymaster;
    @FXML
    private VBox blueTeamOperators;

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
    private Button qrCodeButton;

    public GameSceneController(SceneManager sm) {
        this.sm = sm;
        this.game = sm.getModel().getGame();

        // Make sure a deck is set
        if (game.getConfig().getCurrentDeck() == null) {
            game.getConfig().setCurrentDeck(sm.getModel().getDeckManager().getRandomDeck());
        }

        this.size = game.getConfig().getGridSize();
        System.out.println("GameSceneController initialized with grid size: " + size);
    }

    @FXML
    public void initialize() {
        // Initialize game scene elements
        try {
            setupGameGrid();
            setupHintControls();
            System.out.println("Game scene initialized");
            initializeProgress();
            updatePlayerLabels();
            redTeamSpymaster.getParent().getStyleClass().add("current-play");
            // Permet de gérer l'appui sur la touche entrée pour valider l'indice
            hintField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    submitHint();
                }
            });
            numberChoice.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    submitHint();
                }
            });
            setupQRCode();

        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // INITIALIZATION METHODS

    /*
     * Initialize the grid with the cards of the game's deck.
     */
    private void setupGameGrid() {
        game.loadGrid();
        System.out.println("Game grid loaded with cards");
        updateGrid();
        updateTurnLabel();
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

    private void updatePlayerLabels() {
        Team redTeam = game.getConfig().getTeamManager().getRedTeam();
        Team blueTeam = game.getConfig().getTeamManager().getBlueTeam();

        // Clear previous operators
        redTeamOperators.getChildren().clear();
        blueTeamOperators.getChildren().clear();

        // Update Red Team
        for (Player p : redTeam.getPlayerList()) {
            if (p.getRole()) {
                redTeamSpymaster.setText(p.getName());
            } else {
                Label operatorLabel = new Label(p.getName());
                operatorLabel.setStyle("-fx-text-fill: black; -fx-padding: 5;");
                redTeamOperators.getChildren().add(operatorLabel);
            }
        }

        // Update Blue Team
        for (Player p : blueTeam.getPlayerList()) {
            if (p.getRole()) {
                blueTeamSpymaster.setText(p.getName());
            } else {
                Label operatorLabel = new Label(p.getName());
                operatorLabel.setStyle("-fx-text-fill: black; -fx-padding: 5;");
                blueTeamOperators.getChildren().add(operatorLabel);
            }
        }
    }

    private void setupQRCode() {
        try {
            Image qrCodeImage = new Image(getClass().getResourceAsStream("/imgs/qrcode-placeholder.jpg"));

            BackgroundImage backgroundImage = new BackgroundImage(
                    qrCodeImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, false, true));

            qrCodeButton.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Error loading QR code image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    private void initializeProgress() {
        if (redTeamPanel != null && blueTeamPanel != null) {
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(javafx.scene.paint.Color.valueOf("#0A246A"));
            rectangle.getStyleClass().add("progress-bar");

            rectangle.widthProperty().bind(blueTeamPanel.widthProperty()); // Full width
            rectangle.setStyle("-fx-padding: 10; -fx-margin: 5;");
            rectangle.heightProperty().bind(blueTeamPanel.heightProperty().multiply(blueTeamProgress));
            rectangle.layoutYProperty().bind(blueTeamPanel.heightProperty().subtract(rectangle.heightProperty()));
            blueTeamPanel.getChildren().add(rectangle);


            Rectangle rectangle2 = new Rectangle();
            rectangle2.setFill(javafx.scene.paint.Color.valueOf("#8c0b0b"));
            rectangle2.getStyleClass().add("progress-bar");
            
            rectangle2.widthProperty().bind(redTeamPanel.widthProperty()); // Full width
            rectangle2.heightProperty().bind(redTeamPanel.heightProperty().multiply(redTeamProgress)); 
            rectangle2.layoutYProperty().bind(redTeamPanel.heightProperty().subtract(rectangle2.heightProperty()));
            redTeamPanel.getChildren().add(rectangle2);
            updateProgress();
        }
    }

    // END INITIALIZATION METHODS
    /*
     * Update the grid with the current state of the game.
     */
    private void updateGrid() {
        // Clear the grid
        gameGrid.getChildren().clear();

        // Set fixed column constraints to have a regular grid
        gameGrid.getRowConstraints().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.setVgap(10);
        gameGrid.setHgap(10);
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
                cardButton.getStyleClass().add("card-button");
                cardButton.setPrefSize(150, 100);
                cardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                Card card = game.getGrid().get(i).get(j);
                cardButton.setText(card.getName().toUpperCase());

                // Set button style based on card state
                if (card.isFound()) {
                    switch (card.getColor()) {
                        case RED -> cardButton.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #cc0000;");
                        case BLUE -> cardButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: #0000cc;");
                        case WHITE -> cardButton.setStyle("-fx-background-color: #e6e6e6; -fx-text-fill: #666666;");
                        case BLACK -> cardButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
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

    private void switchTeam() {
        game.switchTeam();
        bonusGuess = 1;
        hintLabel.setText("");
        remainingGuessesLabel.setText("");
        hintInputBox.setVisible(true);
        hintDisplayBox.setVisible(false);

        if (redTeamOperators.getParent().getStyleClass().contains("current-play")) {
            redTeamOperators.getParent().getStyleClass().remove("current-play");
            blueTeamSpymaster.getParent().getStyleClass().add("current-play");
        } else {
            blueTeamOperators.getParent().getStyleClass().remove("current-play");
            redTeamSpymaster.getParent().getStyleClass().add("current-play");
        }
    }

    /*
     * Handle a card click event.
     * Reveals the card and updates the game state.
     */
    private void handleCardClick(int row, int col) {
        if (remainingGuesses > 0 && !game.getGrid().get(row).get(col).isFound()) {
            Card card = game.getGrid().get(row).get(col);
            game.revealCard(card);
            Button revealedCard = (Button) gameGrid.getChildren().get(row * size + col);
            animate(revealedCard, card.getColor());
            if (game.hasBlueTeamWon()) {
                System.out.println("Blue team has won!");
                try {
                    sm.pushScene(new EndGameScene(sm, "bleue"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (game.hasRedTeamWon()) {
                System.out.println("Red team has won!");
                try {
                    sm.pushScene(new EndGameScene(sm, "rouge"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                remainingGuesses--;
                remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);
                if (card.getColor() == Color.BLACK) { // game is lost
                    try {
                        sm.pushScene(new EndGameScene(sm, game.getOppositeTeam().getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (remainingGuesses == 0) {
                    if (bonusGuess > 0) {
                        remainingGuesses = bonusGuess;
                        bonusGuess = 0;
                        remainingGuessesLabel.setText("Essais Bonus : " + remainingGuesses);
                    } else {
                        switchTeam();
                    }
                }

                // updateGrid();
                updateTurnLabel();
                updateProgress();
            }

        }
    }

    private void updateTurnLabel() {
        Team team = game.getCurrentTeam();
        boolean isRedTeam = team.getColor() == Color.RED;
        String teamName = isRedTeam ? "ÉQUIPE ROUGE" : "ÉQUIPE BLEUE";
        String color = isRedTeam ? "#cc0000" : "#0000cc";
        String bgColor = isRedTeam ? "#ffcccc" : "#ccccff";

        if (isRedTeam) {
            mainBorderPane.setStyle("-fx-background-color: radial-gradient(center 20% 50%, radius 120%, #ff9696, #8c0b0b);");
        } else {
            mainBorderPane.setStyle("radial-gradient(center 50% 20%, radius 120%, #A8CAEE, #0A246A)");
        }
        teamTurnLabel.setText(teamName);
        teamTurnLabel.setStyle(String.format(
                "-fx-font-size: 24px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-background-color: %s; "
                        + "-fx-text-fill: %s; "
                        + "-fx-padding: 5px 15px; ",
                bgColor, color));
    }

    @FXML
    public void submitHint() {
        if (hintField == null || numberChoice == null) {
            return;
        }

        currentHint = hintField.getText();
        try {
            remainingGuesses = Integer.parseInt(numberChoice.getText());
            hintLabel.setText("Indice : " + currentHint);
            remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);

            hintInputBox.setVisible(false);
            hintDisplayBox.setVisible(true);

            if (redTeamSpymaster.getParent().getStyleClass().contains("current-play")) {
                redTeamSpymaster.getParent().getStyleClass().remove("current-play");
                redTeamOperators.getParent().getStyleClass().add("current-play");
            } else {
                blueTeamSpymaster.getParent().getStyleClass().remove("current-play");
                blueTeamOperators.getParent().getStyleClass().add("current-play");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid number choice");
        }
    }


    private void updateProgress() {
        System.out.println("Updating progress bars");
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
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(redTeamProgress, redTeamProgress.getValue()), new KeyValue(blueTeamProgress, blueTeamProgress.getValue())), // Start at 0
            new KeyFrame(Duration.seconds(0.5), new KeyValue(redTeamProgress, (double) redFound / redTotal, Interpolator.EASE_OUT), new KeyValue(blueTeamProgress, (double) blueFound / blueTotal, Interpolator.EASE_OUT)) // Fill to 100% in 3 seconds
        );
        timeline.play();
    }

    @FXML
    private void handleMainMenu() {
        showSaveDialog(() -> {
            sm.popAllButFirst();
        });
    }

    @FXML
    private void handleTeamSelect() {
        showSaveDialog(() -> {
            try {
                sm.pushScene(new LobbyScene(sm));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleLoadGame() {
        showSaveDialog(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisissez un fichier de sauvegarde");
            fileChooser.setInitialDirectory(new File("src/main/resources/saves/"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json"));

            File selectedFile = fileChooser.showOpenDialog(sm.getPrimaryStage());
            if (selectedFile != null) {
                try {
                    DeserializationVisitor visitor = new DeserializationVisitor();
                    Game loadedGame = visitor.loadGame(selectedFile.getPath());
                    if (loadedGame != null) {
                        this.game = loadedGame;
                        sm.getModel().setGame(loadedGame);
                        setupGameGrid();
                        setupHintControls();
                        initializeProgress();
                    }
                } catch (CorruptedSaveException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Impossible de charger la partie sélectionnée.");
                    alert.setContentText("Le fichier sélectionné est corrompu.");
                    alert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleSettings() {
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    private void handleQuit() {
        showSaveDialog(() -> Platform.exit());
    }

    private void showSaveDialog(Runnable afterSaveAction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sauvegarder la partie :");
        alert.setHeaderText("Voulez-vous sauvegarder la partie avant avant de quitter ?");
        alert.setContentText("Choisissez une des trois options :");

        ButtonType buttonTypeSave = new ButtonType("Sauvegarder et quitter");
        ButtonType buttonTypeNoSave = new ButtonType("Quitter sans sauvegarder");
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeSave) {
            SerializationVisitor saveVisitor = new SerializationVisitor();
            game.accept(saveVisitor);
            afterSaveAction.run();
        } else if (result.get() == buttonTypeNoSave) {
            afterSaveAction.run();
        }
    }

    @FXML
    public void goBack() {
        SerializationVisitor saveVisitor = new SerializationVisitor();
        game.accept(saveVisitor);
        sm.popScene();
    }

    @FXML
    public void openQRCode() throws Exception {
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle("Scannez ce QR Code pour accéder à la clé de la partie.");
        dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);
        int res = QRCodeGenerator.generateQRCodeImage(sm.getModel().getGame().getGrid(), "src/main/resources/imgs/qrcode_resized.png");
        if (res == 0) {
            System.out.println("QR Code generated successfully");
            File fileImg = new File("src/main/resources/imgs/qrcode_resized.png");
            Image img = new Image(fileImg.toURI().toString());
            double width = img.getWidth();
            double height = img.getHeight();
            ImageView qrCodeView = new ImageView(img);
            dialog.getDialogPane().getChildren().add(qrCodeView);
            dialog.getDialogPane().setMinWidth(width);
            dialog.getDialogPane().setMinHeight(height);
            dialog.show();
        }
    }

    public void animate(Button button, Color color) {
        System.out.println("Animating button with color: " + color);
        Paint fillColor;
        fillColor = switch (color) {
            case RED -> Paint.valueOf("#8c0b0b");
            case BLUE -> Paint.valueOf("#0A246A");
            case BLACK -> Paint.valueOf("#000000");
            default -> Paint.valueOf("#ffffdd");
        };

        System.out.println("Transitioning button color");

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        final javafx.animation.KeyValue scaleKey = new javafx.animation.KeyValue(button.scaleYProperty(), 0, Interpolator.EASE_OUT);
        final javafx.animation.KeyValue scaleKey2 = new javafx.animation.KeyValue(button.scaleYProperty(), 1, Interpolator.EASE_IN);

        BackgroundFill bgcolor = button.backgroundProperty().get().getFills().get(0);
        BackgroundFill newFill = new BackgroundFill(fillColor, bgcolor.getRadii() , bgcolor.getInsets());
        Background newBackground = new Background(newFill);
        

        final javafx.animation.KeyValue colorKey = new javafx.animation.KeyValue(button.backgroundProperty(), newBackground, Interpolator.DISCRETE);
        
        final KeyFrame kf1 = new KeyFrame(Duration.millis(250), scaleKey, colorKey);

        final KeyFrame kf2 = new KeyFrame(Duration.millis(500), scaleKey2);
        timeline.getKeyFrames().addAll(kf1,kf2);

        System.out.println("Playing animation");
        timeline.play();
    }
}
