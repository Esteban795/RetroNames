package linguacrypt.scenes;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import linguacrypt.controllers.MenuSceneController;
import linguacrypt.model.Settings;

public class MenuScene extends ManagedScene {

    private MenuSceneController controller;

    MediaPlayer mediaPlayer;

    public MenuScene(SceneManager sm) throws IOException {
        super(sm);
        super.setFXMLPath("/scenes/menu/MenuScene.fxml");
        controller = new MenuSceneController(sm);
        FXMLLoader loader = new FXMLLoader();
        URL fxmlURL = getClass().getResource(super.getFXMLPath());
        System.err.println(fxmlURL);
        loader.setLocation(fxmlURL);
        loader.setController(controller);
        // try {
        Settings settings = Settings.getInstance();
        Parent content = loader.load();
        AnchorPane root = new AnchorPane();
        root.getChildren().add(content);
        root.setStyle("-fx-background-color: #000000;");
        root.getChildren().add(settings.getTransitionRectangle());
        
        super.setScene(new Scene(root, sm.getWidth(), sm.getHeight()));
        super.updateVisuals();
        super.getScene().getStylesheets().add(getClass().getResource("/scenes/menu/style.css").toExternalForm());

        URL mediaUrl = getClass().getResource("/sounds/startup.mp3");

        if (mediaUrl != null) {
            mediaPlayer = new MediaPlayer(new Media(mediaUrl.toExternalForm()) );
            mediaPlayer.setVolume(0.3);
            mediaPlayer.play();
        } else {
            System.err.println("Error: Media file not found!");
        }

        // Add a listener to update the volume when the settings change
        
        // } catch (Exception e) {
        // System.out.println("Error loading MenuScene.fxml");
        sm.getPrimaryStage().close();
        // }

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
