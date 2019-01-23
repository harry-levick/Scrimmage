package client.handlers.levelHandler;

import client.main.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import shared.gameObjects.GameObject;

public class LevelHandler {

  private ArrayList<GameObject> gameObjects = new ArrayList<>();
  private ArrayList<Map> maps;
  private HashMap<String, Map> menus;
  private GameState gameState;
  private Map map;

  public LevelHandler(Settings settings) {
    maps = MapLoader.getMaps(settings.getMapsPath());
    menus = MapLoader.getMenuMaps(settings.getMenuPath());
    // Set inital game level as the Main Menu
    changeLevel(menus.get("MAIN_MENU"));
  }

  /**
   * Removes current game objects and creates new ones from Map file
   *
   * @param map Map object to change to, can be a Map that is a menu
   */
  public void changeLevel(Map map) {
    // Remove current game objects
    gameObjects.forEach(gameObject -> gameObject.setActive(false));
    gameObjects.clear();
    // Create new game objects for map

    gameObjects = MapLoader.loadMap(map.getPath());
    gameState = map.getGameState();
    this.map = map;
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
