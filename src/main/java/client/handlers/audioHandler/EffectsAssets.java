package client.handlers.audioHandler;

import client.main.Settings;
import java.io.File;
import java.util.HashMap;

/**
 * Defines all of the SOUND EFFECT assets used by AudioHandler
 */
public class EffectsAssets {

  private final HashMap<String, String> tracks = new HashMap<String, String>();
  private String filePath;
  private Settings settings;

  /**
   * Constructor:
   *
   * @param settings Main game settings, required for getting paths.
   */
  public EffectsAssets(Settings settings) {
    this.settings = settings;
    filePath = settings.getSFXPath();
    // put tracks into hashmap
    tracks.put("STEPS_1", "steps_platform.mp3");
    tracks.put("STEPS_2", "steps_platform2.mp3");
    tracks.put("MACHINEGUN", "machinegun.mp3");
    tracks.put("CLICK", "click.mp3");
    tracks.put("LASER", "laser_gun.wav");
  }

  /**
   * Gets the full relative path of the of a track
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
}
