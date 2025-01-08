package linguacrypt.controllers;

import java.io.IOException;

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

        // Set fixed row constraints
        gameGrid.getRowConstraints().clear();
        for (int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(100); // Fixed height for each row
            row.setVgrow(Priority.ALWAYS); // Allow growth if needed
            gameGrid.getRowConstraints().add(row);
        }

        // Pour chaque carte dans la grille du jeu
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cardButton = new Button();
                cardButton.setPrefSize(100, 100); // Fixed button size
                cardButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Allow button to fill cell

                Card card = game.getGrid().get(i).get(j);
                cardButton.setText(card.getName());

                //cardButton.setOnAction(e -> handleCardClick(row, col));
                gameGrid.add(cardButton, j, i);
            }
        }
    }

    private void updateTurnLabel() {
        teamTurnLabel.setText(game.getCurrentTeam().getName());
    }

    // private void handleCardClick(int row, int col) {
    //     // Gérer le clic sur une carte
    //     game.playCard(row, col);
    //     updateGrid();
    //     updateTurnLabel();
    // }
    @FXML
    public void switchScene() throws IOException {
        sm.pushScene(new LobbyScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }
}
