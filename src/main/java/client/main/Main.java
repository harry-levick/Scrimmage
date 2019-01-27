package client.main;

import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.Utils.Version;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;

public class Main extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
  public static KeyboardInput keyInput;
  public static MouseInput mouseInput;
  public static LevelHandler levelHandler;
  public static Settings settings;

  private static final float timeStep = 0.0166f;

  private String gameTitle = "Alone in the Dark";

  private Group root;
  private Scene scene;
  private Map currentMap;
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;

  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;

  public static void main(String args[]) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, root, Version.CLIENT);
    currentMap = levelHandler.getMap();

    // Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Changes Map/Level
        if (currentMap != levelHandler.getMap()) {
          levelHandler.generateLevel(root);
          currentMap = levelHandler.getMap();
        }

        if (previousTime == 0) {
          previousTime = now;
          return;
        }

        float secondElapsed = (now - previousTime) / 1e9f; // time elapsed in seconds
        float secondsElapsedCapped = Math.min(secondElapsed, maximumStep);
        accumulatedTime += secondsElapsedCapped;
        previousTime = now;

        if (accumulatedTime < timeStep) {
          float timeSinceInterpolation = timeStep - (accumulatedTime - secondElapsed);
          float alphaRemaining = secondElapsed / timeSinceInterpolation;
          levelHandler.getGameObjects()
              .forEach(gameObject -> gameObject.interpolatePosition(alphaRemaining));
          return;
        }

        while (accumulatedTime >= 2 * timeStep) {
          levelHandler.getGameObjects().forEach(gameObject -> gameObject.update());
          accumulatedTime -= timeStep;
        }
        levelHandler.getGameObjects().forEach(gameObject -> gameObject.render());
        levelHandler.getGameObjects().forEach(gameObject -> gameObject.update());
        accumulatedTime -= timeStep;
        float alpha = accumulatedTime / timeStep;
        levelHandler.getGameObjects().forEach(gameObject -> gameObject.interpolatePosition(alpha));

        calculateFPS(secondElapsed, primaryStage);
      }
    }.start();
  }

  public void calculateFPS(float secondElapsed, Stage primaryStage) {
    elapsedSinceFPS += secondElapsed;
    framesElapsedSinceFPS++;
    if (elapsedSinceFPS >= 0.5f) {
      int fps = Math.round(framesElapsedSinceFPS / elapsedSinceFPS);
      primaryStage.setTitle(gameTitle + " FPS: " + fps);
      elapsedSinceFPS = 0;
      framesElapsedSinceFPS = 0;
    }
  }

  public void init() {
    maximumStep = Float.MAX_VALUE;
    previousTime = 0;
    accumulatedTime = 0;
    settings = new Settings();
    keyInput = new KeyboardInput();
    mouseInput = new MouseInput();
    // TODO: Add setting up audio, graphics, input, audioHandler and connections
  }

  private void setupRender(Stage primaryStage) {
    root = new Group();
    primaryStage.setTitle(gameTitle);
    scene = new Scene(root, 1000, 1000);
    primaryStage.setScene(scene);
    primaryStage.setFullScreen(false);
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    // TODO Create a screen height and width variable and scale render off that
    // Set Stage boundaries to visible bounds of the main screen
    primaryStage.setX(primaryScreenBounds.getMinX());
    primaryStage.setY(primaryScreenBounds.getMinY());
    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());
    primaryStage.show();

    // Setup Input
    scene.setOnKeyPressed(keyInput);
    scene.setOnKeyReleased(keyInput);
    scene.setOnMousePressed(mouseInput);
    scene.setOnMouseMoved(mouseInput);
    scene.setOnMouseReleased(mouseInput);
  }
}
