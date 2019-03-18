package shared.handlers.levelHandler;

import client.handlers.audioHandler.AudioHandler;
import client.handlers.audioHandler.MusicAssets.PLAYLIST;
import client.main.Client;
import client.main.Settings;
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
import shared.gameObjects.weapons.Uzi;
import shared.gameObjects.weapons.Weapon;
import shared.util.Path;
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
    changeMap(
        new Map("menus/main_menu.map", Path.convert("src/main/resources/menus/main_menu.map")),
        true, false);
    previousMap = null;
  }

  public LevelHandler(Settings settings, Group backgroundRoot, Group gameRoot, Server server) {
    this.settings = settings;
    gameObjects = new ConcurrentSkipListMap<>();
    toRemove = new CopyOnWriteArrayList<>();
    players = new LinkedHashMap<>();
    toCreate = new ArrayList<>();
    bots = new LinkedHashMap<>();
    maps = MapLoader.getMaps(settings.getMapsPath());
    this.isServer = true;
    this.backgroundRoot = backgroundRoot;
    this.gameRoot = gameRoot;
    this.server = server;
    changeMap(new Map("LOBBY", Path.convert("src/main/resources/menus/lobby.map")),
        false, true);
    previousMap = null;
  }

  public void changeMap(Map map, Boolean moveToSpawns, Boolean isServer) {
    previousMap = this.map;
    this.map = map;
    if (!isServer) {
      Client.closeSettingsOverlay();
    }
    generateLevel(backgroundRoot, gameRoot, moveToSpawns, isServer);

    if (!isServer) {
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
  }

  public void previousMap(Boolean moveToSpawns) {
    if (previousMap != null) {
      Map temp = this.map;
      this.map = previousMap;
      previousMap = temp;
      generateLevel(backgroundRoot, gameRoot, moveToSpawns, false);
    }
  }

  /**
   * NOTE: This to change the level use change Map Removes current game objects and creates new ones
   * from Map file
   */
  public void generateLevel(Group backgroundGroup, Group gameGroup, Boolean moveToSpawns,
      Boolean isServer) {

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
              background.initialise(backgroundGroup, settings);
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
            gameObject.initialise(gameGroup, settings);
          }
        });
    gameObjects.putAll(players);
    gameObjects.forEach((key, gameObject) -> gameObject.setSettings(settings));
    gameState = map.getGameState();
    players.forEach((key, player) -> {
      player.reset();
      player.setHolding(new MachineGun(player.getX(), player.getY(),
          "MachineGun@LevelHandler", player, UUID.randomUUID()));

      gameObjects.put(player.getHolding().getUUID(), player.getHolding());
      player.getHolding().initialise(gameRoot, settings);
    });

    bots.forEach((key, bot) -> {
      bot.reset();
      bot.setHolding(new MachineGun(bot.getX(), bot.getY(), "MachineGun@LevelHandler", bot,
          UUID.randomUUID()));

      gameObjects.put(bot.getHolding().getUUID(), bot.getHolding());
      bot.getHolding().initialise(gameRoot, settings);
    });

    if (!isServer) {
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
    } else {
      server.sendObjects(gameObjects);
    }
    System.gc();
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

  public ConcurrentSkipListMap<UUID, GameObject> getGameObjectsFiltered() {
    clearToRemove(); // Remove every gameObjects we no longer need
    ConcurrentSkipListMap<UUID, GameObject> filtered = gameObjects.clone();
    players.forEach((key, player) -> filtered.remove(key));
    return filtered;
  }

  public Background getBackground() {
    return this.background;
  }

  /**
   * Add a gameobject to list to be created next update
   *
   * @param gameObject GameObject to be added
   */
  public void addGameObject(GameObject gameObject) {
    gameObject.initialise(this.gameRoot, settings);
    this.toCreate.add(gameObject);
    if (isServer) {
      ConcurrentSkipListMap<UUID, GameObject> temp = new ConcurrentSkipListMap<>();
      temp.put(gameObject.getUUID(), gameObject);
      server.sendObjects(temp);
      System.out.println("DIDIDIDIIDIDIDIDIDIDIID");
    }
  }

  public void addGameObjects(ConcurrentSkipListMap<UUID, GameObject> gameObjectsT) {
    gameObjectsT.forEach(((uuid, gameObject) -> {
      if (!gameObjects.containsKey(uuid)) {
        this.toCreate.add(gameObject);
      }
    }));
  }

  public void createObjects() {
    toCreate.forEach(gameObject -> {
      gameObject.initialise(gameRoot, settings);
      if (gameObject instanceof Player && gameObject.getUUID() != clientPlayer.getUUID()) {
        addPlayer((Player) gameObject, settings.getGameRoot());
      } else {
        gameObjects.put(gameObject.getUUID(), gameObject);
      }
    });
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
    newPlayer.initialise(root, settings);
    players.put(newPlayer.getUUID(), newPlayer);
    gameObjects.put(newPlayer.getUUID(), newPlayer);
  }

  public void addClientPlayer(Group root) {
    clientPlayer = new Player(500, 200, UUID.randomUUID());
    clientPlayer.initialise(root, settings);
    players.put(clientPlayer.getUUID(), clientPlayer);
    gameObjects.put(clientPlayer.getUUID(), clientPlayer);

    // Add a spawn MachineGun
    Weapon spawnGun = new MachineGun(200, 350, "MachineGun.spawnGun@LevelHandler.addClientPlayer",
        null, UUID.randomUUID());
    spawnGun.initialise(root, settings);
    gameObjects.put(spawnGun.getUUID(), spawnGun);

    // Add a spawn Sword
    Weapon spawnSword = new Sword(1300, 200, "Sword.spawnGun@LevelHandler.addClientPlayer", null,
        UUID.randomUUID());
    spawnSword.initialise(root, settings);
    gameObjects.put(spawnSword.getUUID(), spawnSword);

    // Add a spawn Uzi
    Weapon spawnUzi = new Uzi(330, 350, "Uzi.spawnGun@LevelHandler.addClientPlayer", null,
        UUID.randomUUID());
    spawnUzi.initialise(root, settings);
    gameObjects.put(spawnUzi.getUUID(), spawnUzi);

    /*
    // Add weapon to player
    UUID gunUUID = UUID.randomUUID();
    Weapon gun = new Uzi(clientPlayer.getX(), clientPlayer.getY(), "Uzi@LevelHandler.addClientPlayer", clientPlayer, gunUUID);
    clientPlayer.setHolding(gun);
    gun.initialise(root, settings);
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
