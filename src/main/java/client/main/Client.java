package client.main;

import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.handlers.connectionHandler.ConnectionHandler;
import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.PrimitiveDimensionProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.GameObject;
import shared.gameObjects.UI.UI;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.Utils.TimePosition;
import shared.gameObjects.menu.main.ButtonQuit;
import shared.gameObjects.menu.main.SoundSlider;
import shared.gameObjects.menu.main.SoundSlider.SOUND_TYPE;
import shared.gameObjects.objects.ObjectManager;
import shared.gameObjects.players.Limbs.Arm;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.MachineGun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.maths.Vector2;

public class Client extends Application {

  public static boolean musicActive = true;

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
  private static Group creditsRoot;
  private static Group creditsBackground;
  private Scene scene;
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;
  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;
  private UI userInterface;
  private static boolean credits = false;
  private static int creditStartDelay = 100;
  private boolean gameOver;
  private static boolean settingsOverlay = false;
  private static ArrayList<GameObject> settingsObjects = new ArrayList<>();

  //Networking
  private final boolean prediction = false; //Broken
  private final boolean reconciliation = true;
  private final boolean setStateSnap = true; //Broken
  private final boolean entity_interpolation = true;

  public static void main(String args[]) {
    launch(args);
  }

  public static void settingsToggle() {
    // todo check if ingame
    // show/overlay settings
    if (settingsOverlay == false && levelHandler.getMap().getGameState() != GameState.SETTINGS) {
      settingsOverlay = true;

      int quitButtonExtraPadding = 0;
      switch (levelHandler.getGameState()) {
        case IN_GAME:
        case LOBBY:
        case HOST:
        case START_CONNECTION:
        case MULTIPLAYER:
          quitButtonExtraPadding = settings.getGrisPos(3);
          break;
        default:
          quitButtonExtraPadding = 0;
          break;
      }

      //add screen saturation

      //background
      try {
        Image popupBackground = new Image(new FileInputStream(
            settings.getResourcesPath() + File.separator + "images" + File.separator + "ui"
                + File.separator + "panel.png"));
        ImageView iv = new ImageView(popupBackground);
        iv.setFitWidth(settings.getGrisPos(12));
        iv.setFitHeight(settings.getGrisPos(7) + quitButtonExtraPadding);
        iv.setX(settings.getGrisPos(18));
        iv.setY(settings.getGrisPos(5));
        creditsRoot.getChildren().add(iv);
      } catch (FileNotFoundException e) {
        Rectangle rect = new Rectangle();
        rect.setWidth(settings.getGrisPos(12));
        rect.setHeight(settings.getGrisPos(7) + quitButtonExtraPadding);
        rect.setX(settings.getGrisPos(18));
        rect.setY(settings.getGrisPos(5));
        creditsRoot.getChildren().add(rect);
      }
      //add controls
      settingsObjects.add(
          new SoundSlider(settings.getGrisPos(20), settings.getGrisPos(7), settings.getGrisPos(8),
              settings.getGrisPos(1), SOUND_TYPE.MUSIC,
              "Music", ObjectType.Button, UUID.randomUUID()));
      settingsObjects.add(
          new SoundSlider(settings.getGrisPos(20), settings.getGrisPos(9), settings.getGrisPos(8),
              settings.getGrisPos(1), SOUND_TYPE.SFX,
              "Sound Effects", ObjectType.Button, UUID.randomUUID()));
      if (quitButtonExtraPadding != 0) {
        ButtonQuit quit = new ButtonQuit(
            settings.getGrisPos(20),
            settings.getGrisPos(11),
            settings.getGrisPos(8),
            settings.getGrisPos(2),
            ObjectType.Button,
            UUID.randomUUID());
        settingsObjects.add(quit);
      }
      settingsObjects.forEach(obj -> obj.initialiseAnimation());
      settingsObjects.forEach(obj -> obj.initialise(creditsRoot));
      settingsObjects.forEach(obj -> obj.render());



    } else {
      settingsOverlay = false;
      creditsRoot.getChildren().clear();
      settingsObjects.clear();
    }

    if (credits) {
      endCredits();
    }
  }

