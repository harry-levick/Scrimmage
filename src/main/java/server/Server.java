package server;

import client.main.Client;
import client.main.Settings;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.AnimationTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.LevelHandler;
import shared.handlers.levelHandler.Map;
import shared.packets.PacketGameState;
import shared.packets.PacketInput;
import shared.physics.Physics;
import shared.util.Path;

public class Server implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(Client.class.getName());

  public static LevelHandler levelHandler;

  private Settings settings;
  public final AtomicInteger playerCount = new AtomicInteger(0);
  public final AtomicInteger readyCount = new AtomicInteger(0);
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final AtomicBoolean gameOver = new AtomicBoolean(false);
  private final AtomicInteger counter = new AtomicInteger(0);
  private final int serverUpdateRate = 10;
  private final int port = 4446;
  private final String address = "230.0.0.0";
  public ServerState serverState;
  public ConcurrentMap<UUID, BlockingQueue<PacketInput>> clientTable = new ConcurrentHashMap<>();
  private Thread t;
  private String threadName;
  private DatagramSocket socket;
  private InetAddress group;

  public Server() {
    threadName = "Server";
    settings = new Settings();
    levelHandler = new LevelHandler(settings, null, null, null, false);
    levelHandler.changeMap(
        new Map("Lobby", Path.convert("src/main/resources/menus/lobby.map"), GameState.Lobby));
  }

  public void start() {
    LOGGER.debug("Starting " + threadName);
    if (t == null) {
      t = new Thread(this, threadName);
      t.start();
    }
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

  }

  public void sendWorldState() {
    PacketGameState gameState = new PacketGameState(levelHandler.getGameObjects());
    byte[] buffer = gameState.getData();
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
    try {
      socket.send(packet);
    } catch (IOException e) {
      LOGGER.error("Error sending server message");
    }
  }

  public void setupSocket() {
    try {
      socket = new DatagramSocket();
      group = InetAddress.getByName(address);
    } catch (SocketException e) {
      LOGGER.error("Failed to create server socket");
    } catch (UnknownHostException e) {
      LOGGER.error("Failed to create server group");
    }

  }

  @Override
  public void run() {
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

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        if (!running.get()) {
          this.stop();
        }
        counter.getAndIncrement();

        /** Process Inputs and Update */
        processInputs();
        updateSimulation();

        /** Send update to all clients */
        if (counter.get() == serverUpdateRate) {
          counter.set(0);
          sendWorldState();
        }
      }
    }.start();
  }

  public ConcurrentMap<UUID, BlockingQueue<PacketInput>> getClientTable() {
    return clientTable;
  }
}
