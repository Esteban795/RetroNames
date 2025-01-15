package linguacrypt;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import linguacrypt.model.Model;
import linguacrypt.model.Settings;
import linguacrypt.scenes.ManagedScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;

public class Main extends Application {

    private Model model;
    MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new Model(true);

        SceneManager sm = SceneManager.getInstance(primaryStage, model);

        // Fake scene that shouldn't be used. It is used to initialize the SceneManager
        // to correct values
        URL fxmlURL = getClass().getResource("/Init.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);

        Parent temp_root = loader.load();
        Scene scene = new Scene(temp_root, sm.getWidth(), sm.getHeight());
        primaryStage.setScene(scene);

        // Actual Initial Scene
        ManagedScene MenuScene = new MenuScene(sm);
        sm.pushScene(MenuScene);
        primaryStage.show();

        URL mediaUrl = getClass().getResource("/sounds/old-laptop.mp3");
        if (mediaUrl != null) {
            mediaPlayer = new MediaPlayer(new Media(mediaUrl.toExternalForm()));
            mediaPlayer.volumeProperty().bind(Settings.getInstance().getSoundLevel().divide(100.0).multiply(0.3));

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } else {
            System.err.println("Error: Media file not found!");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
