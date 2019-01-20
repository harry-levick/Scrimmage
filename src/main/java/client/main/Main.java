package client.main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application{

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Alone In The Dark");
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //TODO Create a screen height and width variable and scale render off that
        //Set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.show();

        //TODO Create all rendering setup screens

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
                //printFPS if debuging
                LOGGER.debug("FPS:");
            }
        }.start();
    }

    public void init() {
        // TODO: Add setting up audio, graphics, input, audioHandler and connections
    }

    private void update() {

    }

    private void render() {

    }
}
