package shared.handlers.levelHandler;

import client.handlers.audioHandler.AudioHandler;
import client.main.Settings;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.Group;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.background.Background;
import shared.gameObjects.players.Player;
import shared.util.Path;
import shared.util.maths.Vector2;

public class LevelHandler {

  private ArrayList<GameObject> gameObjects;
  private ArrayList<GameObject> toRemove;
  private ArrayList<Player> players;
  private ArrayList<Bot> bots;
  private Player clientPlayer;
  private ArrayList<Map> maps;
  private GameState gameState;
  private Map map;
  private Group root;
  private Group backgroundRoot;
  private Group gameRoot;
  private Background background;
  private AudioHandler musicPlayer;
  private Settings settings;

  public LevelHandler(Settings settings, Group root, Group backgroundRoot, Group gameRoot) {
    this.settings = settings;
    gameObjects = new ArrayList<>();
    toRemove = new ArrayList<>();
    players = new ArrayList<>();
    bots = new ArrayList<>();
    maps = MapLoader.getMaps(settings.getMapsPath());
    this.root = root;
    this.backgroundRoot = backgroundRoot;
    this.gameRoot = gameRoot;
    musicPlayer = new AudioHandler(settings);
    clientPlayer = new Player(500, 200, UUID.randomUUID());
    clientPlayer.initialise(gameRoot);
    players.add(clientPlayer);
    changeMap(new Map("main_menu.map", Path.convert("src/main/resources/menus/main_menu.map"),
        GameState.IN_GAME), true);
  }

  public LevelHandler(Settings settings) {
    this.settings = settings;
    gameObjects = new ArrayList<>();
    toRemove = new ArrayList<>();
    players = new ArrayList<>();
    bots = new ArrayList<>();
    musicPlayer = new AudioHandler(settings);
  }

  public void changeMap(Map map, Boolean moveToSpawns) {
    this.map = map;
    players.forEach(player -> player.reset());
    generateLevel(root, backgroundRoot, gameRoot, moveToSpawns);
  }


  /**
   * NOTE: This to change the level use change Map Removes current game objects and creates new ones
   * from Map file
   */
  public void generateLevel(Group root, Group backgroundGroup, Group gameGroup,
      Boolean moveToSpawns) {

    gameObjects.removeAll(players);
    gameObjects.removeAll(bots);
    gameObjects.forEach(gameObject -> gameObject.removeRender());
    gameObjects.forEach(gameObject -> gameObject = null);
    gameObjects.clear();

    // Create new game objects for map
    gameObjects = MapLoader.loadMap(map.getPath());
    gameObjects.forEach(
        gameObject -> {
          if (gameObject.getId() == ObjectID.MapDataObject) {
            this.background = ((MapDataObject) gameObject).getBackground();
            ArrayList<Vector2> spawnPoints = ((MapDataObject) gameObject).getSpawnPoints();
            if (this.background != null) {
              background.initialise(backgroundGroup);
            }
            if (moveToSpawns && spawnPoints != null && spawnPoints.size() >= players.size()) {
              players.forEach(player -> {
                Vector2 spawn = spawnPoints.get(0);
                player.setX(spawn.getX());
                player.setY(spawn.getY());
                spawnPoints.remove(0);
              });
            }

          } else {
            gameObject.initialise(gameGroup);
          }
        });
    gameObjects.addAll(players);
    //gameObjects.addAll(bots);
    gameObjects.forEach(gameObject -> gameObject.setSettings(settings));
    gameState = map.getGameState();

    //musicPlayer.playMusicPlaylist();
    System.gc();
  }

  /**
   * List of all current game object
   *
   * @return All Game Objects
   */
  public ArrayList<GameObject> getGameObjects() {
    clearToRemove();    // Remove every gameObjects we no longer need
    return gameObjects;
  }

  public Background getBackground() {
    return this.background;
  }

  /**
   * Add a new bullet to game object list
   *
   * @param g GameObject to be added
   */
  public void addGameObject(GameObject g) {
    this.gameObjects.add(g);
    g.initialise(this.gameRoot);
  }

  /**
   * Remove an existing bullet from game object list
   *
   * @param g GameObject to be removed
   */
  public void delGameObject(GameObject g) {
    toRemove.add(g);  // Will be removed on next frame
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

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void addPlayer(Player newPlayer, Group root) {
    newPlayer.initialise(root);
    players.add(newPlayer);
    gameObjects.add(newPlayer);
  }

  public Player getClientPlayer() {
    return clientPlayer;
  }

  public ArrayList<Bot> getBotPlayerList() {
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
    gameObjects.removeAll(toRemove);
    toRemove.forEach(gameObject -> gameObject.removeRender());
    toRemove.forEach(gameObject -> gameObject.destroy());
    toRemove.clear();
  }

  public Group getGameRoot() {
    return gameRoot;
  }
}
