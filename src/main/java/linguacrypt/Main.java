package linguacrypt;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        // Fake scene that shouldn't be used. It is used to initialize the SceneManager to correct values
        URL fxmlURL = getClass().getResource("/Init.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1600,900);
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
