package shared.handlers.levelHandler;

import client.main.Settings;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.Handgun;
import shared.gameObjects.weapons.MachineGun;
import shared.gameObjects.weapons.Sword;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LevelHandler {

  private ArrayList<GameObject> gameObjects = new ArrayList<>();
  /*  toRemove will remove all gameObjects it contains from gameObjects list
   *  and clear the list for next frame. */
  private ArrayList<GameObject> toRemove = new ArrayList<>();
  private ArrayList<Player> players = new ArrayList<>();
  private Player clientPlayer;
  private ArrayList<Map> maps;
  private HashMap<String, Map> menus;
  private GameState gameState;
  private Map map;
  private Group root;

  public LevelHandler(Settings settings, Group root, boolean isClient) {
    this.root = root;
    if (isClient) {
      clientPlayer = new Player(500, 500, 100, 100, UUID.randomUUID());
      clientPlayer.setHolding(
          //new Handgun(500, 500, 100, 100, "Handgun", UUID.randomUUID())
          new MachineGun(500, 500, 100, 100, "MachineGun@LevelHandler", UUID.randomUUID())
          //new Sword(500, 500, 100, 100, "Sword@LevelHandler", 100, 50, 20, UUID.randomUUID())
        );
      clientPlayer.initialise(root);
      players.add(clientPlayer);
    }
    maps = MapLoader.getMaps(settings.getMapsPath());
    // menus = MapLoader.getMaps(settings.getMenuPath());
    // menus = MapLoader.getMenuMaps(settings.getMenuPath());
    // Set initial game level as the Main Menu
    map = maps.get(0); // FOR TESTING
    generateLevel(root, isClient);

    gameObjects.add(clientPlayer.getHolding());
    clientPlayer.getHolding().initialise(root);
  }

  public boolean changeMap(Map map) {
    if (maps.contains(map)) {
      this.map = map;
      return true;
    }
    return false;
  }

  public boolean changeMenu(Map menu) {
    if (menus.containsValue(menu)) {
      this.map = menu;
      return true;
    }
    return false;
  }

  /**
   * NOTE: This to change the level use change Map Removes current game objects and creates new ones
   * from Map file
   */
  public void generateLevel(Group root, boolean isClient) {
    // Remove current game objects
    gameObjects.forEach(gameObject -> gameObject.setActive(false));
    gameObjects.clear();

    // Create new game objects for map
    gameObjects = MapLoader.loadMap(map.getPath());
    gameObjects.forEach(
        gameObject -> {
          if (gameObject.getId() == ObjectID.MapDataObject && isClient) {
            // clientPlayer.setX(gameObject.getX());
            // clientPlayer.setY(gameObject.getY());
            gameObjects.remove(gameObject);
          } else {
            gameObject.initialise(root);
          }
        });
    gameObjects.add(clientPlayer);
    gameState = map.getGameState();
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
  
  /**
   * Add a new bullet to game object list
   * 
   * @param g GameObject to be added
   */
  public void addGameObject(GameObject g) {
    this.gameObjects.add(g);
    g.initialise(this.root);
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
   * List of all available menus,
   *
   * @return All Menus
   */
  public HashMap<String, Map> getMenus() {
    return menus;
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

  public void addPlayer(Player newPlayer) {
    players.add(newPlayer);
  }

  public Player getClientPlayer() {
    return clientPlayer;
  }
  
  /**
   *  It removes the image from the imageView,
   *  destroy the gameObject and
   *  remove it from gameObjects list.
   *  Finally clear the list for next frame
   */
  private void clearToRemove() {
    gameObjects.removeAll(toRemove);
    toRemove.forEach(gameObject -> gameObject.removeRender());
    toRemove.forEach(gameObject -> gameObject.destroy());
    toRemove.clear();
  }
}
