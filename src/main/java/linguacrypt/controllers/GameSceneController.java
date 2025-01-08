package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;

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
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.SceneManager;

public class GameSceneController {

    private final SceneManager sm;
    private Game game;
    private final int size;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label teamTurnLabel;

    @FXML
    private ProgressBar redTeamProgress;

    @FXML
    private ProgressBar blueTeamProgress;

    @FXML private HBox hintInputBox;
    @FXML private HBox hintDisplayBox;
    @FXML private TextField hintField;
    @FXML private MenuButton numberChoice;
    @FXML private Label hintLabel;
    @FXML private Label remainingGuessesLabel;
    
    private String currentHint = "";
    private int remainingGuesses = 0;

    public GameSceneController(SceneManager sm) {
        this.sm = sm;
        this.game = sm.getModel();
        game.initGrid();
        this.size = game.getGrid().size();
        System.out.println("GameSceneController initialized with grid size: " + size);
    }

    @FXML
    public void initialize() {
        try {
            // Initialize game elements
            setupGameGrid();
            setupHintControls();
            initializeProgress();
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupGameGrid() {
        if (game == null || game.getGrid() == null) {
            System.err.println("Game or grid is null");
            return;
        }
        
        // Initialize grid if empty
        game.getGrid().clear();
        for (int i = 0; i < size; i++) {
            game.getGrid().add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                game.getGrid().get(i).add(new Card("Mot " + i + "," + j));
            }
        }
        updateGrid();
    }
    
    private void updateGrid() {
        gameGrid.getChildren().clear();

        // Set fixed column constraints
        gameGrid.getColumnConstraints().clear();
        for (int i = 0; i < size; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(100); // Fixed width for each column
            column.setHgrow(Priority.ALWAYS); // Allow growth if needed
            gameGrid.getColumnConstraints().add(column);
        }

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
            
            final int row = i;
            final int col = j;
            cardButton.setOnAction(e -> handleCardClick(row, col));
            gameGrid.add(cardButton, j, i);
        }
    }
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
    
    private void initializeProgress() {
        if (redTeamProgress != null && blueTeamProgress != null) {
            updateProgress();
        }
    }
    
    @FXML
    public void submitHint() {
        if (hintField == null || numberChoice == null) return;
        
        currentHint = hintField.getText();
        try {
            remainingGuesses = Integer.parseInt(numberChoice.getText());
            hintLabel.setText("Indice : " + currentHint);
            remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);
            
            hintInputBox.setVisible(false);
            hintDisplayBox.setVisible(true);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number choice");
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
        
        redTeamProgress.setProgress((double)redFound / redTotal);
        blueTeamProgress.setProgress((double)blueFound / blueTotal);
    }

    private void updateTurnLabel() {
        teamTurnLabel.setText(game.getCurrentTeam().getName());
    }

    private void handleCardClick(int row, int col) {
        if (remainingGuesses > 0) {
            Card card = game.getGrid().get(row).get(col);
            game.revealCard(card);
            remainingGuesses--;
            remainingGuessesLabel.setText("Essais restants : " + remainingGuesses);
            
            if (remainingGuesses == 0) {
                hintInputBox.setVisible(true);
                hintDisplayBox.setVisible(false);
            }
            
            updateGrid();
            updateTurnLabel();
            updateProgress();
        }
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
