package server;

import client.main.Client;
import client.main.Settings;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.packets.PacketMap;
import shared.physics.Physics;
import shared.util.Path;

public class Server extends Application {

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());

  public static LevelHandler levelHandler;

  private Settings settings;
  public final AtomicInteger playerCount = new AtomicInteger(0);
  public final AtomicInteger readyCount = new AtomicInteger(0);
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean gameOver = new AtomicBoolean(false);
  private final AtomicInteger counter = new AtomicInteger(0);
  private final int serverUpdateRate = 10;
  private final int multicastPort = 4446;
  private final String multicastAddress = "230.0.0.0";
  private final int maxPlayers = 4;
  public ServerState serverState;
  public ConcurrentMap<UUID, BlockingQueue<PacketInput>> clientTable = new ConcurrentHashMap<>();
  private String threadName;
  private DatagramSocket socket;
  private InetAddress group;
  private LinkedList<Map> playlist;

  public static void main(String args[]) {
    launch(args);
  }

  public void init() {
    threadName = "Server";
    settings = new Settings();
    playlist = new LinkedList();

    //Testing code
    playlist
        .add(new Map("Map1", Path.convert("src/main/resources/maps/map1.map"), GameState.IN_GAME));
    playlist
        .add(new Map("Map2", Path.convert("src/main/resources/maps/map2.map"), GameState.IN_GAME));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    levelHandler = new LevelHandler(settings);
    levelHandler.changeMap(
        new Map("Lobby", Path.convert("src/main/resources/menus/lobby.map"), GameState.Lobby));
    running.set(true);
    LOGGER.debug("Running " + threadName);
    serverState = ServerState.WAITING_FOR_PLAYERS;
    setupSocket();
    /** Receiver from clients */
    ServerReceiver receiver = new ServerReceiver(this);
    receiver.start();

    /** Setup Game timer */
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        gameOver.set(true);
      }
    };
    Timer timer = new Timer("Timer", true);
    timer.schedule(task, 300000L);

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        if (!running.get()) {
          this.stop();
        }
        counter.getAndIncrement();
        if (playerCount.get() == maxPlayers) {
          serverState = ServerState.WAITING_FOR_READYUP;
        }
        if (playerCount.get() > 1 && readyCount.get() == playerCount.get()) {
          startMatch();
        }

        if (serverState == ServerState.IN_GAME) {
          checkConditions();
        }

        /** Process Inputs and Update */
        processInputs();
        updateSimulation();

        /** Send update to all clients */
        if (counter.get() == serverUpdateRate && playerCount.get() > 0) {
          counter.set(0);
          sendWorldState();
        }
      }
    }.start();
  }

  public void stop() {
    running.set(false);
  }


  public void updateSimulation() {
    /** Check Collisions */
    Physics.gameObjects = levelHandler.getGameObjects();
    levelHandler
        .getGameObjects()
        .forEach(gameObject -> gameObject.updateCollision(levelHandler.getGameObjects()));
    /** Update Game Objects */
    levelHandler.getGameObjects().forEach(gameObject -> gameObject.update());
  }

  public void processInputs() {
    for (Player player : levelHandler.getPlayers()) {
      PacketInput input = clientTable.get(player.getUUID()).poll();
      if (input != null) {
        player.mouseY = input.getY();
        player.mouseX = input.getX();
        player.leftKey = input.isLeftKey();
        player.rightKey = input.isRightKey();
        player.jumpKey = input.isJumpKey();
        player.click = input.isClick();
      }
    }
  }

  public void sendToClients(byte[] buffer) {
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, multicastPort);
    try {
      socket.send(packet);
    } catch (IOException e) {
      LOGGER.error("Error sending server message");
    }
  }

  public void sendWorldState() {
    ArrayList<GameObject> gameObjectsFiltered = new ArrayList<>();
    for (GameObject gameObject : levelHandler.getGameObjects()) {
      if (!(gameObject instanceof MapDataObject)) {
        gameObjectsFiltered.add(gameObject);
      }
    }
    PacketGameState gameState = new PacketGameState(gameObjectsFiltered);

    byte[] buffer = gameState.getData();
    sendToClients(buffer);
  }

  public void startMatch() {
    if (serverState == ServerState.WAITING_FOR_READYUP) {
      //Add bots
    }
    serverState = ServerState.IN_GAME;
    nextMap();
  }

  public void nextMap() {
    Map nextMap = playlist.pop();
    levelHandler.changeMap(nextMap);
    //TODO Change to actual UUID
    PacketMap mapPacket = new PacketMap(nextMap.getName(), UUID.randomUUID());
    sendToClients(mapPacket.getData());
  }

  public void checkConditions() {
    if (gameOver.get()) {

    } else {
      int dead = 0;
      for (Player player : levelHandler.getPlayers()) {
        if (player.getHealth() <= 0) {
          dead++;
        }
      }
      if (playerCount.get() > 0 && dead == playerCount.get() || dead == (playerCount.get() - 1)) {
        nextMap();
      }
    }
  }

  public void setupSocket() {
    try {
      socket = new DatagramSocket();
      group = InetAddress.getByName(multicastAddress);
    } catch (SocketException e) {
      LOGGER.error("Failed to create server socket");
    } catch (UnknownHostException e) {
      LOGGER.error("Failed to create server group");
    }

  }

  public ConcurrentMap<UUID, BlockingQueue<PacketInput>> getClientTable() {
    return clientTable;
  }
}
