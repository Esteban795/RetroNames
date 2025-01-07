package linguacrypt.controllers;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import linguacrypt.model.Card;
import linguacrypt.scenes.SceneManager;

public class EndGameSceneController {
    
    private final SceneManager sm;

    @FXML
    private VBox gridBox;

    @FXML 
    private VBox keyBox;


    public EndGameSceneController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    void initialize() {
        int size = sm.getModel().getGrid().size();
        GridPane expectedMapGrid = new GridPane();
        GridPane keyGrid = new GridPane();
        ArrayList<ArrayList<Card>> expectedMap = sm.getModel().getGrid();
        ArrayList<ArrayList<Card>> key = sm.getModel().getKey();
    
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
}
