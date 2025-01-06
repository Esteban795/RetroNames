package org.example;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlURL = getClass().getResource("/scenes/menu/MenuScene.fxml");
        if (fxmlURL == null) {
            System.err.println("Could not find MenuScene.fxml");
            System.exit(1);
        }
        Parent root = FXMLLoader.load(fxmlURL);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Bootstrap Project using FXML");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
