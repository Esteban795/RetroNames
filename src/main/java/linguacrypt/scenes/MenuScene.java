package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import linguacrypt.controllers.MenuSceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MenuScene extends ManagedScene {

    private MenuSceneController controller;

    public MenuScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/menu/MenuScene.fxml");
        controller = new MenuSceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        System.err.println(fxmlURL);
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, 800, 600));
            super.getScene().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading MenuScene.fxml");
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
