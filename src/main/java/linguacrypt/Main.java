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
import linguacrypt.scenes.ManagedScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;

public class Main extends Application {

    private Model model;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new Model(true);

        SceneManager sm = SceneManager.getInstance(primaryStage, model);

        // Fake scene that shouldn't be used. It is used to initialize the SceneManager
        // to correct values
        URL fxmlURL = getClass().getResource("/Init.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);

        Parent root = loader.load();
        Scene scene = new Scene(root, sm.getWidth(), sm.getHeight());
        primaryStage.setScene(scene);

        // Actual Initial Scene
        Media sound = new Media(getClass().getResource("/sounds/98_Startup_Sound.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        ManagedScene MenuScene = new MenuScene(sm);
        sm.pushScene(MenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
