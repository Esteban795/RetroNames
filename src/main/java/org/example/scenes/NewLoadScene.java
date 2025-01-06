package org.example.scenes;

import java.net.URL;

import org.example.controllers.NewLoadController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class NewLoadScene extends ManagedScene {

    private NewLoadController controller;

    public NewLoadScene(SceneManager sm) {
        super(sm);
        super.setFXMLPath("/scenes/newLoad/NewLoadScene.fxml");
        controller = new NewLoadController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());

        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, 800, 600));
            super.getScene().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading NewLoad.fxml");
            sm.getPrimaryStage().close();
        }
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(int keyCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(int keyCode) {
        // TODO Auto-generated method stub

    }
}
