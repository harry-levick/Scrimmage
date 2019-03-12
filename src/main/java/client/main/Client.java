package client.main;

import client.handlers.AchivementHandler.AchivementHandler;
import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import client.handlers.networkHandlers.ClientNetworkManager;
import client.handlers.networkHandlers.ConnectionHandler;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.PrimitiveDimensionProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import shared.gameObjects.menu.main.ButtonQuit;
import shared.gameObjects.menu.main.SoundSlider;
import shared.gameObjects.menu.main.SoundSlider.SOUND_TYPE;
import shared.gameObjects.objects.ObjectManager;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.MachineGun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
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
  private static Group uiRoot;
  private static Group creditsRoot;
  private static Group creditsBackground;
  private Scene scene;
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;
  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;
  private static UI userInterface;
  private static boolean credits = false;
  private static int creditStartDelay = 100;
  private static double resolutionX;
  private static double resolutionY;
  private boolean gameOver;
  private static boolean settingsOverlay = false;
  private static ArrayList<GameObject> settingsObjects = new ArrayList<>();
  private static ClientNetworkManager networkManager;

  public static void main(String args[]) {
    launch(args);
  }

  public static void setUserInterface() {
    userInterface = new UI(uiRoot, levelHandler.getClientPlayer());
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
      closeSettingsOverlay();
    }

    if (credits) {
      endCredits();
    }
  }

  public static void closeSettingsOverlay() {
    settingsOverlay = false;
    creditsRoot.getChildren().clear();
    settingsObjects.clear();
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

  public static void endCredits() {
    credits = false;
    creditStartDelay = 100; //todo magic number
    creditsRoot.getChildren().clear(); // deletes all children, removing all credit texts
    creditsBackground.getChildren().clear();
    levelHandler.getMusicAudioHandler()
        .playMusicPlaylist(PLAYLIST.MENU); //assume always return to menu map from credits
  }

  public void calculateFPS(float secondElapsed, Stage primaryStage) {
    elapsedSinceFPS += secondElapsed;
    framesElapsedSinceFPS++;
    if (elapsedSinceFPS >= 0.5f) {
      int fps = Math.round(framesElapsedSinceFPS / elapsedSinceFPS);
      primaryStage.setTitle(
          gameTitle
              + "   --    FPS: "
              + fps);
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
    resolutionX = settings.getMapWidth();
    resolutionY = settings.getMapHeight();
    networkManager = new ClientNetworkManager();
    // Start off screen
  }

  public void endGame() {
    singleplayerGame = false;
    levelHandler.getPlayers().entrySet().removeAll(levelHandler.getBotPlayerList().entrySet());
    levelHandler.getBotPlayerList().forEach((key, gameObject) -> gameObject.removeRender());
    levelHandler.getBotPlayerList().forEach((key, gameObject) -> gameObject = null);
    levelHandler.getBotPlayerList().clear();
    levelHandler.changeMap(Settings.getMainMenu(), false);
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
    levelHandler = new LevelHandler(settings, backgroundRoot, gameRoot, uiRoot);
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
    setUserInterface();

    AchivementHandler handler = new AchivementHandler(settings);
    handler.showAchivements(creditsRoot);

    // Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {

        if (multiplayer) {
          ClientNetworkManager.processServerPackets();
        }
        levelHandler.createObjects();

        if (gameOver) {
          endGame();
        }


        /** Apply Input */
        levelHandler.getClientPlayer().applyInput();

        if (multiplayer && sendUpdate) {
          ClientNetworkManager.sendInput();
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
        } else {
          ClientNetworkManager.update();
        }
        //Update Generic Object Timers
        ObjectManager.update();

        /** Scale and Render Game Objects */
        double resolutionXNew = primaryStage.getWidth();
        double resolutionYNew = primaryStage.getHeight();
        Vector2 scaleRatio = new Vector2(resolutionXNew / resolutionX,
            resolutionYNew / resolutionY);
        resolutionX = resolutionXNew;
        resolutionY = resolutionYNew;

        //levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.getTransform().scaleScreen(scaleRatio));
        levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.render());
        if (levelHandler.getBackground() != null) {
          levelHandler.getBackground().render();
        }

        /** Draw the UI */
        if (levelHandler.getGameState() == GameState.IN_GAME
            || levelHandler.getGameState() == GameState.MULTIPLAYER) {
          userInterface.render();
        }

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
    uiRoot = new Group();
    creditsRoot = new Group();
    creditsBackground = new Group();

    root.setStyle("-fx-font-family: Kenney Future");

    root.getChildren().add(backgroundRoot);
    root.getChildren().add(gameRoot);
    root.getChildren().add(uiRoot);
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
