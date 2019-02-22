package client.handlers.audioHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MusicAssets {

  private final HashMap<String, String> tracks = new HashMap<String, String>();
  private final ArrayList<String> playlist = new ArrayList<>();

  private String filePath =
      "src"
          + File.separator
          + "main"
          + File.separator
          + "resources"
          + File.separator
          + "audio"
          + File.separator
          + "music";

  public MusicAssets() {
    tracks.put("FUNK_GAME_LOOP", "funk-game-loop-by-kevin-macleod.mp3");
    tracks.put("EDM_DETECTION_MODE", "edm-detection-mode-by-kevin-macleod.mp3");
    tracks.put("GETTING_IT_DONE", "getting-it-done-by-kevin-macleod.mp3");
    tracks.put("LOCAL_FORECAST", "local-forecast---slower-by-kevin-macleod.mp3");
    tracks.put("MAN_DOWN", "man-down-by-kevin-macleod.mp3");
    tracks.put("OBLITERATION", "obliteration-by-kevin-macleod.mp3");
    tracks.put("SATIATE", "satiate---only-percussion-by-kevin-macleod.mp3");
  }

  protected String getTrackPath(String trackIndex) {
    if (tracks.containsKey(trackIndex)) {
      return filePath + File.separator + tracks.get(trackIndex);
    } else {
      return null;
    }
  }

  protected ArrayList<String> getPlaylist() {
    return playlist;
  }
}
