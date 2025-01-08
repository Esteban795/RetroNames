package linguacrypt.controllers;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Card;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;

public class EndGameSceneController {

    private final SceneManager sm;

    @FXML
    private VBox gridBox;

    @FXML
    private VBox keyBox;

    @FXML
    private Label blueTeamStatsLabel;

    @FXML
    private Label redTeamStatsLabel;

    public EndGameSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    void initialize() {
        setupGrid();
        displayStats();
    }

    public void setupGrid() {
        int size = sm.getModel().getGame().getGrid().size();
        GridPane expectedMapGrid = new GridPane();
        GridPane keyGrid = new GridPane();
        ArrayList<ArrayList<Card>> expectedMap = sm.getModel().getGame().getGrid();
        ArrayList<ArrayList<Card>> key = sm.getModel().getGame().getKey();

        // Set grid properties
        expectedMapGrid.setHgap(5);
        expectedMapGrid.setVgap(5);
        keyGrid.setHgap(5);
        keyGrid.setVgap(5);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(75, 75); // Set fixed size
                pane.setStyle("-fx-background-color: " + expectedMap.get(i).get(j).getCardColor().toString().toLowerCase() + "; -fx-border-color: black;");
                expectedMapGrid.add(pane, i, j);

                Pane paneExpected = new Pane();
                paneExpected.setPrefSize(75, 75); // Set fixed size
                paneExpected.setStyle("-fx-background-color: " + key.get(i).get(j).getCardColor().toString().toLowerCase() + "; -fx-border-color: black;");
                keyGrid.add(paneExpected, i, j);
            }
        }

        // Set VBox properties
        keyBox.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");
        gridBox.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");

        keyBox.getChildren().add(keyGrid);
        gridBox.getChildren().add(expectedMapGrid);
    }

    public void displayStats() {
        System.out.println("Displaying stats");
        int[] blueTeamStats = sm.getModel().getGame().getStats().getBlueTeamStats();
        int[] redTeamStats = sm.getModel().getGame().getStats().getRedTeamStats();

        blueTeamStatsLabel.setText("Statistiques de l'équipe bleue :\n"
                + "   - Carte bleues découvertes : " + blueTeamStats[0] + "\n"
                + "   - Carte rouges découvertes : " + blueTeamStats[1] + "\n"
                + "   - Carte noires découvertes : " + blueTeamStats[2] + "\n"
                + "   - Carte blanches découvertes : " + blueTeamStats[3] + "\n"
                + "   - Temps moyen pour répondre : " + sm.getModel().getGame().getStats().getBlueTeamAvgTimeToAnswer() + "s");

        redTeamStatsLabel.setText("Statistiques de l'équipe rouge :\n"
                + "   - Carte bleues découvertes : " + redTeamStats[0] + "\n"
                + "   - Carte rouges découvertes : " + redTeamStats[1] + "\n"
                + "   - Carte noires découvertes : " + redTeamStats[2] + "\n"
                + "   - Carte blanches découvertes : " + redTeamStats[3] + "\n"
                + "   - Temps moyen pour répondre : " + sm.getModel().getGame().getStats().getRedTeamAvgTimeToAnswer() + "s");
    }

    @FXML
    public void goToMenu() {
        sm.goToPreviousSceneType(MenuScene.class);
    }

    @FXML
    public void goToLobby() {
        sm.goToPreviousSceneType(LobbyScene.class);
    }

}
