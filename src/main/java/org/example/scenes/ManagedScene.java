package org.example.scenes;

import javafx.scene.Scene;

public abstract class ManagedScene extends Scene{
    
    private SceneManager sm;
    private String FXMLPath;

    public ManagedScene(SceneManager sm) {
        super(sm.getRoot());
        this.sm = sm;
    }

    public ManagedScene(SceneManager sm,int width, int height) {
        super(sm.getRoot(), width, height);
        this.sm = sm;
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

