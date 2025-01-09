package linguacrypt.scenes;

import java.util.Stack;

import javafx.stage.Stage;
import linguacrypt.model.Model;

public class SceneManager {

    private final Model model;
    private final Stack<ManagedScene> scenes;
    private final Stage primaryStage;
    private static SceneManager instance;

    private SceneManager(Stage primaryStage, Model model) {
        this.primaryStage = primaryStage;
        this.model = model;
        scenes = new Stack<>();
    }

    public static SceneManager getInstance(Stage primaryStage, Model model) {
        if (instance == null) {
            instance = new SceneManager(primaryStage, model);
        }
        return instance;
    }

    public ManagedScene pushScene(ManagedScene scene, boolean resize) {
        double width = primaryStage.getScene().getWidth();
        double height = primaryStage.getScene().getHeight();
        primaryStage.setScene(scene.getScene());
        scenes.push(scene);
        if (resize) {
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
        }
        return scene;
    }

    public ManagedScene pushScene(ManagedScene scene) {
        return pushScene(scene, false);
    }

    public ManagedScene popScene(boolean resize) {
        ManagedScene scene = scenes.pop();
        if (scenes.isEmpty()) {
            primaryStage.close();
        } else {
            ManagedScene newScene = scenes.peek();
            double width = newScene.getScene().getWidth();
            double height = newScene.getScene().getHeight();
            primaryStage.setScene(newScene.getScene());
            if (resize) {
                primaryStage.setWidth(width);
                primaryStage.setHeight(height);
            }
        }
        return scene;
    }

    public ManagedScene popScene() {
        return popScene(false);
    }

    public void popAllButFirst() {
        ManagedScene first = scenes.get(0);
        scenes.clear();
        scenes.push(first);
    }

    public ManagedScene peek() {
        return scenes.peek();
    }

    public ManagedScene getSceneAt(int index) {
        return scenes.get(index);
    }

    public int getSceneCount() {
        return scenes.size();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Model getModel() {
        return model;
    }

    public void goToPreviousSceneType(Object obj) {
        while (!scenes.peek().getClass().equals(obj)) {
            // Pop all scenes until we reach the correct type scene
            popScene();
        }

    }
}
