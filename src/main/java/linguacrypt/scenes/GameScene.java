package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import linguacrypt.controllers.GameSceneController;
public class GameScene extends ManagedScene { 

    private GameSceneController controller;

    public GameScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/game/GameScene.fxml");
        controller = new GameSceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        //System.out.println("FXML Path : " + super.getFXMLPath());
        try {
            Parent root = loader.load();
            super.setScene(new Scene(root, sm.getWidth(), sm.getHeight()));
            super.getScene().getStylesheets().add(getClass().getResource("/scenes/game/GameScene.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading GameScene.fxm            System.out.println(\"Red card\");l");
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
