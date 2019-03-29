package client.main;

import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.handlers.inputHandler.KeyboardInput;
import client.handlers.inputHandler.MouseInput;
import client.handlers.networkHandlers.ClientNetworkManager;
import client.handlers.networkHandlers.ConnectionHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.transform.Scale;
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
import shared.gameObjects.rendering.ColourFilters;
import shared.gameObjects.weapons.MachineGun;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketInput;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.maths.Vector2;

/**
 * Main class of the game: The Game Client
 */
public class Client extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());
  /**
   * A temporary implementation for deactivating game music and sound effects
   */
  public static boolean musicActive = true;
  /**
   * Handler for changing levels/maps
   */
  public static LevelHandler levelHandler;

  /**
   * Global settings, defines some game settings/configs and paths of resources
   */
  public static Settings settings;

  /**
   * Boolean state if a multiplayer game is being played
   */
  public static boolean multiplayer;

  /**
   * Boolean state if a singleplayer game is being played
   */
  public static boolean singleplayerGame;

  /**
   * Server/Client connection handling
   */
  public static ConnectionHandler connectionHandler;

  /**
   * Boolean state if updates are being sent to the server
   */
  public static boolean sendUpdate;

  /**
   * Game timer
   */
  public static Timer timer = new Timer("Timer", true);

  /**
   * Tracking current input number
   */
  public static int inputSequenceNumber;

  /**
   * Tracking all inputs sent to server
   */
  public static ArrayList<PacketInput> pendingInputs;

  /**
   * Screen rendering scale ratio
   */
  public static Vector2 scaleRatio;

  /**
   * JavaFX root for all GameObjects
   */
  public static Group gameRoot;
  public static KeyboardInput keyInput;
  private static Group backgroundRoot;
  private static Group uiRoot;
  public static Group overlayRoot;
  private static Group overlayBackground;
  private static Group lightingRoot;
  private Group root;
  private Scene scene;
  private float elapsedSinceFPS = 0f;
  private int framesElapsedSinceFPS = 0;
  private static UI userInterface;
  private static boolean credits = false;
  private static int creditStartDelay = 100;
  private static boolean gameOver;
  private static boolean settingsOverlay = false;
  private static ArrayList<GameObject> settingsObjects = new ArrayList<>();
  private final String gameTitle = "Alone in the Dark";
  private final float timeStep = 0.0166f;
  private MouseInput mouseInput;
  private boolean startedGame;
  public static int timeRemaining;
  private int timeLimit = 1; // Time limit in minutes
  private float maximumStep;
  private long previousTime;
  private float accumulatedTime;


  /**
   * Launch game client
   */
  public static void main(String args[]) {
    launch(args);
  }

  /**
   * Sets a new user interface in the uiRoot, rendered by the main game loop depending on use
   */
  public static void setUserInterface() {
    userInterface = new UI(uiRoot, levelHandler.getClientPlayer(),settings);
  }

  /**
   * Toggle display the mini-settings overlay. Shares use of overlayRoot since the ui and credit are not displayed at the same time. Clears all elements in the JavaFX group when toggle off.
   * Toggle display the mini-settings overlay. Shares use of creditsRoot since the ui and credit are
   * not displayed at the same time. Clears all elements in the JavaFX group when toggle off.
   */
  public static void settingsToggle() {
    // todo check if ingame
    // show/overlay settings
    if (settingsOverlay == false &&
        levelHandler.getMap().getGameState() != GameState.SETTINGS &&
        levelHandler.getMap().getGameState() != GameState.MAIN_MENU) {
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
      ColourFilters filter = new ColourFilters();
      filter.setDesaturate(-0.5);
      filter.applyFilter(uiRoot, "desaturate");
      filter.applyFilter(gameRoot, "desaturate");
      filter.applyFilter(backgroundRoot, "desaturate");

      //background
      try {
        Image popupBackground = new Image(new FileInputStream(
            settings.getResourcesPath() + File.separator + "images" + File.separator + "ui"
                + File.separator + "panel.png"));
        ImageView iv = new ImageView(popupBackground);
        iv.setFitWidth(settings.getGrisPos(12));
        iv.setFitHeight(settings.getGrisPos(10) + quitButtonExtraPadding);
        iv.setX(settings.getGrisPos(18));
        iv.setY(settings.getGrisPos(3));
        overlayRoot.getChildren().add(iv);
      } catch (FileNotFoundException e) {
        Rectangle rect = new Rectangle();
        rect.setWidth(settings.getGrisPos(12));
        rect.setHeight(settings.getGrisPos(7) + quitButtonExtraPadding);
        rect.setX(settings.getGrisPos(18));
        rect.setY(settings.getGrisPos(5));
        overlayRoot.getChildren().add(rect);
      }
      //add controls
      settingsObjects.add(
          new SoundSlider(settings.getGrisPos(20), settings.getGrisPos(5), settings.getGrisPos(8),
              settings.getGrisPos(1), SOUND_TYPE.MUSIC,
              "Music", ObjectType.Button, UUID.randomUUID()));
      settingsObjects.add(
          new SoundSlider(settings.getGrisPos(20), settings.getGrisPos(9), settings.getGrisPos(8),
              settings.getGrisPos(1), SOUND_TYPE.SFX,
              "Sound Effects", ObjectType.Button, UUID.randomUUID()));
      if (quitButtonExtraPadding != 0) {
        ButtonQuit quit = new ButtonQuit(
            settings.getGrisPos(20) - 20,
            settings.getGrisPos(12),
            settings.getGrisPos(8),
            settings.getGrisPos(2),
            ObjectType.Button,
            UUID.randomUUID());
        settingsObjects.add(quit);
      }
      settingsObjects.forEach(obj -> obj.initialiseAnimation());
      settingsObjects.forEach(obj -> obj.initialise(overlayRoot, settings));
      settingsObjects.forEach(obj -> obj.render());
    } else {
      closeSettingsOverlay();
    }

    if (credits) {
      endCredits();
    }
  }

  /**
   * Clear the overlayRoot JavaFX group, hiding the overlay
   */
  public static void closeSettingsOverlay() {
    settingsOverlay = false;
    overlayRoot.getChildren().clear();
    settingsObjects.clear();
    ColourFilters filter = new ColourFilters();
    filter.setDesaturate(0); //todo change to remove method
    filter.applyFilter(uiRoot, "desaturate");
    filter.applyFilter(gameRoot, "desaturate");
    filter.applyFilter(backgroundRoot, "desaturate");
  }

  /**
   * Shows the game credits in the creditsRoot, uses the CREDITS.MD file, allowing styling by
   * italics or bold text, as well as optional 1st and 2nd size headers. A single <br> in any tag
   * will display the while line as empty.
   */
  public static void showCredits() {
    credits = true;
    ArrayList<String> lines = new ArrayList<String>();
    levelHandler.getMusicAudioHandler().playMusic(
        "LOCAL_FORECAST"); // not using playlist since assumed length of credits is less than the length of song
    Rectangle bg = new Rectangle(0, 0, settings.getMapWidth(), settings.getMapHeight());
    overlayBackground.getChildren().add(bg);
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
    int x = settings.getMapWidth() / 2; // todo auto fetch
    int y = 200;
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
        overlayRoot.getChildren().add(text);
      }
    }

  }

  /**
   * Hides the credits being displayed and resets the animation for the next time the credits are
   * displayed.
   */
  public static void endCredits() {
    credits = false;
    creditStartDelay = 100; //todo magic number
    overlayRoot.getChildren().clear(); // deletes all children, removing all credit texts
    overlayBackground.getChildren().clear();
    levelHandler.getMusicAudioHandler()
        .playMusicPlaylist(PLAYLIST.MENU); //assume always return to menu map from credits
  }

  /**
   * The end of the game, resets game back to main menu
   */
  public static void endGame() {
    singleplayerGame = false;
    gameOver = false;
    // remove desaturation
    ColourFilters filter = new ColourFilters();
    filter.setDesaturate(0);
    filter.applyFilter(gameRoot, "desaturation");
    filter.applyFilter(backgroundRoot, "desaturation");
    //Show Scores
    levelHandler.changeMap(
        new Map("menus/score.map", Path.convert("src/main/resources/menus/score.map")),
        true, false);
  }

  /**
   * Calculates the FPS of the game and sets it in the title of the game window
   * @param secondElapsed
   * @param primaryStage
   */
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

  /**
   * Initialise the settings and sets multiplayer as false
   */
  public void init() {
    maximumStep = 0.0166f;
    previousTime = 0;
    accumulatedTime = 0;
    settings = new Settings(levelHandler, gameRoot);
    multiplayer = false;
  }

  /**
   * Begin the timer
   */
  private void beginTimer() {
    if (!startedGame) {
      timeRemaining = timeLimit * 60;

     
      Timer secondsTimer = new Timer();
      secondsTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          //System.out.println(String
              //.format("%d:%d", timeRemaining / 60, timeRemaining - ((timeRemaining / 60) * 60)));
          timeRemaining -= 1;
        }
      }, 0, 1000);

      long delay = 1000l * 60l * timeLimit;
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          gameOver = true;
          secondsTimer.cancel();
        }
      }, delay);

      startedGame = true;
    }
  }
  // TODO change this to get the chosen playlist and all of its maps

  /**
   * Main game setup and game loop
   *
   * @param primaryStage The JavaFX stage the game is put in
   */
  @Override
  public void start(Stage primaryStage) {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, backgroundRoot, gameRoot, lightingRoot, uiRoot);
    settings.setLevelHandler(levelHandler);
    levelHandler.addClientPlayer(gameRoot);

    gameOver = false;
    inputSequenceNumber = 0;
    pendingInputs = new ArrayList<>();
    singleplayerGame = false;
    sendUpdate = false;
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

    Physics.settings = settings;

    // Main Game Loop
    new AnimationTimer() {
      @Override
      public void handle(long now) {

        if (!singleplayerGame && !multiplayer) {
          startedGame = false;
        }
        settings.setMultiplayer(multiplayer);

        if (multiplayer) {
          ClientNetworkManager.processServerPackets();
          levelHandler.processToCreate();
        }

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
        if (multiplayer) {
          levelHandler.getClientPlayer().applyMultiplayerInput();
        } else {
          levelHandler.getClientPlayer().applyInput();
        }

        if (multiplayer && sendUpdate) {
          ClientNetworkManager.sendInput();
          sendUpdate = false;
        }

        if (!multiplayer && singleplayerGame && levelHandler.getPlayers().size() > 1) {
          beginTimer();
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
            Map nextMap = levelHandler.pollPlayList();
            levelHandler.changeMap(nextMap, true, false);
          }
          /** Move bots */
          levelHandler.getBotPlayerList().forEach((key, bot) -> bot.applyInput());
        }

        if (settingsOverlay) {
          settingsObjects.forEach(obj -> obj.update());
        }

        if (!multiplayer) {
          /** Check Collisions */
          Physics.gameObjects = levelHandler.getGameObjects();

          levelHandler
              .getGameObjects()
              .forEach((key, gameObject) -> gameObject.updateCollision());
          Physics.clearCollisions();

          /** Update Game Objects */
          levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.update());
        } else {
          ClientNetworkManager.update();
        }
        //Update Generic Object Timers
        ObjectManager.update();

        accumulatedTime -= timeStep;
        float alpha = accumulatedTime / timeStep;
        levelHandler.getGameObjects()
            .forEach((key, gameObject) -> gameObject.interpolatePosition(alpha));

        /** Scale and Render Game Objects */
        scaleRendering(primaryStage);

        levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.render());
        if (levelHandler.getBackground() != null) {
          levelHandler.getBackground().render();
        }
        calculateFPS(secondElapsed, primaryStage);

        /** Draw the UI */
        if (levelHandler.getGameState() == GameState.IN_GAME) {
          userInterface.render();
        }

        // animate credits scrolling
        if (credits) {
          creditStartDelay--;
          if (creditStartDelay < 0 && creditStartDelay % 2 == 0) {
            int maxY = Integer.MIN_VALUE;
            if (overlayRoot.getChildren().size() != 0) {
              maxY = (int) overlayRoot.getChildren().get(0).getLayoutY();
            }
            for (Node node : overlayRoot.getChildren()) {
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


  /**
   * Initialises the rendering stage of the game setup
   *
   * @param primaryStage The JavaFX stage the game elements are to be placed into
   */
  private void setupRender(Stage primaryStage) {
    root = new Group();
    backgroundRoot = new Group();
    gameRoot = new Group();
    lightingRoot = new Group();
    uiRoot = new Group();
    overlayRoot = new Group();
    overlayBackground = new Group();

    root.setStyle("-fx-font-family: Kenney Future");

    root.getChildren().add(backgroundRoot);
    root.getChildren().add(gameRoot);
    root.getChildren().add(lightingRoot);
    root.getChildren().add(uiRoot);
    root.getChildren().add(overlayBackground);
    root.getChildren().add(overlayRoot);
    settings.setOverlay(overlayRoot);

    primaryStage.setTitle(gameTitle);
    primaryStage.getIcons().add(new Image(Path.convert("images/logo.png")));

    scene = new Scene(root, settings.getWindowWidth(), settings.getWindowHeight());
    scene.setCursor(Cursor.CROSSHAIR);
    scene.getStylesheets().add("style.css");

    primaryStage.setScene(scene);
    primaryStage.setFullScreen(false);
    primaryStage.show();
  }

  /**
   * Scaling of the window
   *
   * @param primaryStage The JavaFX stage to be scaled
   */
  public void scaleRendering(Stage primaryStage) {
    scaleRatio = new Vector2(scene.getWidth() / settings.getMapWidth(),
        scene.getHeight() / settings.getMapHeight());
    Scale scale = new Scale(scaleRatio.getX(), scaleRatio.getY(), 0, 0);
    scene.getRoot().getTransforms().setAll(scale);
  }

  /**
   * Gives a weapon to the client's player
   */
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
    levelHandler.getClientPlayer().getHolding().initialise(gameRoot, settings);
  }
}
