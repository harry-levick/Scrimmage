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
import java.util.LinkedList;
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
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.packets.PacketJoin;
import shared.packets.PacketMap;
import shared.physics.Physics;
import shared.util.Path;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import shared.util.maths.Vector2;

public class Server extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());

  public static LevelHandler levelHandler;
  public static Group gameRoot;
  public static Settings settings;
  public final AtomicInteger playerCount = new AtomicInteger(0);
  public final AtomicInteger readyCount = new AtomicInteger(0);
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean sendAllObjects = new AtomicBoolean(false);
  private final AtomicBoolean gameOver = new AtomicBoolean(false);
  private final AtomicInteger counter = new AtomicInteger(0);
  private boolean startedGame;
  private int timeRemaining;
  private int timeLimit = 1;
  private Timer timer = new Timer("Timer", true);
  private final int maxPlayers = 4;
  private final int serverUpdateRate = 3;
  private final String gameTitle = "SERVER";
  public ServerState serverState;
  private ArrayList<InetAddress> connectedList = new ArrayList<>();
  private List connected = Collections.synchronizedList(connectedList);
  private String threadName;
  private LinkedList<Map> playlist;
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

  public static void main(String args[]) {
    launch(args);
  }

  public static LevelHandler getLevelHandler() {
    return levelHandler;
  }

  public static Group getGameRoot() {
    return gameRoot;
  }

  public void init() {
    server = this;
    executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    threadName = "Server";
    settings = new Settings(levelHandler, gameRoot);
    running.set(true);
    settings.setLevelHandler(levelHandler);
    playlist = new LinkedList();
    inputQueue = new ConcurrentHashMap<>();
    try {
      this.serverSocket = new ServerSocket(4445);
      this.socket = new DatagramSocket();
    } catch (IOException e) {
      e.printStackTrace();
    }
    serverState = ServerState.WAITING_FOR_PLAYERS;

    // Testing code
    playlist.add(
        new Map("Map1", Path.convert("src/main/resources/maps/menu.map")));
    playlist.add(
        new Map("Map2", Path.convert("src/main/resources/maps/map2.map")));
  }

  public void stop() {
    running.set(false);
  }

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
              System.out.println("SEND: " + new String(buffer));
            } catch (UnknownHostException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

  public void updateSimulation() {
    levelHandler.createObjects();
    /** Check Collisions */
    Physics.gameObjects = levelHandler.getGameObjects();
    inputQueue.forEach(
        ((player, packetInputs) -> {
          PacketInput temp = packetInputs.poll();
          if (temp != null) {
            System.out.println("Input-:" + temp.getString());
            player.click = temp.isClick();
            player.rightKey = temp.isRightKey();
            player.leftKey = temp.isLeftKey();
            player.mouseX = temp.getX();
            player.mouseY = temp.getY();
            player.jumpKey = temp.isJumpKey();
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

  public void startMatch() {
    if (serverState == ServerState.WAITING_FOR_READYUP) {
      // Add bots
    }
    serverState = ServerState.IN_GAME;
    nextMap();
  }

  public void nextMap() {
    Map nextMap = playlist.pop();
    levelHandler.changeMap(nextMap, true, true);
    // TODO Change to actual UUID
    PacketMap mapPacket = new PacketMap(nextMap.getName(), UUID.randomUUID());
    sendToClients(mapPacket.getData(), false);
  }

  public void add(Player player) {
    inputQueue.put(player, new LinkedBlockingQueue<PacketInput>());
  }

  public BlockingQueue<PacketInput> getQueue(Player player) {
    return inputQueue.get(player);
  }

  public void sendWorldState() {
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

  public void checkConditions() {
    if (gameOver.get()) {

    } else {
      int dead = 0;
      for (UUID key : levelHandler.getPlayers().keySet()) {
        Player player = levelHandler.getPlayers().get(key);
        if (player.getHealth() <= 0) {
          dead++;
        }
      }
      if (playerCount.get() > 0 && dead == playerCount.get() || dead == (playerCount.get() - 1)) {
        nextMap();
      }
    }

  }

  public void scaleRendering(Stage primaryStage) {
    Vector2 scaleRatio = new Vector2(primaryStage.getWidth() / 1920,
        primaryStage.getHeight() / 1080);
    Scale scale = new Scale(scaleRatio.getX(), scaleRatio.getY(), 0, 0);
    primaryStage.getScene().getRoot().getTransforms().setAll(scale);
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
          System.out.println(String.format("%d:%d", timeRemaining / 60, timeRemaining - ((timeRemaining / 60) * 60)));
          timeRemaining -= 1;
        }
      }, 0, 1000);

      long delay = 1000l * 60l * timeLimit;
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          gameOver.set(true);
          secondsTimer.cancel();
        }
      }, delay);

      startedGame = true;
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    setupRender(primaryStage);
    levelHandler = new LevelHandler(settings, backgroundRoot, gameRoot, this);
    settings.setLevelHandler(levelHandler);
    settings.setGameRoot(gameRoot);
    LOGGER.debug("Running " + threadName);
    /** Receiver from clients */
    executor.execute(new ServerReceiver(this, serverSocket, connected));

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        if (!running.get()) {
          this.stop();
        }

        if (playerLastCount < playerCount.get()) {
          playerLastCount++;
          executor.execute(new ServerReceiver(server, serverSocket, connected));
        }

        if (playerCount.get() == 5) {
          //Bot bot = new Bot(500, 500, UUID.randomUUID(), levelHandler.getGameObjects(), levelHandler);
          //bot.initialise(null);
          //levelHandler.getPlayers().add(bot);
          //levelHandler.getBotPlayerList().add(bot);
          //levelHandler.getGameObjects().add(bot);
        }
        counter.getAndIncrement();
        if (playerCount.get() == maxPlayers) {
          serverState = ServerState.WAITING_FOR_READYUP;
        }
        if (playerCount.get() > 1 && readyCount.get() == playerCount.get()) {
          startMatch();
          beginTimer();
        }

        if (serverState == ServerState.IN_GAME) {
          checkConditions();
        }

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

  public void sendObjects(ConcurrentLinkedHashMap<UUID, GameObject> gameobjects) {
    ByteArrayOutputStream byteArrayOutputStream = null;
    try {
      byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(gameobjects);
      objectOutputStream.flush();

      sendToClients(byteArrayOutputStream.toByteArray(), true);
    } catch (IOException e) {
      LOGGER.error("Unable to send new objects to clients ");
      e.printStackTrace();
    } finally {
      try {
        byteArrayOutputStream.close();
      } catch (IOException e) {
        LOGGER.error("Can't close Byte Array Output Stream on Server");
      }
    }
  }

  public AtomicBoolean getSendAllObjects() {
    return sendAllObjects;
  }

  //Rendering

  public Player addPlayer(PacketJoin joinPacket, InetAddress address) {
    Player player = new Player(joinPacket.getX(), joinPacket.getY(), joinPacket.getClientID());
    levelHandler.addPlayer(player, gameRoot);
    playerCount.getAndIncrement();
    connected.add(address);
    server.add(player);
    return player;
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
}