  public static void showCredits() {
    credits = true;
    ArrayList<String> lines = new ArrayList<String>();
    levelHandler.getMusicAudioHandler().playMusic(
        "LOCAL_FORECAST"); // not using playlist since assumed length of credits is less than the length of song
    Rectangle bg = new Rectangle(0, 0, settings.getWindowWidth(), settings.getWindowHeight());
    creditsBackground.getChildren().add(bg);
    try {
      BufferedReader reader = new BufferedReader(
          new FileReader(settings.getResourcesPath() + File.separator + "CREDITS.md"));
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
      reader.close();
    } catch (FileNotFoundException e) {
      // todo file not found
    } catch (IOException e) {
      // todo io exception
    }
    int yOffset = 0;
    int x = settings.getWindowWidth() / 2; // todo auto fetch
    int y = 200;
    ArrayList<Text> textList = new ArrayList<>();
    for (String line : lines) {
      if (!line.equals("")) {
        int extraBufferSpace = 0;
        double size = 20;
        FontWeight weight = FontWeight.NORMAL;
        FontPosture posture = FontPosture.REGULAR;
        // # title
        Pattern title1 = Pattern.compile("^# (.*)");
        Matcher m = title1.matcher(line);
        if (m.find()) {
          line = m.group(1);
          size = 40;
          weight = FontWeight.EXTRA_BOLD;
          extraBufferSpace = 50;
        }
        // ## title
        Pattern title2 = Pattern.compile("^## (.*)");
        m = title2.matcher(line);
        if (m.find()) {
          line = m.group(1);
          size = 30;
          weight = FontWeight.EXTRA_BOLD;
          extraBufferSpace = 50;
        }
        // *..* italics
        Pattern italic = Pattern.compile("(?<!\\*)\\*([^*]+)\\*(?!\\*)");
        m = italic.matcher(line);
        if (m.find()) {
          line = m.group(1);
          posture = FontPosture.ITALIC;
        }
        // **..** bold
        Pattern bold = Pattern.compile("(?<!\\*)\\*\\*([^*]+)\\*\\*(?!\\*)");
        m = bold.matcher(line);
        if (m.find()) {
          line = m.group(1);
          weight = FontWeight.BOLD;
        }
        // <br> blank
        Pattern blank = Pattern.compile("^<br>(.*)");
        m = blank.matcher(line);
        if (m.find()) {
          line = m.group(1);//m.group(1);
        }

        Text text = new Text();
        text.setText(line);
        text.setFont(Font.font("Helvetica", weight, posture, size));
        text.setFill(Color.WHITE);
        text.setLayoutX(x - (text.getLayoutBounds().getWidth() / 2));
        text.setLayoutY(y + extraBufferSpace + yOffset);
        y += 40 + extraBufferSpace;
        creditsRoot.getChildren().add(text);
      }
    }

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
            Path.convert(settings.getMenuPath() + File.separator + "main_menu.map")),
        false);
  }

  public static void endCredits() {
    credits = false;
    creditStartDelay = 100; //todo magic number
    creditsRoot.getChildren().clear(); // deletes all children, removing all credit texts
    creditsBackground.getChildren().clear();
    levelHandler.getMusicAudioHandler()
        .playMusicPlaylist(PLAYLIST.MENU); //assume always return to menu map from credits
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
              Path.convert(settings.getMapsPath() + File.separator + "map" + i + ".map")));
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
        if (settingsOverlay) {
          settingsObjects.forEach(obj -> obj.update());
        }

        /** Check Collisions */
        Physics.gameObjects = levelHandler.getGameObjects();

        levelHandler
            .getGameObjects()
            .forEach((key, gameObject) -> gameObject.updateCollision());
        Physics.processCollisions();

        if (!multiplayer) {
          /** Update Game Objects */
          levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.update());
        }
        //Update Generic Object Timers
        ObjectManager.update();

        if (multiplayer) {
          if (prediction) {
            levelHandler.getClientPlayer().update();
          }
          levelHandler.getPlayers().forEach((key, player) ->
              player.getChildren().forEach(child -> {
                child.update();
                if (child instanceof Arm) {
                  child.getChildren().forEach(childChild -> childChild.update());
                }
              })
          );
        }

        accumulatedTime -= timeStep;
        float alpha = accumulatedTime / timeStep;
        levelHandler.getGameObjects()
            .forEach((key, gameObject) -> gameObject.interpolatePosition(alpha));

        //Interpolate Networked Entities
        if (multiplayer && entity_interpolation) {
          interpolateEntities();
        }


        /** Draw the UI */
        if (levelHandler.getGameState() == GameState.IN_GAME
            || levelHandler.getGameState() == GameState.MULTIPLAYER) {
          userInterface.render();
        }

        calculateFPS(secondElapsed, primaryStage);

        // animate credits scrolling
        if (credits) {
          creditStartDelay--;
          if (creditStartDelay < 0 && creditStartDelay % 2 == 0) {
            int maxY = Integer.MIN_VALUE;
            if (creditsRoot.getChildren().size() != 0) {
              maxY = (int) creditsRoot.getChildren().get(0).getLayoutY();
            }
            for (Node node : creditsRoot.getChildren()) {
              node.setLayoutY(node.getLayoutY() - 1);
              maxY = Math.max(maxY, (int) node.getLayoutY());
            }
            if (maxY < -100) { //-100 for some buffer
              endCredits();
            }
          }
        }
      }
    }.start();
  }

  private void setupRender(Stage primaryStage) {
    root = new Group();
    backgroundRoot = new Group();
    gameRoot = new Group();
    creditsRoot = new Group();
    creditsBackground = new Group();

    root.setStyle("-fx-font-family: Kenney Future");

    root.getChildren().add(backgroundRoot);
    root.getChildren().add(gameRoot);
    root.getChildren().add(creditsBackground);
    root.getChildren().add(creditsRoot);

    primaryStage.setTitle(gameTitle);
    primaryStage.getIcons().add(new Image(Path.convert("images/logo.png")));

    scene = new Scene(root, settings.getWindowWidth(), settings.getWindowHeight());
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
        int messageID = Integer.parseInt(message.substring(0, 1));
        switch (messageID) {
          // Ends
          case 6:
            Client.connectionHandler.end();
            Client.connectionHandler = null;
            // Show score board
            multiplayer = false;
            Client.levelHandler.changeMap(
                new Map(
                    "main_menu",
                    Path.convert(settings.getMenuPath() + File.separator + "main_menu.map")),
                false);

            break;
          case 7:
            PacketGameState gameState = new PacketGameState(message);
            HashMap<UUID, String> data = gameState.getGameObjects();
            data.forEach((key, value) -> {
              GameObject gameObject = levelHandler.getGameObjects().get(key);
              if (gameObject == null) {
                createGameObject(value);
              } else {
                if (!entity_interpolation || gameObject.getUUID() == Client.levelHandler
                    .getClientPlayer().getUUID()) {
                  gameObject.setState(value, setStateSnap);
                } else {
                  Timestamp now = new Timestamp(System.currentTimeMillis());
                  String[] unpackedData = value.split(";");
                  Vector2 statePos = new Vector2(Double.parseDouble(unpackedData[2]),
                      Double.parseDouble(unpackedData[3]));
                  gameObject.getPositionBuffer().add(new TimePosition(now, statePos));
                }
              }
            });
            if (reconciliation) {
              serverReconciliation(Client.levelHandler.getClientPlayer().getLastInputCount());
            }
            break;
          default:
            System.out.println("ERROR" + messageID + " " + message);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void createGameObject(String data) {
    String[] unpackedData = data.split(";");
    switch (unpackedData[1]) {
      case "Player":
        Player player = new Player(Float.parseFloat(unpackedData[2]),
            Float.parseFloat(unpackedData[3]), UUID.fromString(unpackedData[0]),
            Client.levelHandler);
        Client.levelHandler.addPlayer(player, gameRoot);
        break;
      default:

    }
  }

  public void interpolateEntities() {
    Timestamp now = new Timestamp(System.currentTimeMillis() - (1000 / 60));
    //Need to calculate render timestamp

    levelHandler.getGameObjects().forEach((key, gameObject) -> {
      //Find the two authoritative positions surrounding the rendering timestamp
      ArrayList<TimePosition> buffer = gameObject.getPositionBuffer();

      if (gameObject.getUUID() != Client.levelHandler.getClientPlayer().getUUID() && buffer != null
          && buffer.size() > 0) {

        //Drop older positions
        while (buffer.size() >= 2 && buffer.get(1).getTimestamp().before(now)) {
          buffer.remove(0);
        }

        //Interpolate between the two surrounding  authoritative  positions to smooth motion
        if (buffer.size() >= 2 && buffer.get(0).getTimestamp().before(now) && now
            .before(buffer.get(1).getTimestamp())) {
          Vector2 pos0 = buffer.get(0).getPosition();
          Vector2 pos1 = buffer.get(1).getPosition();
          Long t0 = buffer.get(0).getTimestamp().getTime();
          Long t1 = buffer.get(1).getTimestamp().getTime();
          Long tnow = now.getTime();

          gameObject
              .setX(pos0.getX() + (pos1.getX() - pos0.getX()) * (tnow - t0) / (t1 - t0));
          gameObject
              .setY(pos0.getY() + (pos1.getY() - pos0.getY()) * (tnow - t0) / (t1 - t0));
        }
      }
    });
  }

  public void serverReconciliation(int lastProcessedInput) {
    int j = 0;
    // Server Reconciliation. Re-apply all the inputs not yet processed by
    // the server.
    while (j < pendingInputs.size()) {
      if (inputSequenceNumber - 1 <= lastProcessedInput) {
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
