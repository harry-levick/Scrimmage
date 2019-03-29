package shared.handlers.levelHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

  private ArrayList<Map> maps;
  private String name;

  public Playlist() {
    maps = new ArrayList<>();
  }

  public boolean addMap(Map map) {
    if (maps.contains(map)) return false;
    maps.add(map);
    return true;
  }

  public boolean addMap(String name, String filepath) {
    Map newMap = new Map(name, filepath);
    return addMap(newMap);
  }

  public ArrayList<Map> getMaps() {
    return maps;
  }

  public boolean deleteMap(Map map) {
    return maps.remove(map);
  }

  public boolean deleteMap(String name) {
    Map toRemove = null;
    for (Map map : maps) {
      if (map.getName().equals(name)) {
        toRemove = map;
        break;
      }
    }
    if(toRemove != null) {
      maps.remove(toRemove);
      return true;
    } else {
      return false;
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
