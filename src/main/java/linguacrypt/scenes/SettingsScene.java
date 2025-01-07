package linguacrypt.scenes;

import java.net.URL;

import linguacrypt.controllers.SettingsSceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SettingsScene extends ManagedScene {

    private SettingsSceneController controller;

    public SettingsScene(SceneManager sm) {
        super(sm);
        super.setFXMLPath("/scenes/settings/SettingsScene.fxml");
        controller = new SettingsSceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());

        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            System.out.println("Error loading Settings.fxml");
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
