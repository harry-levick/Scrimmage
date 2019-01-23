package client.main;

import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import client.handlers.levelHandler.LevelHandler;
import client.handlers.levelHandler.Map;
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

public class Main extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
  public static KeyboardInput keyInput;
  public static MouseInput mouseInput;
  public static LevelHandler levelHandler;
  public static Settings settings;

  private Group root;
  private Scene scene;
  private Map currentMap;

  public static void main(String args[]) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, root, Version.CLIENT);
    currentMap = levelHandler.getMap();

    //Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        //Changes Map/Level
        if (currentMap != levelHandler.getMap()) {
          levelHandler.generateLevel(root);
          currentMap = levelHandler.getMap();
        }
        //Updates and Renders every object
        levelHandler.getGameObjects().forEach(gameObject -> gameObject.update());
        levelHandler.getGameObjects().forEach(gameObject -> gameObject.render());
        //TODO Add networking here
      }
    }.start();
  }

  public void init() {
    settings = new Settings();
    keyInput = new KeyboardInput();
    mouseInput = new MouseInput();
    // TODO: Add setting up audio, graphics, input, audioHandler and connections
  }

  private void setupRender(Stage primaryStage) {
    root = new Group();
    primaryStage.setTitle("Alone In The Dark");
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
