package levelEditor;

import client.main.Settings;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import de.codecentric.centerdevice.javafxsvg.dimension.PrimitiveDimensionProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.stage.Stage;
import shared.gameObjects.Blocks.Metal.MetalBlockLargeObject;
import shared.gameObjects.Blocks.Stone.StoneFloorObject;
import shared.gameObjects.Blocks.Stone.StoneWallObject;
import shared.gameObjects.Blocks.Wood.WoodBlockLargeObject;
import shared.gameObjects.Blocks.Wood.WoodBlockSmallObject;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.background.Background1;
import shared.gameObjects.background.Background2;
import shared.gameObjects.background.Background3;
import shared.gameObjects.background.Background4;
import shared.gameObjects.background.Background5;
import shared.gameObjects.background.Background6;
import shared.gameObjects.background.Background7;
import shared.gameObjects.background.Background8;
import shared.gameObjects.components.behaviours.blockBehaviours.Crushing;
import shared.gameObjects.menu.main.ButtonBack;
import shared.gameObjects.menu.main.ButtonCredits;
import shared.gameObjects.menu.main.ButtonMultiplayer;
import shared.gameObjects.menu.main.ButtonQuit;
import shared.gameObjects.menu.main.ButtonSettings;
import shared.gameObjects.menu.main.ButtonSingleplayer;
import shared.gameObjects.menu.main.SoundSlider;
import shared.gameObjects.menu.main.SoundSlider.SOUND_TYPE;
import shared.gameObjects.menu.multiplayer.ButtonJoin;
import shared.gameObjects.objects.utility.BlueBlock;
import shared.gameObjects.objects.utility.GreenBlock;
import shared.gameObjects.objects.utility.JumpPad;
import shared.gameObjects.objects.utility.RedBlock;
import shared.gameObjects.objects.utility.YellowBlock;
import shared.gameObjects.players.Player;
import shared.gameObjects.weapons.MachineGun;
import shared.gameObjects.weapons.WeaponSpawner;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.MapLoader;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * Script class for regenerating the main maps/levels used in the game
 */
public class LevelCreator extends Application {

  private static int stageSizeX = 1920; // todo autofetch
  private static int stageSizeY = 1080;
  private static int gridSizePX = 40;
  private static int gridSizeX = stageSizeX / gridSizePX; // 40 px blocks
  private static int gridSizeY = stageSizeY / gridSizePX; // 48 x 27

  private static ConcurrentLinkedHashMap<UUID, GameObject> gameObjects;
  private static ArrayList<Player> playerSpawns;
  private static MapDataObject mapDataObject;
  private UUID uuid = UUID.randomUUID();

  private Settings settings = new Settings(null, null);

  private static int getAbs(int gridPos) {
    return gridPos * gridSizePX;
  }

