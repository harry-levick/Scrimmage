package client.handlers.audioHandler;

import client.main.Settings;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Defines all of the MUSIC assets used by AudioHandler
 */
public class MusicAssets {

  private final HashMap<String, String> tracks = new HashMap<String, String>();
  private final ArrayList<String> menuPlaylist = new ArrayList<>();
  private final ArrayList<String> ingamePlaylist = new ArrayList<>();
  private String filePath;
  private Settings settings;

  /**
   * Constructor:
   *
   * @param settings Main game settings, required for getting paths.
   */
  public MusicAssets(Settings settings) {
    this.settings = settings;
    filePath = settings.getMusicPath();
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
    menuPlaylist.add("MAN_DOWN");
    Collections.shuffle(menuPlaylist);

    ingamePlaylist.add("OBLITERATION");
    ingamePlaylist.add("SATIATE");
    Collections.shuffle(ingamePlaylist);
  }

  /**
   * Gets an ArrayList<String> of tracks from the Hashmap which have been added to the playlist set
   *
   * @param playlist The ENUM of the playlist wanted
   * @return An arraylist of Keys to the Hashmap of tracks.
   */
  protected ArrayList<String> getPlaylist(PLAYLIST playlist) {
    switch (playlist) {
      case MENU:
      default:
        return menuPlaylist;
      case INGAME:
        return ingamePlaylist;
    }
  }

  /**
   * Gets the full relative path of the of a track
   *
   * @param trackIndex The key of the track in the hashmap
   * @return The relative path of the track. If no track in the hashmap then null
   */
  protected String getTrackPath(String trackIndex) {
    if (tracks.containsKey(trackIndex)) {
      return filePath + File.separator + tracks.get(trackIndex);
    } else {
      return null;
    }
  }

  /**
   * Available types of playlist. Associated with an ArrayList<String> where the Sring is the Key of
   * the hashmap, in getPlayList().
   */
  public enum PLAYLIST {MENU, INGAME}
}
