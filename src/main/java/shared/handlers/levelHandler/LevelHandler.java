package shared.handlers.levelHandler;

import client.main.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.Version;

public class LevelHandler {

  private ArrayList<GameObject> gameObjects = new ArrayList<>();
  private ArrayList<Map> maps;
  private HashMap<String, Map> menus;
  private GameState gameState;
  private Map map;
  private Version version;

  public LevelHandler(Settings settings, Group root, Version version) {
    this.version = version;
    maps = MapLoader.getMaps(settings.getMapsPath());
    // menus = MapLoader.getMaps(settings.getMenuPath());
    // menus = MapLoader.getMenuMaps(settings.getMenuPath());
    // Set inital game level as the Main Menu
    map = maps.get(0); // FOR TESTING
    generateLevel(root);
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
  public void generateLevel(Group root) {
    // Remove current game objects
    gameObjects.forEach(gameObject -> gameObject.setActive(false));
    gameObjects.clear();

    // Create new game objects for map
    gameObjects = MapLoader.loadMap(map.getPath());
    gameObjects.forEach(gameObject -> gameObject.initialise(root, version, true));
    gameObjects.removeIf(gameObject -> !(gameObject instanceof GameObject));
    gameState = map.getGameState();
  }

  /**
   * List of all current game object
   *
   * @return All Game Objects
   */
  public ArrayList<GameObject> getGameObjects() {
    return gameObjects;
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
}
