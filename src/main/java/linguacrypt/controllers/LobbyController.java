package linguacrypt.controllers;

import java.io.IOException;

import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;
import linguacrypt.scenes.SettingsScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;


public class LobbyController {

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

    public LobbyController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        setupDragAndDrop(pseudoVB);
        setupDragAndDrop(blueTeamOperative);
        setupDragAndDrop(blueTeamSpy);
        setupDragAndDrop(redTeamSpy);
        setupDragAndDrop(redTeamOperative);
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
            if (event.getGestureSource() != vbox &&
                event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });
        
        vbox.setOnDragEntered(event -> {
            if (event.getGestureSource() != vbox &&
                event.getDragboard().hasString()) {
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
        // sm.getModel().    check if player already exists
        if (pseudoString.isEmpty()) {
            return;
        }
        pseudoTextField.setText("");

        Label pseudoLabel = createDraggableLabel(pseudoString);
        pseudoVB.getChildren().add(pseudoLabel);
    }

    @FXML 
    public void lobbyDone() throws IOException {
        int count = blueTeamOperative.getChildren().size() + blueTeamSpy.getChildren().size() + redTeamOperative.getChildren().size() + redTeamSpy.getChildren().size();
        if (count < 4) {
            errorLabel.setText("Pas assez de joueurs.");
            return;
        }

        if (blueTeamSpy.getChildren().size() != 1 || redTeamSpy.getChildren().size() != 1) {
            errorLabel.setText("Il doit y avoir exactement un espion par équipe.");
            return;
        }
        sm.pushScene(new MenuScene(sm));
    }
}