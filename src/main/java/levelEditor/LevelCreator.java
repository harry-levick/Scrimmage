package levelEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.stage.Stage;
import shared.gameObjects.Blocks.Stone.StoneFloorObject;
import shared.gameObjects.Blocks.Stone.StoneWallObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.background.Background;
import shared.gameObjects.menu.main.ButtonBack;
import shared.gameObjects.menu.main.ButtonMultiplayer;
import shared.gameObjects.menu.main.ButtonSettings;
import shared.gameObjects.menu.main.ButtonSingleplayer;
import shared.gameObjects.menu.main.SoundSlider;
import shared.gameObjects.menu.main.SoundSlider.SOUND_TYPE;
import shared.gameObjects.menu.multiplayer.ButtonJoin;
import shared.gameObjects.players.Player;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.MapLoader;

public class LevelCreator extends Application {

  private static int stageSizeX = 1920; // todo autofetch
  private static int stageSizeY = 1080;
  private static int gridSizePX = 40;
  private static int gridSizeX = stageSizeX / gridSizePX; // 40 px blocks
  private static int gridSizeY = stageSizeY / gridSizePX; // 48 x 27

  private static ArrayList<GameObject> gameObjects;
  private static ArrayList<Player> playerSpawns;
  private static MapDataObject mapDataObject;

  private static int getAbs(int gridPos) {
    return gridPos * gridSizePX;
  }

  @Override
  public void start(Stage primaryStage) {
    Group root = new Group();
    // CLASS TO AUTO RECREATE MAPS
    String filename = "";
    String filepath =
        "src"
            + File.separator
            + "main"
            + File.separator
            + "resources"
            + File.separator
            + "menus"
            + File.separator;

    String filepathMaps =
        "src"
            + File.separator
            + "main"
            + File.separator
            + "resources"
            + File.separator
            + "maps"
            + File.separator;

    ////////////////////////////////////////
    // MAIN MENU
    ////////////////////////////////////////
    filename = "main_menu";
    gameObjects = new ArrayList<GameObject>();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MAIN_MENU);
    mapDataObject.setBackground(
        new Background(
            "images/backgrounds/background1.png", ObjectID.Background, UUID.randomUUID()));
    gameObjects.add(
        new ButtonSingleplayer(
            getAbs(20), getAbs(7), getAbs(8), getAbs(2), ObjectID.Button, UUID.randomUUID()));
    gameObjects.add(
        new ButtonMultiplayer(
            getAbs(20), getAbs(10), getAbs(8), getAbs(2), ObjectID.Button, UUID.randomUUID()));
    gameObjects.add(
        new ButtonSettings(
            getAbs(20), getAbs(13), getAbs(8), getAbs(2), ObjectID.Button, UUID.randomUUID()));

    for (int i = 0; i < 24; i++) {
      // top row wall
      gameObjects.add(
          new StoneWallObject(
              getAbs(i * 2), getAbs(0), getAbs(2), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // side col walls
      gameObjects.add(
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
      gameObjects.add(
          new StoneWallObject(
              getAbs(46),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.add(
          new StoneFloorObject(
              getAbs(i * 4), getAbs(25), getAbs(4), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // SINGLEPLAYER MAP
    ////////////////////////////////////////
    filename = "menu";
    gameObjects = new ArrayList<GameObject>();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.IN_GAME);
    mapDataObject.setBackground(
        new Background(
            "images/backgrounds/background1.png", ObjectID.Background, UUID.randomUUID()));
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.add(
          new StoneFloorObject(
              getAbs(i * 4), getAbs(25), getAbs(4), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map1" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map2" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map3" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map4" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map5" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map6" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map7" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map8" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map9" + ".map");
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map10" + ".map");

    ////////////////////////////////////////
    // MULTIPLAYER
    ////////////////////////////////////////
    filename = "multiplayer";
    gameObjects = new ArrayList<GameObject>();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MAIN_MENU);
    mapDataObject.setBackground(
        new Background(
            "images/backgrounds/background1.png", ObjectID.Background, UUID.randomUUID()));
    gameObjects.add(
        new ButtonJoin(
            getAbs(20), getAbs(7), getAbs(8), getAbs(2), ObjectID.Button, UUID.randomUUID()));
    for (int i = 0; i < 24; i++) {
      // top row wall
      gameObjects.add(
          new StoneWallObject(
              getAbs(i * 2), getAbs(0), getAbs(2), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // side col walls
      gameObjects.add(
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
      gameObjects.add(
          new StoneWallObject(
              getAbs(46),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.add(
          new StoneFloorObject(
              getAbs(i * 4), getAbs(25), getAbs(4), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // MULTIPLAYER LOBBY
    ////////////////////////////////////////
    filename = "lobby";
    gameObjects = new ArrayList<GameObject>();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MAIN_MENU);
    mapDataObject.setBackground(
        new Background(
            "images/backgrounds/background1.png", ObjectID.Background, UUID.randomUUID()));
    for (int i = 0; i < 24; i++) {
      // top row wall
      gameObjects.add(
          new StoneWallObject(
              getAbs(i * 2), getAbs(0), getAbs(2), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // side col walls
      gameObjects.add(
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
      gameObjects.add(
          new StoneWallObject(
              getAbs(46),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.add(
          new StoneFloorObject(
              getAbs(i * 4), getAbs(25), getAbs(4), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // SETTINGS
    ////////////////////////////////////////
    filename = "settings";
    gameObjects = new ArrayList<GameObject>();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MAIN_MENU);
    mapDataObject.setBackground(
        new Background("images/backgrounds/background1.png", ObjectID.Background,
            UUID.randomUUID()));
    gameObjects.add(new SoundSlider(getAbs(20), getAbs(7), getAbs(8), getAbs(1), SOUND_TYPE.MUSIC,
        "Music", ObjectID.Button, UUID.randomUUID()));
    gameObjects.add(new SoundSlider(getAbs(20), getAbs(9), getAbs(8), getAbs(1), SOUND_TYPE.SFX,
        "Sound Effects", ObjectID.Button, UUID.randomUUID()));
    gameObjects.add(new ButtonBack(getAbs(20), getAbs(12), getAbs(8), getAbs(2), ObjectID.Button,
        UUID.randomUUID()));
    for (int i = 0; i < 24; i++) {
      // top row wall
      gameObjects.add(
          new StoneWallObject(
              getAbs(i * 2), getAbs(0), getAbs(2), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // side col walls
      gameObjects.add(
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
      gameObjects.add(
          new StoneWallObject(
              getAbs(46),
              getAbs((i * 2) + 2),
              getAbs(2),
              getAbs(2),
              ObjectID.Bot,
              UUID.randomUUID()));
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.add(
          new StoneFloorObject(
              getAbs(i * 4), getAbs(25), getAbs(4), getAbs(2), ObjectID.Bot, UUID.randomUUID()));
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    System.out.println("RECREATED MAP FILES");

    Platform.exit();
  }
}
