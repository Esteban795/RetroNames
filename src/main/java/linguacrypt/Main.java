package linguacrypt;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import linguacrypt.model.Model;
import linguacrypt.model.Settings;
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
        Settings settings = Settings.getInstance();
        

        AnchorPane trueRoot = new AnchorPane();
        trueRoot.getChildren().add(root);
            if (settings.isScanlines()) {
                trueRoot.getChildren().add(settings.getScanlines(sm.getWidth(), sm.getHeight()));
            }
            else {
                if (trueRoot.getChildren().size() > 1)
                trueRoot.getChildren().remove(1);
            }
            if (settings.isFisheye()) {
                settings.applyFisheye(root);
                if (settings.isScanlines()) {
                    settings.applyFisheye(trueRoot.getChildren().get(1));
                }
            } else {
                trueRoot.setEffect(null);
            } 

        Scene trueScene = new Scene(trueRoot, sm.getWidth(), sm.getHeight());
        trueScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(trueScene);

        ManagedScene MenuScene = new MenuScene(sm);
        sm.pushScene(MenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
