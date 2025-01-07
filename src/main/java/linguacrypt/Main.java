package linguacrypt;

import java.io.IOException;
import java.net.URL;

import linguacrypt.model.Game;
import linguacrypt.scenes.ManagedScene;
import linguacrypt.scenes.MenuScene;
import linguacrypt.scenes.SceneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Game game;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.game = new Game();
        SceneManager sm = SceneManager.getInstance(primaryStage, this.game);

        // Fake scene that shouldn't be used. It is used to initialize the SceneManager to correct values
        URL fxmlURL = getClass().getResource("/Init.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);

        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Actual Initial Scene
        ManagedScene MenuScene = new MenuScene(sm);
        sm.pushScene(MenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
