package shared.handlers.levelHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;

public class MapLoader {

  public static void saveMap(
      HashMap<UUID, GameObject> gameObjects, MapDataObject mapDataObject, String path) {
    try {
      FileOutputStream fos = new FileOutputStream(path);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      gameObjects.put(mapDataObject.getUUID(), mapDataObject);
      oos.writeObject(gameObjects);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static HashMap<UUID, GameObject> loadMap(String path) {
    try {
      FileInputStream fis = new FileInputStream(path);
      ObjectInputStream ois = new ObjectInputStream(fis);
      return (HashMap<UUID, GameObject>) ois.readObject();
    } catch (FileNotFoundException e) {
      return new HashMap<>();
    } catch (IOException e) {
      return new HashMap<>();
    } catch (ClassNotFoundException e) {
      return new HashMap<>();
    }
  }

  // TODO Replace with lambda
  // TODO Add map image and playlist
  public static ArrayList<Map> getMaps(String path) {
    /**
     * File dir = new File(path); File files[] = dir.listFiles( new FilenameFilter() { @Override
     * public boolean accept(File dir, String name) { return name.endsWith(".map"); } });s
     */
    File folder = new File(path);
    File[] list = folder.listFiles();
    ArrayList<Map> maps = new ArrayList<>();
    for (File file : Objects.requireNonNull(list)) {
      Map tempMap = new Map(file.getName(), file.getPath(), GameState.IN_GAME);
      maps.add(tempMap);
    }
    return maps;
  }

  public static HashMap<String, Map> getMenuMaps(String path) {
    File dir = new File(path);
    File files[] =
        dir.listFiles(
            new FilenameFilter() {
              @Override
              public boolean accept(File dir, String name) {
                return name.endsWith(".map");
              }
            });
    HashMap<String, Map> maps = new HashMap<>();
    for (File file : Objects.requireNonNull(files)) {
      Map tempMap = new Map(file.getName(), file.getPath(), GameState.MAIN_MENU);
      maps.put(file.getName(), tempMap);
    }
    return maps;
  }
}
