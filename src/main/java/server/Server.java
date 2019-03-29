package server;

import client.main.Settings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.objects.ObjectManager;
import shared.gameObjects.players.Player;
import shared.gameObjects.rendering.ColourFilters;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import shared.util.maths.Vector2;

/**
 * The Server Application
 */
public class Server extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());
  /**
   * The level handler attached to the application
   */
  public static LevelHandler levelHandler;
  /**
   * The settings container attached to the application
   */
  public static Settings settings;
  private static Group gameRoot;
  /**
   * The number of players connected
   */
  public final AtomicInteger playerCount = new AtomicInteger(0);
  /**
   * The numbers of players that are ready to play
   */
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean ready = new AtomicBoolean(false);
  private final AtomicBoolean sendAllObjects = new AtomicBoolean(false);
  private final AtomicBoolean gameOver = new AtomicBoolean(false);
  private final AtomicInteger counter = new AtomicInteger(0);
  private final int maxPlayers = 2;
  private final int serverUpdateRate = 3;
  private final String gameTitle = "SERVER";
  /**
   * Current game state of the server
   */
  public ServerState serverState;
  private boolean startedGame;
  private int timeRemaining;
  private int timeLimit = 1;
  private Timer timer = new Timer("Timer", true);
  private ArrayList<InetAddress> connectedList = new ArrayList<>();
  private List connected = Collections.synchronizedList(connectedList);
  private String threadName;
  private ConcurrentMap<Player, BlockingQueue<PacketInput>> inputQueue;
  private int playerLastCount = 0;
  private ServerSocket serverSocket = null;
  private int serverPort = 4446;
  private ExecutorService executor;
  private Server server;
  private DatagramSocket socket;
  //Rendering
  private Group root;
  private Group backgroundRoot;
  private Scene scene;
  public static Group overlayRoot;
  private static Group uiRoot;
  private static Group overlayBackground;
  private static Group lightingRoot;

  public static void main(String args[]) {
    launch(args);
  }

  public static LevelHandler getLevelHandler() {
    return levelHandler;
  }

  /**
   * The end of the game, resets game back to main menu
   */
  public static void endGame() {
    // remove desaturation
    ColourFilters filter = new ColourFilters();
    filter.setDesaturate(0);
    filter.applyFilter(gameRoot, "desaturation");
    //Show Scores
    levelHandler.changeMap(
        new Map("menus/score.map", Path.convert("src/main/resources/menus/score.map")),
        true, true);
  }

  /**
   * Stops the server
   */
  public void stop() {
    running.set(false);
  }

  /**
   * Send data to clients
   *
   * @param buffer Data as a byte array
   * @param object If true, server is sending objects
   */
  public void sendToClients(byte[] buffer, boolean object) {
    synchronized (connected) {
      connected.forEach(
          address -> {
            try {
              //Send buffer size to set
              byte[] lengthBuffer = ("length:" + buffer.length).getBytes();
              DatagramPacket packet = new DatagramPacket(lengthBuffer, lengthBuffer.length,
                  (InetAddress) address, serverPort);
              socket.send(packet);

              if (object) {
                packet = new DatagramPacket("object".getBytes(), "object".getBytes().length,
                    (InetAddress) address, serverPort);
                socket.send(packet);
              }

              packet = new DatagramPacket(buffer, buffer.length,
                  (InetAddress) address, serverPort);
              socket.send(packet);
              LOGGER.debug("SEND: " + new String(buffer));
            } catch (UnknownHostException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

  //Updates the Physics of the objects on the Server
  private void updateSimulation() {
    /** Check Collisions */
    Physics.gameObjects = levelHandler.getGameObjects();
    inputQueue.forEach(
        ((player, packetInputs) -> {
          PacketInput temp = packetInputs.poll();
          if (temp != null) {
            LOGGER.debug("Input-:" + temp.getString());
            player.click = temp.isClick();
            player.rightKey = temp.isRightKey();
            player.leftKey = temp.isLeftKey();
            player.mouseX = temp.getX();
            player.mouseY = temp.getY();
            player.jumpKey = temp.isJumpKey();
            player.throwHoldingKey = temp.isThrowKey();
            player.setLastInputCount(temp.getInputSequenceNumber());
          }
        }));
    levelHandler.getPlayers().forEach((key, player) -> player.applyInput());

    levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.updateCollision());
    Physics.clearCollisions();
    /** Update Game Objects */
    levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.update());
    ObjectManager.update();
  }

  /**
   * Adds new player to input list
   *
   * @param player Player to add
   */
  public void add(Player player) {
    inputQueue.put(player, new LinkedBlockingQueue<PacketInput>());
  }


  /**
   * Gets the queue of items for a specific client
   *
   * @param player Player to get inputs for
   * @return Queue of player inputs received and not processed
   */
  public BlockingQueue<PacketInput> getQueue(Player player) {
    BlockingQueue<PacketInput> toRet = new LinkedBlockingQueue<>();
    try {
      toRet = inputQueue.get(player);
    } catch (Exception e) {

    }
    return toRet;
  }

  private void sendWorldState() {
    ArrayList<GameObject> gameObjectsFiltered = new ArrayList<>();
    for (UUID key : levelHandler.getGameObjects().keySet()) {
      GameObject gameObject = levelHandler.getGameObjects().get(key);
      if (!(gameObject instanceof MapDataObject)) {
        gameObjectsFiltered.add(gameObject);
      }
    }

    PacketGameState gameState = new PacketGameState(gameObjectsFiltered, sendAllObjects.get());
    sendAllObjects.set(false);

    if (gameState.isUpdate()) {
      byte[] buffer = gameState.getData();
      sendToClients(buffer, false);
    }
  }

  /**
   * Initializes the Server
   */
  public void init() {
    server = this;
    executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    threadName = "Server";
    settings = new Settings(levelHandler, gameRoot);
    running.set(true);
    startedGame = false;
    settings.setLevelHandler(levelHandler);
    inputQueue = new ConcurrentHashMap<>();
    try {
      this.serverSocket = new ServerSocket(4445);
      this.socket = new DatagramSocket();
    } catch (IOException e) {
      e.printStackTrace();
    }
    serverState = ServerState.WAITING_FOR_PLAYERS;
  }

  private void scaleRendering(Stage primaryStage) {
    Vector2 scaleRatio = new Vector2(scene.getWidth() / settings.getMapWidth(),
        scene.getHeight() / settings.getMapHeight());
    Scale scale = new Scale(scaleRatio.getX(), scaleRatio.getY(), 0, 0);
    primaryStage.getScene().getRoot().getTransforms().setAll(scale);
  }

  private void checkConditions() {
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
    if (alive.size() == 1 && serverState == ServerState.IN_GAME) {
      alive.forEach(player -> player.increaseScore());
      Map nextMap = levelHandler.pollPlayList();
      levelHandler.changeMap(nextMap, true, true);
    } else if (alive.size() == 1 && serverState == ServerState.WAITING_FOR_READYUP) {
      ready.set(true);
    }
  }

  /**
   * Begin the timer
   */
  private void startMatch() {
    if (!startedGame) {
      serverState = ServerState.IN_GAME;
      timeRemaining = timeLimit * 60;
      Timer secondsTimer = new Timer();
      secondsTimer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          System.out.println(String
              .format("%d:%d", timeRemaining / 60, timeRemaining - ((timeRemaining / 60) * 60)));
          timeRemaining -= 1;
        }
      }, 0, 1000);

      long delay = 1000l * 60l * timeLimit;
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              endGame();
              gameOver.set(true);
              secondsTimer.cancel();
            }
          });
        }
      }, delay);

      startedGame = true;
      serverState = ServerState.IN_GAME;
    }
  }

  @Override
  public void start(Stage primaryStage) {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, backgroundRoot, gameRoot, this);
    settings.setLevelHandler(levelHandler);
    settings.setGameRoot(gameRoot);
    LOGGER.debug("Running " + threadName);
    /** Receiver from clients */
    executor.execute(new ServerReceiver(this, serverSocket, connected));

    Physics.settings = settings;

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        counter.getAndIncrement();
        if (!running.get()) {
          this.stop();
        }

        //Allow player to join
        if (playerLastCount < playerCount.get() && server.playerCount.get() < 5
            && serverState == ServerState.WAITING_FOR_PLAYERS) {
          playerLastCount++;
          executor.execute(new ServerReceiver(server, serverSocket, connected));
        }

        //All players have joined
        if (playerCount.get() == maxPlayers && (serverState != ServerState.IN_GAME
            || serverState != ServerState.WAITING_FOR_READYUP)) {
          serverState = ServerState.WAITING_FOR_READYUP;
        }
        //Start game
        if (playerCount.get() > 1 && ready.get()) {
          startMatch();
        }

        /** Check Conditions */
        checkConditions();

        /** Process Update */
        updateSimulation();

        scaleRendering(primaryStage);

        /** Render Game Objects */
        levelHandler.getGameObjects().forEach((key, gameObject) -> gameObject.render());
        if (levelHandler.getBackground() != null) {
          levelHandler.getBackground().render();
        }

        /** Send update to all clients */
        if (playerCount.get() > 0 && counter.get() >= serverUpdateRate) {
          counter.set(0);
          sendWorldState();
        }
      }
    }.start();
  }

  /**
   * Sends a list of updated objects to all the clients
   *
   * @param gameobjects List of objects to send
   */
  public void sendObjects(ConcurrentLinkedHashMap<UUID, GameObject> gameobjects) {
    ByteArrayOutputStream byteArrayOutputStream = null;
    try {
      int i = 0;
      ArrayList list = new ArrayList();
      for (java.util.Map.Entry<UUID, GameObject> entry : gameobjects.entrySet()) {
        list.add(entry.getValue());
        if (i >= 18) {
          byteArrayOutputStream = new ByteArrayOutputStream();
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
          objectOutputStream.writeObject(list);
          objectOutputStream.flush();
          sendToClients(byteArrayOutputStream.toByteArray(), true);
          list.clear();
          i = 0;
        }
        i++;
      }
      byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(list);
      objectOutputStream.flush();
      sendToClients(byteArrayOutputStream.toByteArray(), true);
      list.clear();

    } catch (IOException e) {
      LOGGER.error("Unable to send new objects to clients ");
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        byteArrayOutputStream.close();
      } catch (IOException e) {
        LOGGER.error("Can't close Byte Array Output Stream on Server");
      }
    }
  }

  //Rendering; mostly for Debugging

  /**
   * Adds a player to the server and renders them
   *
   * @param joinPacket Packet of data responsible for join details
   * @param address IP address of the player
   * @return The player object
   */
  public Player addPlayer(PacketJoin joinPacket, InetAddress address) {
    Player player = new Player(joinPacket.getX(), joinPacket.getY(), joinPacket.getClientID());
    player.initialise(gameRoot, settings, joinPacket.getLegLeftUUID(), joinPacket.getLegRightUUID(),
        joinPacket.getBodyUUID(), joinPacket.getHeadUUID(), joinPacket.getArmLeftUUID(),
        joinPacket.getArmRightUUID(), joinPacket.getHandLeftUUID(), joinPacket.getHandRightUUID());
    levelHandler.addPlayer(player);
    playerCount.getAndIncrement();
    connected.add(address);
    server.add(player);
    server.sendObjects(levelHandler.getGameObjects());
    return player;
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
}
