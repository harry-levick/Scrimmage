package client.handlers.audioHandler;

import java.io.File;
import java.util.HashMap;

public class EffectsAssets {

  private String filePath =
      "src"
          + File.separator
          + "main"
          + File.separator
          + "resources"
          + File.separator
          + "audio"
          + File.separator
          + "sound-effects";

  private final HashMap<String, String> tracks = new HashMap<String, String>();

  public EffectsAssets() {
    // put tracks into hashmap
    tracks.put("CHOOSE_YOUR_CHARACTER", "choose_your_character.mp3");
  }

  protected String getTrackPath(String trackIndex) {
    if (tracks.containsKey(trackIndex)) {
      return filePath + File.separator + tracks.get(trackIndex);
    } else {
      return null;
    }
  }
}
