package client.handlers.audioHandler;

import client.main.Client;
import java.io.File;
import java.util.HashMap;

public class EffectsAssets {

  private final HashMap<String, String> tracks = new HashMap<String, String>();
  private String filePath = Client.settings.getSFXPath();

  public EffectsAssets() {
    // put tracks into hashmap
    tracks.put("CHOOSE_YOUR_CHARACTER", "choose_your_character.mp3");
    tracks.put("STEPS_1", "steps_platform.mp3");
    tracks.put("STEPS_2", "steps_platform2.mp3");
    tracks.put("MACHINEGUN", "machinegun.mp3");
    tracks.put("CLICK", "click.mp3");
  }

  protected String getTrackPath(String trackIndex) {
    if (tracks.containsKey(trackIndex)) {
      return filePath + File.separator + tracks.get(trackIndex);
    } else {
      return null;
    }
  }
}
