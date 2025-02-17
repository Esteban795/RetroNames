package linguacrypt.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import linguacrypt.model.Card;
import linguacrypt.model.Game;
import linguacrypt.scenes.LobbyScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;

public class EndGameSceneController {

    private final SceneManager sm;
    private final String winningTeam;

    @FXML
    private Label labelVictor;

    // @FXML
    // private GridPane keyGrid;

    // @FXML
    // private GridPane expectedMapGrid;

    @FXML
    private Label blueTeamStatsLabel;

    @FXML
    private Label redTeamStatsLabel;

    @FXML
    private VBox expectedMapBox;

    @FXML
    private VBox keyBox;

    @FXML
    private VBox mainVBox;

    public EndGameSceneController(SceneManager sm,  String winningTeam) {
        this.sm = sm;
        this.winningTeam = winningTeam;
    }

    @FXML
    void initialize() {
        labelVictor.setText("L'équipe " + winningTeam + " a gagné !");
        labelVictor.getStyleClass().add(winningTeam.toLowerCase().split(" ")[0] + "-team");
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), labelVictor);
        RotateTransition rt = new RotateTransition(Duration.millis(400), labelVictor);
        labelVictor.setTranslateX(5);
        labelVictor.setRotate(-5);
        tt.setByY(10);
        rt.setByAngle(10);
        tt.setAutoReverse(true);
        rt.setAutoReverse(true);
        tt.setCycleCount(TranslateTransition.INDEFINITE);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        tt.play();
        rt.play();
        mainVBox.getStyleClass().add(winningTeam.toLowerCase().split(" ")[0] + "-main");
        setupGrid();
        displayStats();
    }

    public void setupGrid() {
        int size = sm.getModel().getGame().getGrid().size();
        ArrayList<ArrayList<Card>> expectedMap = sm.getModel().getGame().getGrid();

        GridPane expectedMapGrid = new GridPane();
        GridPane keyGrid = new GridPane();
        int cellSize = 70 - 5 * (size - 3);
        expectedMapGrid.setStyle(
                "-fx-max-width:" + (cellSize) * (size + 1) + "; -fx-max-height:"
                        + (cellSize) * (size + 1) + ";");
        keyGrid.setStyle(
                "-fx-max-width:" + (cellSize) * (size + 1) + "; -fx-max-height:" + (cellSize) * (size + 1) + ";");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(cellSize, cellSize); // Set fixed size
                String color = expectedMap.get(i).get(j).isFound() ? expectedMap.get(i).get(j).getColor().toString()
                        .toLowerCase() : "white";
                if (color.equals("white") && expectedMap.get(i).get(j).isFound()) {
                    color = "beige";
                }
                pane.setStyle("-fx-max-width:70;-fx-max-height:70;-fx-background-color: " + color
                       
                        + "; -fx-border-color: black;");
                expectedMapGrid.add(pane, j,  i);

                Pane paneExpected = new Pane();
                paneExpected.setPrefSize(cellSize, cellSize); // Set fixed size
                paneExpected.setStyle("-fx-max-width:70;-fx-max-height:70;-fx-background-color: "
                        + expectedMap.get(i).get(j).getColor().toString().toLowerCase() + "; -fx-border-color: black;");
                keyGrid.add(paneExpected, j, i);
            }
        }

        expectedMapGrid.setAlignment(Pos.CENTER);
        keyGrid.setAlignment(Pos.CENTER);

        expectedMapBox.getChildren().add(expectedMapGrid);
        keyBox.getChildren().add(keyGrid);
    }

    public void displayStats() {
        // System.out.println("Displaying stats");
        int[] blueTeamStats = sm.getModel().getGame().getStats().getBlueTeamStats();
        int[] redTeamStats = sm.getModel().getGame().getStats().getRedTeamStats();

        String blueTeamAvgTimeAnswer = String.format("%.2f",
               
                sm.getModel().getGame().getStats().getBlueTeamAvgTimeToAnswer());
        blueTeamStatsLabel.setText("   - Carte bleues découvertes : " + blueTeamStats[0] + "\n"
                + "   - Carte rouges découvertes : " + blueTeamStats[1] + "\n"
                + "   - Carte noires découvertes : " + blueTeamStats[2] + "\n"
                + "   - Carte blanches découvertes : " + blueTeamStats[3] + "\n"
                + "   - Temps moyen pour répondre : " + blueTeamAvgTimeAnswer
                + "s");

        String redTeamAvgTimeAnswer = String.format("%.2f",
                sm.getModel().getGame().getStats().getRedTeamAvgTimeToAnswer());

        redTeamStatsLabel.setText("   - Carte bleues découvertes : " + redTeamStats[0] + "\n"
                + "   - Carte rouges découvertes : " + redTeamStats[1] + "\n"
                + "   - Carte noires découvertes : " + redTeamStats[2] + "\n"
                + "   - Carte blanches découvertes : " + redTeamStats[3] + "\n"
                + "   - Temps moyen pour répondre : " + redTeamAvgTimeAnswer
                + "s");
    }

    @FXML
    public void goToMenu() {
        sm.getModel().setGame(new Game());
        sm.goToPreviousSceneType(MenuScene.class);
    }

    @FXML
    public void goToLobby() throws IOException {
        sm.getModel().setGame(new Game());
        if (!sm.goToPreviousSceneType(LobbyScene.class)) {
            sm.pushScene(new LobbyScene(sm));
        }
    }

}
