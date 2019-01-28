package client.handlers.audioHandler;

import java.io.File;

public class MusicAssets {

  private String filePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "audio" + File.separator + "music";

  private final String FUNK_GAME_LOOP = "funk-game-loop-by-kevin-macleod.mp3";

  protected String getFUNK_GAME_LOOP(){
    return filePath + File.separator + FUNK_GAME_LOOP;
  }
}
