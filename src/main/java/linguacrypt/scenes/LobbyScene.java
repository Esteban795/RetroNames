package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import linguacrypt.controllers.LobbySceneController;
import linguacrypt.model.Settings;

public class LobbyScene extends ManagedScene {

    private LobbySceneController controller;

    public LobbyScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/lobby/LobbyScene.fxml");
        controller = new LobbySceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        try {
                        Settings settings = Settings.getInstance();
            Parent content = loader.load();
            AnchorPane root = new AnchorPane();
            root.getChildren().add(content);
            root.setStyle("-fx-background-color: #000000;");
            root.getChildren().add(settings.getTransitionRectangle());

            if (settings.isScanlines()) {
                 root.getChildren().add(settings.getScanlines(sm.getWidth(), sm.getHeight()));
            }
            else {
                if (root.getChildren().size() > 2)
                root.getChildren().remove(2);
            }
            if (settings.isFisheye()) {
                settings.applyFisheye(content);
                if (settings.isScanlines()) {
                    settings.applyFisheye(root.getChildren().get(1));
                }
            } else {
                root.setEffect(null);
            } 
            super.setScene(new Scene(root, sm.getWidth(), sm.getHeight()));
            super.getScene().getStylesheets()
                    .add(getClass().getResource("/scenes/lobby/LobbyScene.css").toExternalForm());

        } catch (Exception e) {
            // System.out.println("Error loading LobbyScene.fxml");
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

    public LobbySceneController getController() {
        return controller;
    }

}
