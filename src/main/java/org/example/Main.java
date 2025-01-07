package org.example;

import java.io.IOException;
import java.net.URL;

import org.example.model.Model;
import org.example.scenes.ManagedScene;
import org.example.scenes.MenuScene;
import org.example.scenes.SceneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Model model;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new Model("ça marche hamdullah");
        SceneManager sm = SceneManager.getInstance(primaryStage, this.model);

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
