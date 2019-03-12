package shared.handlers.levelHandler;

import client.handlers.audioHandler.AudioHandler;
import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.main.Client;
import client.main.Settings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.Group;
import server.Server;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.background.Background;
import shared.gameObjects.players.Player;
import shared.gameObjects.rendering.ColorFilters;
import shared.gameObjects.weapons.MachineGun;
import shared.gameObjects.weapons.Sword;
import shared.gameObjects.weapons.Weapon;
import shared.util.maths.Vector2;

public class LevelHandler {

  private ConcurrentSkipListMap<UUID, GameObject> gameObjects;
  private CopyOnWriteArrayList<GameObject> toRemove;
  private LinkedHashMap<UUID, Player> players;
  private LinkedHashMap<UUID, Bot> bots;
  private Player clientPlayer;
  private ArrayList<Map> maps;
  private GameState gameState;
  private Map map;
  private Map previousMap;
  private Group backgroundRoot;
  private Group gameRoot;
  private Group uiRoot;
  private Background background;
  private ColorFilters filters;
  private AudioHandler musicPlayer;
  private Settings settings;
  private ArrayList<GameObject> toCreate;
  private boolean isServer;
  private Server server;
  private ByteArrayOutputStream byteArrayOutputStream;
  private ObjectOutput objectOutput;

  public LevelHandler(Settings settings, Group backgroundRoot, Group gameRoot,
      Group uiRoot) {
    this.settings = settings;
    gameObjects = new ConcurrentSkipListMap<>();
    toCreate = new ArrayList<>();
    toRemove = new CopyOnWriteArrayList<>();
    players = new LinkedHashMap<>();
    bots = new LinkedHashMap<>();
    maps = MapLoader.getMaps(settings.getMapsPath());
    filters = new ColorFilters();
    this.backgroundRoot = backgroundRoot;
    this.gameRoot = gameRoot;
    this.isServer = false;
    this.uiRoot = uiRoot;

    musicPlayer = new AudioHandler(settings, Client.musicActive);
    changeMap(Settings.getMainMenu(), true);
    previousMap = null;
  }

  public LevelHandler(Settings settings, Group backgroundRoot, Group gameRoot, Server server) {
    this.settings = settings;
    gameObjects = new ConcurrentSkipListMap<>();
    toRemove = new CopyOnWriteArrayList<>();
    players = new LinkedHashMap<>();
    bots = new LinkedHashMap<>();
    maps = MapLoader.getMaps(settings.getMapsPath());
    this.backgroundRoot = backgroundRoot;
    this.gameRoot = gameRoot;
    this.server = server;
    musicPlayer = new AudioHandler(settings, Client.musicActive);
    changeMap(Settings.getMultiplayerLobby(), false);
    previousMap = null;
  }

  public void changeMap(Map map, Boolean moveToSpawns) {
    previousMap = this.map;
    this.map = map;
    Client.closeSettingsOverlay();
    generateLevel(backgroundRoot, gameRoot, moveToSpawns);
    uiRoot.getChildren().clear();
    switch (gameState) {
      case IN_GAME:
      case MULTIPLAYER:
        if (Client.levelHandler != null) {
          Client.setUserInterface();
        }
        break;
    }
  }

  public void previousMap(Boolean moveToSpawns) {
    if (previousMap != null) {
      Map temp = this.map;
      this.map = previousMap;
      previousMap = temp;
      generateLevel(backgroundRoot, gameRoot, moveToSpawns);
    }
  }

  /**
   * NOTE: This to change the level use change Map Removes current game objects and creates new ones
   * from Map file
   */
  public void generateLevel(Group backgroundGroup, Group gameGroup, Boolean moveToSpawns) {

    gameObjects.keySet().removeAll(players.keySet());
    gameObjects.keySet().removeAll(bots.keySet());
    gameObjects.forEach((key, gameObject) -> gameObject.removeRender());
    gameObjects.forEach((key, gameObject) -> gameObject = null);
    gameObjects.clear();

    // Create new game objects for map
    gameObjects = MapLoader.loadMap(map.getPath());
    gameObjects.forEach(
        (key, gameObject) -> {
          gameObject.setSettings(settings);
          if (gameObject.getId() == ObjectType.MapDataObject) {
            this.background = ((MapDataObject) gameObject).getBackground();
            ArrayList<Vector2> spawnPoints = ((MapDataObject) gameObject).getSpawnPoints();
            this.map.setGameState(((MapDataObject) gameObject).getGameState());
            if (this.background != null) {
              background.initialise(backgroundGroup);
            }
            if (moveToSpawns && spawnPoints != null && spawnPoints.size() >= players.size()) {
              players.forEach(
                  (key2, player) -> {
                    Vector2 spawn = spawnPoints.get(0);
                    player.setX(spawn.getX());
                    player.setY(spawn.getY());
                    spawnPoints.remove(0);
                  });
            }

          } else {
            gameObject.initialise(gameGroup);
            if (isServer) {
              sendNewGameObject(gameObject);
            }
          }
        });
    gameObjects.putAll(players);
    gameObjects.forEach((key, gameObject) -> gameObject.setSettings(settings));
    gameState = map.getGameState();
    players.forEach((key, player) -> player.reset());

    musicPlayer.stopMusic();
    switch (gameState) {
      case IN_GAME:
        musicPlayer.playMusicPlaylist(PLAYLIST.INGAME);
        break;
      case MAIN_MENU:
      case LOBBY:
      case START_CONNECTION:
      case MULTIPLAYER:
      default:
        musicPlayer.playMusicPlaylist(PLAYLIST.MENU);
        break;

    }
    System.gc();
  }

