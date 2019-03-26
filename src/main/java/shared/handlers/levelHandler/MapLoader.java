package shared.handlers.levelHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

/**
 * Saves and Loads the Game Objects stored in a map as a new level
 */
public class MapLoader {

  /**
   * Saves the contents of a map to a file to be loaded later
   * @param gameObjects List of gameObjects to save
   * @param mapDataObject List of Map Data Objects to save
   * @param path The filepath to save the file to
   */
  public static void saveMap(
      ConcurrentLinkedHashMap<UUID, GameObject> gameObjects, MapDataObject mapDataObject,
      String path) {
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
  /**
   * Loads the contents of a map to a file to be loaded later
   * @param path The filepath to save the file to
   */
  public static ConcurrentLinkedHashMap<UUID, GameObject> loadMap(String path) {
    try {
      FileInputStream fis = new FileInputStream(path);
      ObjectInputStream ois = new ObjectInputStream(fis);
      return (ConcurrentLinkedHashMap<UUID, GameObject>) ois.readObject();
    } catch (FileNotFoundException e) {
      return new ConcurrentLinkedHashMap.Builder<UUID, GameObject>().maximumWeightedCapacity(500)
          .build();
    } catch (IOException e) {
      return new ConcurrentLinkedHashMap.Builder<UUID, GameObject>().maximumWeightedCapacity(500)
          .build();
    } catch (ClassNotFoundException e) {
      return new ConcurrentLinkedHashMap.Builder<UUID, GameObject>().maximumWeightedCapacity(500)
          .build();
    }
  }

  // TODO Replace with lambda
  // TODO Add map image and playlist

  /**
   * Obtains all the maps in a string directory
   */
  public static ArrayList<Map> getMaps(String path) {
    /**
     * File dir = new File(path); File files[] = dir.listFiles( new FilenameFilter() { @Override
     * public boolean accept(File dir, String name) { return name.endsWith(".map"); } });s
     */
    File folder = new File(path);
    File[] list = folder.listFiles();
    ArrayList<Map> maps = new ArrayList<>();
    for (File file : Objects.requireNonNull(list)) {
      Map tempMap = new Map(file.getName(), file.getPath());
      maps.add(tempMap);
    }
    return maps;
  }
}
