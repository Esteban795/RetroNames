package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import linguacrypt.model.Card;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
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


    public GameSceneController(SceneManager sm) {
        this.sm = sm;
        this.game = sm.getModel();
        game.initGrid();
        this.size = game.getGrid().size();
        System.out.println("GameSceneController initialized with grid size: " + size);
    }

    @FXML
    public void initialize() {
        // First initialize the model's grid if needed
        for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Card card = new Card("Test " + i + "," + j);
                    game.getGrid().get(i).set(j, card);
                }
            }
        updateGrid();
        updateTurnLabel();
        updateProgress();
    }

    private void updateProgress() {
        GameConfiguration config = game.getConfig();
        int redFound = game.getRedTeamFoundCards();
        int blueFound = game.getBlueTeamFoundCards();
        int redTotal = config.isFirstTeam() ? config.getNbCardsGoal() : config.getNbCardsGoal() - 1;
        int blueTotal = config.isFirstTeam() ? config.getNbCardsGoal() - 1 : config.getNbCardsGoal();

        redTeamProgress.setProgress((double)redFound / redTotal);
        blueTeamProgress.setProgress((double)blueFound / blueTotal);
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

    private void updateTurnLabel() {
        teamTurnLabel.setText(game.getCurrentTeam().getName());
    }

    private void handleCardClick(int row, int col) {
        Card card = game.getGrid().get(row).get(col);
        game.revealCard(card);
        updateGrid();
        updateTurnLabel();
        updateProgress();
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