  public void sendNewGameObject(GameObject gameObject) {
    byteArrayOutputStream = new ByteArrayOutputStream();
    objectOutput = null;
    try {
      byte[] packetID = "19,".getBytes();
      byteArrayOutputStream.write(packetID);
      objectOutput = new ObjectOutputStream(byteArrayOutputStream);
      objectOutput.writeObject(gameObject);
      objectOutput.flush();
      byte[] objectBytes = byteArrayOutputStream.toByteArray();
      server.sendToClients(objectBytes);
    } catch (IOException e) {
      System.out.println("Error Writing");
    } finally {
      try {
        byteArrayOutputStream.close();
      } catch (IOException ex) {
        System.out.println("Error Writing");
      }
    }
  }

  /**
   * List of all current game object
   *
   * @return All Game Objects
   */
  public ConcurrentSkipListMap<UUID, GameObject> getGameObjects() {
    clearToRemove(); // Remove every gameObjects we no longer need
    return gameObjects;
  }

  public Background getBackground() {
    return this.background;
  }

  /**
   * Add a new bullet to game object list
   *
   * @param gameObject GameObject to be added
   */
  public void addGameObject(GameObject gameObject) {
    gameObject.initialise(this.gameRoot);
    this.toCreate.add(gameObject);
    if (isServer) {
      sendNewGameObject(gameObject);
    }
  }

  public void addGameObject(ArrayList<GameObject> gameObjects) {
    gameObjects.forEach(gameObject -> gameObject.initialise(this.gameRoot));
    this.toCreate.addAll(gameObjects);
  }

  public void createObjects() {
    toCreate.forEach(gameObject -> gameObjects.put(gameObject.getUUID(), gameObject));
    toCreate.clear();
  }

  /**
   * Remove an existing bullet from game object list
   *
   * @param g GameObject to be removed
   */
  public void removeGameObject(GameObject g) {
    toRemove.add(g); // Will be removed on next frame
  }

  /**
   * List of all available maps
   *
   * @return All Maps
   */
  public ArrayList<Map> getMaps() {
    return maps;
  }

  /**
   * Current State of Game, eg Main_Menu or In_Game
   *
   * @return Game State
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Current map that is loaded
   *
   * @return Current Map
   */
  public Map getMap() {
    return map;
  }

  public LinkedHashMap<UUID, Player> getPlayers() {
    return players;
  }

  public void addPlayer(Player newPlayer, Group root) {
    newPlayer.initialise(root);
    players.put(newPlayer.getUUID(), newPlayer);
    gameObjects.put(newPlayer.getUUID(), newPlayer);
  }

  public void addClientPlayer(Group root) {
    clientPlayer = new Player(500, 200, UUID.randomUUID(), this);
    clientPlayer.initialise(root);
    players.put(clientPlayer.getUUID(), clientPlayer);
    gameObjects.put(clientPlayer.getUUID(), clientPlayer);

    // Add a spawn MachineGun
    Weapon spawnGun = new MachineGun(200, 350, "MachineGun.spawnGun@LevelHandler.addClientPlayer",
        null, UUID.randomUUID());
    spawnGun.initialise(root);
    gameObjects.put(spawnGun.getUUID(), spawnGun);

    // Add a spawn Sword
    Weapon spawnSword = new Sword(1300, 200, "Sword.spawnGun@LevelHandler.addClientPlayer", null,
        UUID.randomUUID());
    spawnSword.initialise(root);
    gameObjects.put(spawnSword.getUUID(), spawnSword);

    /*
    // Add weapon to player
    UUID gunUUID = UUID.randomUUID();
    Weapon gun = new MachineGun(clientPlayer.getX(), clientPlayer.getY(), "MachineGun@LevelHandler.addClientPlayer", clientPlayer, gunUUID);
    clientPlayer.setHolding(gun);
    gun.initialise(root);
    gameObjects.put(gunUUID, gun);
    */
  }

  public Player getClientPlayer() {
    return clientPlayer;
  }

  public LinkedHashMap<UUID, Bot> getBotPlayerList() {
    return bots;
  }

  public AudioHandler getMusicAudioHandler() {
    return this.musicPlayer;
  }

  /**
   * It removes the image from the imageView, destroy the gameObject and remove it from gameObjects
   * list. Finally clear the list for next frame
   */
  private void clearToRemove() {
    gameObjects.values().removeAll(toRemove);
    toRemove.forEach(gameObject -> gameObject.removeRender());
    toRemove.forEach(gameObject -> gameObject.destroy());
    toRemove.clear();
  }

  public Group getGameRoot() {
    return gameRoot;
  }
}
