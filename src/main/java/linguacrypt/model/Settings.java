package linguacrypt.model;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Settings {
    private static Settings instance;
    private SimpleIntegerProperty soundLevel;
    private boolean fisheye;
    private boolean scanlines;
    private Rectangle transitionRectangle;
    private FadeTransition fadeIn;
    private FadeTransition fadeOut;
    

    private Settings(int soundLevel, boolean fisheye, boolean scanlines) {
        this.soundLevel = new SimpleIntegerProperty(soundLevel);
        this.fisheye = fisheye;
        this.scanlines = scanlines;

        this.transitionRectangle = new Rectangle(1280, 800);
        this.transitionRectangle.setFill(javafx.scene.paint.Color.BLACK);
        this.transitionRectangle.setOpacity(1);
        this.transitionRectangle.setFocusTraversable(true);
        this.transitionRectangle.setMouseTransparent(true);
        this.transitionRectangle.setPickOnBounds(false);

        this.fadeIn = new FadeTransition(Duration.seconds(0.3), transitionRectangle);
        this.fadeIn.setFromValue(0);
        this.fadeIn.setToValue(1);

        this.fadeOut = new FadeTransition(Duration.seconds(0.3), transitionRectangle);
        this.fadeOut.setFromValue(1);
        this.fadeOut.setToValue(0);

    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings(100, false, false);
        }
        return instance;
    }

    public SimpleIntegerProperty getSoundLevel() {
        return this.soundLevel;
    }

    public void setSoundLevel(int soundLevel) {
        this.soundLevel.set(soundLevel);
    }

    public boolean isFisheye() {
        return fisheye;
    }

    public void setFisheye(boolean fisheye) {
        this.fisheye = fisheye;
    }

    public boolean isScanlines() {
        return scanlines;
    }

    public void setScanlines(boolean scanlines) {
        this.scanlines = scanlines;
    }

    public Node getScanlines(float width, float  height) {
        Shape rectangle = new javafx.scene.shape.Rectangle(width, height);
        rectangle.setId("scanlines");
        LinearGradient scanlineGradient = new LinearGradient(
                0, 0, 0, 0.01, // Start and end points (vertical gradient, shorter cycle)
                true, // Proportional to the Rectangle size
                CycleMethod.REFLECT, // Cycle method
                new Stop(0.0, javafx.scene.paint.Color.BLACK), // Dark line
                new Stop(0.5, javafx.scene.paint.Color.GRAY), // Light line
                new Stop(1.0, javafx.scene.paint.Color.BLACK) // Dark line
        );

        // Apply the gradient as the fill for the rectangle
        rectangle.setFill(scanlineGradient);
        rectangle.setOpacity(0.2);
        rectangle.setFocusTraversable(true);
        rectangle.setMouseTransparent(true);
        rectangle.setPickOnBounds(false);
        rectangle.setBlendMode(javafx.scene.effect.BlendMode.SOFT_LIGHT);
        rectangle.idProperty().set("scanlines");
        return rectangle;
    }

    
    public void applyFisheye(Node mainPane) {
        FloatMap floatMap = new FloatMap();
        int width = 200; // Map width
        int height = 200; // Map height
        floatMap.setWidth(width);
        floatMap.setHeight(height);

        // Center of the fisheye effect
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.max(centerX, centerY); // Maximum radius for the effect

        // Populate the FloatMap with fisheye distortion
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Calculate the distance from the center
                double dx = (x - centerX) / radius;
                double dy = (y - centerY) / radius;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Scale the distance for the fisheye effect
                if (distance < 1.5) {
                    double scale = Math.pow(distance, 1.5); // Less aggressive scaling
                    floatMap.setSamples(x, y, (float) (dx * (scale - 1) * 0.03), (float) (dy * (scale - 1) * 0.03));
                } else {
                    floatMap.setSamples(x, y, 0, 0); // Outside the radius, no displacement
                }
            }
        }

        DisplacementMap displacementMap = new DisplacementMap(floatMap);

        // Create a Bloom effect
        javafx.scene.effect.Bloom bloom = new javafx.scene.effect.Bloom();
        bloom.setThreshold(0.95);

        // Wrap the displacement map with the bloom effect
        displacementMap.setInput(bloom);

        // Apply the combined effect to the mainPane
        mainPane.setEffect(displacementMap);
    }

    public Rectangle getTransitionRectangle() {
        return transitionRectangle;
    }

    public void fadeInRectangle(Runnable onComplete){
        this.fadeIn.setOnFinished(event -> {
            onComplete.run();
        });

        this.fadeIn.play();
    }

    public void fadeOutRectangle(){
        fadeOut.play();
    }

    public void playClickSound(){
        javafx.scene.media.AudioClip clickSoundPlayer = new javafx.scene.media.AudioClip(getClass().getResource("/sounds/button" + (int) (Math.random() * 3 + 1) + ".mp3").toExternalForm());
        clickSoundPlayer.setVolume(getSoundLevel().getValue() / 100.0);
        clickSoundPlayer.setRate(0.8 + Math.random() * 0.4);
        clickSoundPlayer.play();
    }

    @Override
    public String toString() {
        return "Settings{" + "soundLevel=" + soundLevel + ", fisheye=" + fisheye + ", scanlines=" + scanlines + '}';
    }
}
