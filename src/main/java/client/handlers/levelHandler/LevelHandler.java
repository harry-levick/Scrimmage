package client.handlers.levelHandler;

import client.main.Settings;
import shared.gameObjects.GameObject;

import java.util.ArrayList;

public class LevelHandler {
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Map> maps;
    private ArrayList<Map> menus;
    private GameState gameState;
    private Map map;

    public LevelHandler(Settings settings) {
        maps = MapLoader.getMaps(settings.getMapsPath());
        menus = MapLoader.getMaps(settings.getMenuPath());
        //TODO Load menu
        //TODO change menus load method to hashmap
    }

    public void changeLevel(Map map){
        gameObjects.forEach(gameObject -> gameObject.setActive(false));
        gameObjects.clear();
        gameObjects = MapLoader.loadMap(map.getPath());
        gameState = map.getGameState();
        this.map = map;
    }

}