  /**
   * Script class for regenerating the main maps/levels used in the game
   *
   * @param primaryStage From JavaFX Application, not used in this class
   */
  @Override
  public void start(Stage primaryStage) {
    SvgImageLoaderFactory.install(new PrimitiveDimensionProvider());
    Group root = new Group();
    // CLASS TO AUTO RECREATE MAPS
    String filename = "";
    String filepath = settings.getMenuPath() + File.separator;

    String filepathMaps = settings.getMapsPath() + File.separator;

    ////////////////////////////////////////
    // MAIN MENU
    ////////////////////////////////////////
    System.out.println("Generating Main Menu");
    filename = "main_menu";
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MAIN_MENU);
    mapDataObject.setBackground(
        new Background1(uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid, new ButtonSingleplayer(
        getAbs(20), getAbs(6), getAbs(8), getAbs(2), ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid, new ButtonMultiplayer(
        getAbs(20), getAbs(9), getAbs(8), getAbs(2), ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid, new ButtonSettings(
        getAbs(20), getAbs(14), getAbs(8), getAbs(2), ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid, new ButtonQuit(
        getAbs(20), getAbs(17), getAbs(8), getAbs(2), ObjectType.Button, uuid));
    uuid = UUID.randomUUID();

    //ColouredBlocks todo remove form mm
    gameObjects
        .put(uuid, new RedBlock(getAbs(4), getAbs(10), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    //ColouredBlocks
    gameObjects.put(uuid,
        new BlueBlock(getAbs(5), getAbs(10), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    //ColouredBlocks
    gameObjects.put(uuid,
        new GreenBlock(getAbs(6), getAbs(10), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    //ColouredBlocks
    gameObjects.put(uuid,
        new YellowBlock(getAbs(7), getAbs(10), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();

    //JumpPad todo remove form mm
    gameObjects.put(uuid, new JumpPad(getAbs(2), getAbs(25), uuid));
    uuid = UUID.randomUUID();

    //Middle platforms
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(4), getAbs(6), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(8), getAbs(6), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(14), getAbs(12), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(4), getAbs(20), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(12), getAbs(20), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(31), getAbs(18), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(35), getAbs(5), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(37), getAbs(13), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();

    // left side blocks
    GameObject object = new WoodBlockLargeObject(
        getAbs(5), getAbs(4), getAbs(2), getAbs(2), ObjectType.Bot, uuid);
    object.addComponent(new Crushing(object));
    gameObjects.put(uuid, object);
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(6), getAbs(3), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
        gameObjects.put(uuid, new WeaponSpawner(
            getAbs(8), getAbs(4), getAbs(1), getAbs(1), uuid
        ));
        uuid = UUID.randomUUID();

    // right side blocks
    gameObjects.put(uuid,
        new WoodBlockLargeObject(
            getAbs(38), getAbs(25), getAbs(2), getAbs(2), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockLargeObject(
            getAbs(40), getAbs(25), getAbs(2), getAbs(2), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockLargeObject(
            getAbs(40), getAbs(23), getAbs(2), getAbs(2), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(37), getAbs(26), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(38), getAbs(24), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(42), getAbs(24), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(42), getAbs(25), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(42), getAbs(26), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new WoodBlockSmallObject(
            getAbs(43), getAbs(26), getAbs(1), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();

    for (int i = 0; i < 12; i++) {
      // top row wall
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(0), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 5; i++) {
      // side col walls
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              UUID.randomUUID()));
      uuid = UUID.randomUUID();
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(47),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              UUID.randomUUID()));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(26), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // SINGLEPLAYER MAP
    ////////////////////////////////////////
    System.out.println("Generating Single Player Map");
    filename = "menu";
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    playerSpawns = new ArrayList<>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.IN_GAME);
    mapDataObject.setBackground(
        new Background1(UUID.randomUUID()));
    uuid = UUID.randomUUID();
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(26), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }

    //Middle platforms
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(4), getAbs(6), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(8), getAbs(6), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(14), getAbs(12), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(4), getAbs(20), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(12), getAbs(20), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(31), getAbs(18), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(35), getAbs(5), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new StoneFloorObject(
            getAbs(37), getAbs(13), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
    uuid = UUID.randomUUID();

    //weapons
//    // Add a spawn MachineGun
//    Weapon spawnGun = new MachineGun(200, 350, "MachineGun.spawnGun@LevelHandler.addClientPlayer",
//        null, UUID.randomUUID());
//    gameObjects.put(spawnGun.getUUID(), spawnGun);
//
//    // Add a spawn Sword
//    Weapon spawnSword = new Sword(1300, 200, "Sword.spawnGun@LevelHandler.addClientPlayer", null,
//        UUID.randomUUID());
//    gameObjects.put(spawnSword.getUUID(), spawnSword);
//
//    // Add a spawn Uzi
//    Weapon spawnUzi = new Uzi(330, 350, "Uzi.spawnGun@LevelHandler.addClientPlayer", null,
//        UUID.randomUUID());
//    gameObjects.put(spawnUzi.getUUID(), spawnUzi);


    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map1" + ".map");
    mapDataObject.setBackground(new Background2(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map2" + ".map");
    mapDataObject.setBackground(new Background3(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map3" + ".map");
    mapDataObject.setBackground(new Background4(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map4" + ".map");
    mapDataObject.setBackground(new Background5(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map5" + ".map");
    mapDataObject.setBackground(new Background6(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map6" + ".map");
    mapDataObject.setBackground(new Background7(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map7" + ".map");
    mapDataObject.setBackground(new Background8(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map8" + ".map");
    mapDataObject.setBackground(new Background1(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map9" + ".map");
    mapDataObject.setBackground(new Background2(UUID.randomUUID()));
    MapLoader.saveMap(gameObjects, mapDataObject, filepathMaps + "map10" + ".map");

    ////////////////////////////////////////
    // MULTIPLAYER
    ////////////////////////////////////////
    System.out.println("Generating MULTIPLAYER Map");
    filename = "multiplayer";
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.MULTIPLAYER);
    mapDataObject.setBackground(
        new Background1(UUID.randomUUID()));
    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new ButtonJoin(
            getAbs(20), getAbs(7), getAbs(8), getAbs(2), ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    for (int i = 0; i < 12; i++) {
      // top row wall
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(0), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 5; i++) {
      // side col walls
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(47),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(26), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // MULTIPLAYER LOBBY
    ////////////////////////////////////////
    System.out.println("Generating LOBBY");
    filename = "lobby";
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.LOBBY);
    uuid = UUID.randomUUID();
    mapDataObject.setBackground(
        new Background1(uuid));
    uuid = UUID.randomUUID();
    for (int i = 0; i < 12; i++) {
      // top row wall
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(0), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    /**
     for (int i = 0; i < 10; i++) {
     // top row wall
     gameObjects.put(uuid,
     new StoneBlockObject(
     getAbs(i * 4 + 2), getAbs(20), getAbs(2), getAbs(2), ObjectType.Bot, uuid));
     uuid = UUID.randomUUID();
     }
     **/
    for (int i = 0; i < 5; i++) {
      // side col walls
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(47),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(26), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }

    //Gun
    uuid = UUID.randomUUID();
    gameObjects
        .put(uuid, new MachineGun(200, 350, "MachineGun.spawnGun@LevelHandler.addClientPlayer",
            null, uuid));

    uuid = UUID.randomUUID();
    gameObjects.put(uuid,
        new MetalBlockLargeObject(getAbs(10), getAbs(5), getAbs(2), getAbs(2), ObjectType.Bot,
            uuid));

    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    ////////////////////////////////////////
    // SETTINGS
    ////////////////////////////////////////
    System.out.println("Generating Settings");
    filename = "settings";
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    playerSpawns = new ArrayList<Player>();
    mapDataObject = new MapDataObject(UUID.randomUUID(), GameState.SETTINGS);
    uuid = UUID.randomUUID();
    mapDataObject.setBackground(
        new Background1(UUID.randomUUID()));
    uuid = UUID.randomUUID();
    gameObjects
        .put(uuid, new SoundSlider(getAbs(20), getAbs(7), getAbs(8), getAbs(1), SOUND_TYPE.MUSIC,
            "Music", ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    gameObjects
        .put(uuid, new SoundSlider(getAbs(20), getAbs(9), getAbs(8), getAbs(1), SOUND_TYPE.SFX,
            "Sound Effects", ObjectType.Button, uuid));
    uuid = UUID.randomUUID();
    gameObjects
        .put(uuid,
            new ButtonCredits(getAbs(20), getAbs(12), getAbs(8), getAbs(2), ObjectType.Button,
                uuid));
    uuid = UUID.randomUUID();
    gameObjects
        .put(uuid, new ButtonBack(getAbs(20), getAbs(15), getAbs(8), getAbs(2), ObjectType.Button,
            uuid));
    uuid = UUID.randomUUID();
    for (int i = 0; i < 12; i++) {
      // top row wall
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(0), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 5; i++) {
      // side col walls
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(0),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
      gameObjects.put(uuid,
          new StoneWallObject(
              getAbs(47),
              getAbs((i * 5) + 1),
              getAbs(1),
              getAbs(5),
              ObjectType.Bot,
              uuid));
      uuid = UUID.randomUUID();
    }
    for (int i = 0; i < 12; i++) {
      // bottom row floor
      gameObjects.put(uuid,
          new StoneFloorObject(
              getAbs(i * 4), getAbs(26), getAbs(4), getAbs(1), ObjectType.Bot, uuid));
      uuid = UUID.randomUUID();
    }
    MapLoader.saveMap(gameObjects, mapDataObject, filepath + filename + ".map");

    System.out.println("RECREATED MAP FILES");

    Platform.exit();
  }
}
