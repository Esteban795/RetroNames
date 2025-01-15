package linguacrypt.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import linguacrypt.model.Settings;

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

    public void updateVisuals(){
        Settings settings = Settings.getInstance();
        AnchorPane root = (AnchorPane) scene.getRoot();

        if (settings.isScanlines()) {
            if (root.lookup("#scanlines") == null){
                root.getChildren().add(settings.getScanlines(sm.getWidth(), sm.getHeight()));
            } else {
                root.lookup("#scanlines").setVisible(true);
            }
        } else {
            if (root.lookup("#scanlines") != null){
                root.lookup("#scanlines").setVisible(false);
            }
        }
        if (settings.isFisheye()) {
            settings.applyFisheye(root.getChildren().get(0)); // Apply fisheye to content
            if (settings.isScanlines()) {
                settings.applyFisheye(root.lookup("#scanlines")); // Apply fisheye to scanlines if they exist
            }
        } else {
            root.getChildren().get(0).setEffect(null);
        }
    }

    public abstract void update();

    public abstract void destroy();

    public abstract void init();

    public abstract void keyPressed(int keyCode);

    public abstract void keyReleased(int keyCode);
}
