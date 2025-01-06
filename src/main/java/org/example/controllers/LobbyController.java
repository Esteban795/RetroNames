package org.example.controllers;

import org.example.scenes.SceneManager;
import org.example.scenes.SettingsScene;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LobbyController {

    private SceneManager sm;
    private double startX;
    private double startY;

    @FXML
    private TextField pseudoTextField;

    @FXML
    private FlowPane pseudoFlowPane;

    public LobbyController(SceneManager sm) {
        this.sm = sm;
    }

    @FXML
    public void initialize() {
        
    }

    @FXML
    public void switchScene() {
        sm.pushScene(new SettingsScene(sm));
    }

    @FXML
    public void goBack() {
        sm.popScene();
    }

    public void makeDraggable(Node node){
        node.setOnMousePressed((mouse_event) -> {
            startX = mouse_event.getSceneX() - node.getTranslateX();
            startY = mouse_event.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged((mouse_event) -> {
            node.setTranslateX(mouse_event.getSceneX() - startX);
            node.setTranslateY(mouse_event.getSceneY() - startY);
        });
    }

    @FXML
    public void validatePseudo() {
        String pseudoString = this.pseudoTextField.getText();
        pseudoTextField.setText("");

        StackPane pseudoPane = new StackPane();
        pseudoPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        pseudoPane.setPadding(new Insets(5));
        Label pseudoLabel = new Label(pseudoString);
        pseudoPane.getChildren().add(pseudoLabel);
        pseudoFlowPane.getChildren().add(pseudoPane);
        
        makeDraggable(pseudoPane);

    }
}
