package client.main;

import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;

public class Main extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
  public static KeyboardInput keyInput;
  public static MouseInput mouseInput;
  public static LevelHandler levelHandler;
  public static Settings settings;
  public static Player clientPlayer;

  private static final float timeStep = 0.0166f;

  private String gameTitle = "Alone in the Dark";
  private static final int port = 4445;

  private Group root;
  private Scene scene;
  private Map currentMap;
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;

  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;
  private boolean multiplayer = false;
  private DatagramSocket socket;
  private InetAddress address;
  private byte[] buffer;

  public static void main(String args[]) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, root, true);
    currentMap = levelHandler.getMap();

    // Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Changes Map/Level
        if (currentMap != levelHandler.getMap()) {
          levelHandler.generateLevel(root, true);
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

        if (multiplayer) {
          buffer = KeyboardInput.getInput().getBytes();
          try {
            socket.send(new DatagramPacket(buffer, buffer.length, address, port));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

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
    maximumStep = 0.0166f;
    previousTime = 0;
    accumulatedTime = 0;
    settings = new Settings();
    keyInput = new KeyboardInput();
    mouseInput = new MouseInput();
    //Start off screen
    clientPlayer = new Player(-500, -500, UUID.randomUUID());

    if (multiplayer) {
      try {
        socket = new DatagramSocket();
      } catch (SocketException e) {
        e.printStackTrace();
      }
      try {
        address = InetAddress.getByName("localhost");
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }

  private void setupRender(Stage primaryStage) {
    root = new Group();
    primaryStage.setTitle(gameTitle);
    scene = new Scene(root, 1920, 1080);
    primaryStage.setScene(scene);
    primaryStage.setFullScreen(true);
    //Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    // TODO Create a screen height and width variable and scale render off that
    // Set Stage boundaries to visible bounds of the main screen
    //primaryStage.setX(primaryScreenBounds.getMinX());
    //primaryStage.setY(primaryScreenBounds.getMinY());
    //primaryStage.setWidth(primaryScreenBounds.getWidth());
    //primaryStage.setHeight(primaryScreenBounds.getHeight());
    primaryStage.show();

    // Setup Input
    scene.setOnKeyPressed(keyInput);
    scene.setOnKeyReleased(keyInput);
    scene.setOnMousePressed(mouseInput);
    scene.setOnMouseMoved(mouseInput);
    scene.setOnMouseReleased(mouseInput);
  }


}
