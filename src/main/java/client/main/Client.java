package client.main;

import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.PrimitiveDimensionProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.UI.UI;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.MachineGun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.packets.PacketPlayerJoin;
import shared.physics.Physics;
import shared.util.Path;

public class Client extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());
  public static LevelHandler levelHandler;
  public static Settings settings;
  public static boolean multiplayer;
  public static boolean singleplayerGame;
  public static ConnectionHandler connectionHandler;
  public static boolean sendUpdate;
  public static Timer timer = new Timer("Timer", true);
  public static int inputSequenceNumber;
  public static ArrayList<PacketInput> pendingInputs;
  public static TimerTask task;
  public static Group gameRoot;
  private final float timeStep = 0.0166f;
  private final String gameTitle = "Alone in the Dark";
  private LinkedList<Map> playlist;
  private KeyboardInput keyInput;
  private MouseInput mouseInput;
  private Group root;
  private Group backgroundRoot;
  private Scene scene;
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;
  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;
  private UI userInterface;
  private boolean gameOver;

  public static void main(String args[]) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    gameOver = false;
    SvgImageLoaderFactory.install(new PrimitiveDimensionProvider());
    playlist = new LinkedList<>();
    // Testing code
    for (int i = 1; i < 11; i++) {
      playlist.add(
          new Map(
              "Map" + i,
              Path.convert("src/main/resources/maps/map" + i + ".map"),
              GameState.IN_GAME));
    }

    /** Setup Game timer */
    task =
        new TimerTask() {
          @Override
          public void run() {
            gameOver = true;
          }
        };

    setupRender(primaryStage);
    inputSequenceNumber = 0;
    pendingInputs = new ArrayList<>();
    singleplayerGame = false;
    sendUpdate = false;
    levelHandler = new LevelHandler(settings, root, backgroundRoot, gameRoot);
    settings.setLevelHandler(levelHandler);
    levelHandler.addClientPlayer(gameRoot);
    keyInput = new KeyboardInput();
    mouseInput = new MouseInput();

    // Setup Input
    scene.setOnKeyPressed(keyInput);
    scene.setOnKeyReleased(keyInput);
    scene.setOnMousePressed(mouseInput);
    scene.setOnMouseMoved(mouseInput);
    scene.setOnMouseReleased(mouseInput);
    scene.setOnMouseDragged(mouseInput);

    //Setup UI
    userInterface = new UI(root, levelHandler.getClientPlayer());

    // Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {

        if (multiplayer) {
          processServerPackets();
        }
        levelHandler.createObjects();

        if (gameOver) {
          endGame();
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
          levelHandler
              .getGameObjects()
              .forEach((key, gameObject) -> gameObject.interpolatePosition(alphaRemaining));
          return;
        }

        while (accumulatedTime >= 2 * timeStep) {
          levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.update());
          accumulatedTime -= timeStep;
        }

        /** Apply Input */
        levelHandler.getClientPlayer().applyInput();

        if (multiplayer && sendUpdate) {
          sendInput();
          sendUpdate = false;
        }

        if (!multiplayer && singleplayerGame && levelHandler.getPlayers().size() > 1) {
          /** Calculate Score */
          ArrayList<Player> alive = new ArrayList<>();
          for (UUID key : levelHandler.getPlayers().keySet()) {
            Player p = levelHandler.getPlayers().get(key);
            if (p.isActive()) {
              alive.add(p);
            }
            if (alive.size() > 1) {
              break;
            }
          }
          if (alive.size() == 1) {
            alive.forEach(player -> player.increaseScore());
            Map nextMap = playlist.poll();
            levelHandler.changeMap(nextMap, true);
            giveWeapon();
          }
          /** Move bots */
          levelHandler.getBotPlayerList().forEach((key, bot) -> bot.applyInput());
        }

        /** Render Game Objects */
        levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.render());
        if (levelHandler.getBackground() != null) {
          levelHandler.getBackground().render();
        }

        /** Draw the UI */
        if (levelHandler.getGameState() == GameState.IN_GAME
            || levelHandler.getGameState() == GameState.Multiplayer) {
          userInterface.render();
        }

        /** Check Collisions */
        //TODO Change physics to LinkedHashMaps
        Physics.gameObjects = levelHandler.getGameObjects();

        levelHandler
            .getGameObjects()
            .forEach((key, gameObject) -> gameObject.updateCollision());
        Physics.processCollisions();

        /** Update Game Objects */
        levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.update());

        accumulatedTime -= timeStep;
        float alpha = accumulatedTime / timeStep;
        levelHandler.getGameObjects()
            .forEach((key, gameObject) -> gameObject.interpolatePosition(alpha));

        calculateFPS(secondElapsed, primaryStage);
      }
    }.start();
  }

  public void calculateFPS(float secondElapsed, Stage primaryStage) {
    elapsedSinceFPS += secondElapsed;
    framesElapsedSinceFPS++;
    if (elapsedSinceFPS >= 0.5f) {
      int fps = Math.round(framesElapsedSinceFPS / elapsedSinceFPS);
      primaryStage.setTitle(
          gameTitle
              + "   --    FPS: "
              + fps
              + "    Score: "
              + Client.levelHandler.getClientPlayer().getScore());
      elapsedSinceFPS = 0;
      framesElapsedSinceFPS = 0;
    }
  }

  public void init() {
    maximumStep = 0.0166f;
    previousTime = 0;
    accumulatedTime = 0;
    settings = new Settings();
    multiplayer = false;
    // Start off screen
  }

  public void endGame() {
    singleplayerGame = false;
    levelHandler.getPlayers().entrySet().removeAll(levelHandler.getBotPlayerList().entrySet());
    levelHandler.getBotPlayerList().forEach((key, gameObject) -> gameObject.removeRender());
    levelHandler.getBotPlayerList().forEach((key, gameObject) -> gameObject = null);
    levelHandler.getBotPlayerList().clear();
    levelHandler.changeMap(
        new Map(
            "Main Menu",
            Path.convert("src/main/resources/menus/main_menu.map"),
            GameState.MAIN_MENU),
        false);
  }

  private void setupRender(Stage primaryStage) {
    root = new Group();
    backgroundRoot = new Group();
    gameRoot = new Group();

    root.getChildren().add(backgroundRoot);
    root.getChildren().add(gameRoot);

    primaryStage.setTitle(gameTitle);
    primaryStage.getIcons().add(new Image(Path.convert("images/logo.png")));

    scene = new Scene(root, 1920, 1080);
    scene.setCursor(Cursor.CROSSHAIR);

    primaryStage.setScene(scene);
    primaryStage.setFullScreen(false);
    primaryStage.show();
  }

  public void sendInput() {
    PacketInput input =
        new PacketInput(
            levelHandler.getClientPlayer().mouseX,
            levelHandler.getClientPlayer().mouseY,
            levelHandler.getClientPlayer().leftKey,
            levelHandler.getClientPlayer().rightKey,
            levelHandler.getClientPlayer().jumpKey,
            levelHandler.getClientPlayer().click,
            levelHandler.getClientPlayer().getUUID(),
            inputSequenceNumber);
    connectionHandler.send(input.getString());
    input.setInputSequenceNumber(inputSequenceNumber);
    pendingInputs.add(input);
    inputSequenceNumber++;
  }

  private void processServerPackets() {
    if (connectionHandler.received.size() != 0) {
      try {
        String message = (String) connectionHandler.received.take();
        System.out.println(message);
        int messageID = Integer.parseInt(message.substring(0, 1));
        switch (messageID) {
          // PlayerJoin
          case 4:
            PacketPlayerJoin packetPlayerJoin = new PacketPlayerJoin(message);
            levelHandler.addPlayer(
                new Player(packetPlayerJoin.getX(), packetPlayerJoin.getY(),
                    packetPlayerJoin.getUUID(), levelHandler), gameRoot);
            break;
          // Ends
          case 6:
            Client.connectionHandler.end();
            Client.connectionHandler = null;
            // Show score board
            multiplayer = false;
            Client.levelHandler.changeMap(
                new Map(
                    "main_menu",
                    Path.convert("src/main/resources/menus/main_menu.map"),
                    GameState.IN_GAME),
                false);

            break;
          case 7:
            PacketGameState gameState = new PacketGameState(message);
            HashMap<UUID, String> data = gameState.getGameObjects();
            levelHandler
                .getGameObjects()
                .forEach(
                    (key, gameObject) -> {
                      if (!(gameObject instanceof MapDataObject)) {
                        gameObject.setState(data.get(gameObject.getUUID()));
                      }
                    });
            serverReconciliation(gameState.getLastProcessedInput());
            break;
          default:
            System.out.println("ERROR" + messageID + " " + message);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void serverReconciliation(int lastProcessedInput) {
    int j = 0;
    // Server Reconciliation. Re-apply all the inputs not yet processed by
    // the server.
    while (j < pendingInputs.size()) {
      if (inputSequenceNumber <= lastProcessedInput) {
        // Already processed. Its effect is already taken into account into the world update
        // we just got so drop it
        pendingInputs.remove(j);
      } else {
        Player player = levelHandler.getClientPlayer();
        PacketInput input = pendingInputs.get(j);
        // Not processed by the server yet. Re-apply it.
        player.mouseY = input.getY();
        player.mouseX = input.getX();
        player.jumpKey = input.isJumpKey();
        player.leftKey = input.isLeftKey();
        player.rightKey = input.isRightKey();
        player.click = false; // Don't want extra bullets
        player.applyInput();
        j++;
      }
    }
  }

  public void giveWeapon() {
    levelHandler
        .getClientPlayer()
        .setHolding(
            new MachineGun(
                500,
                500,
                "MachineGun@LevelHandler",
                Client.levelHandler.getClientPlayer(),
                UUID.randomUUID()));
    levelHandler.getGameObjects().put(Client.levelHandler.getClientPlayer().getHolding().getUUID(),
        Client.levelHandler.getClientPlayer().getHolding());
    levelHandler.getClientPlayer().getHolding().initialise(Client.gameRoot);
  }
}
