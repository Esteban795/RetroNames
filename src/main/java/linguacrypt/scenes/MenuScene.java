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
    MediaPlayer mediaPlayer2;

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
        if (settings.isScanlines()) {
            root.getChildren().add(settings.getScanlines(sm.getWidth(), sm.getHeight()));
        } else {
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
        super.getScene().getStylesheets().add(getClass().getResource("/scenes/menu/style.css").toExternalForm());

        URL mediaUrl = getClass().getResource("/sounds/startup.mp3");
        URL mediaUrl2 = getClass().getResource("/sounds/old-laptop.mp3");
        if (mediaUrl2 != null) {
            mediaPlayer2 = new MediaPlayer(new Media(mediaUrl2.toExternalForm()));
            mediaPlayer2.setVolume(0.3);
            mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer2.play();
        } else {
            System.err.println("Error: Media file not found!");
        }
        if (mediaUrl != null) {
            mediaPlayer = new MediaPlayer(new Media(mediaUrl.toExternalForm()));
            mediaPlayer.setVolume(0.3);
            mediaPlayer.play();
            
        } else {
            System.err.println("Error: Media file not found!");
        }
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
