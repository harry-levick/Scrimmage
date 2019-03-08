package client.handlers.audioHandler;

import client.main.Client;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MusicAssets {

  private final HashMap<String, String> tracks = new HashMap<String, String>();
  private final ArrayList<String> menuPlaylist = new ArrayList<>();
  private final ArrayList<String> ingamePlaylist = new ArrayList<>();
  private String filePath = Client.settings.getMusicPath();

  public MusicAssets() {
    tracks.put("FUNK_GAME_LOOP", "funk-game-loop-by-kevin-macleod.mp3");
    tracks.put("EDM_DETECTION_MODE", "edm-detection-mode-by-kevin-macleod.mp3");
    tracks.put("GETTING_IT_DONE", "getting-it-done-by-kevin-macleod.mp3");
    tracks.put("LOCAL_FORECAST", "local-forecast---slower-by-kevin-macleod.mp3");
    tracks.put("MAN_DOWN", "man-down-by-kevin-macleod.mp3");
    tracks.put("OBLITERATION", "obliteration-by-kevin-macleod.mp3");
    tracks.put("SATIATE", "satiate---only-percussion-by-kevin-macleod.mp3");

    menuPlaylist.add("FUNK_GAME_LOOP");
    menuPlaylist.add("EDM_DETECTION_MODE");
    menuPlaylist.add("GETTING_IT_DONE");
    menuPlaylist.add("LOCAL_FORECAST");
    menuPlaylist.add("MAN_DOWN");
    Collections.shuffle(menuPlaylist);

    ingamePlaylist.add("OBLITERATION");
    ingamePlaylist.add("SATIATE");
    Collections.shuffle(ingamePlaylist);
  }

  protected ArrayList<String> getPlaylist(PLAYLIST playlist) {
    switch (playlist) {
      case MENU:
      default:
        return menuPlaylist;
      case INGAME:
        return ingamePlaylist;
    }
  }

  protected String getTrackPath(String trackIndex) {
    if (tracks.containsKey(trackIndex)) {
      return filePath + File.separator + tracks.get(trackIndex);
    } else {
      return null;
    }
  }

  public enum PLAYLIST {MENU, INGAME}
}
