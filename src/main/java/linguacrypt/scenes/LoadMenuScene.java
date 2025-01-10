package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import linguacrypt.controllers.LoadMenuSceneController;

public class LoadMenuScene extends ManagedScene {

    private LoadMenuSceneController controller;

    public LoadMenuScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/newLoad/load/LoadMenuScene.fxml");
        controller = new LoadMenuSceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        System.err.println(fxmlURL);
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, sm.getWidth(), sm.getHeight()));
            super.getScene().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            super.getScene().getStylesheets().add(getClass().getResource("/scenes/newLoad/load/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading LoadMenuScene.fxml");
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