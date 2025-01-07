package linguacrypt.scenes;

import javafx.scene.Scene;

public abstract class ManagedScene {

    private Scene scene;
    private SceneManager sm;
    private String FXMLPath;

    public ManagedScene(SceneManager sm) {
        this.sm = sm;
    }

    public ManagedScene(SceneManager sm, int width, int height) {
        this.sm = sm;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public SceneManager getSceneManager() {
        return sm;
    }

    public void setSceneManager(SceneManager sm) {
        this.sm = sm;
    }

    public String getFXMLPath() {
        return FXMLPath;
    }

    public void setFXMLPath(String FXMLPath) {
        this.FXMLPath = FXMLPath;
    }

    public abstract void update();

    public abstract void destroy();

    public abstract void init();

    public abstract void keyPressed(int keyCode);

    public abstract void keyReleased(int keyCode);
}
