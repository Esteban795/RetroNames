package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import linguacrypt.controllers.LobbyController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
public class LobbyScene extends ManagedScene { 

    private LobbyController controller;

    public LobbyScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/lobby/LobbyScene.fxml");
        controller = new LobbyController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            System.out.println("Error loading LobbyScene.fxml");
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
