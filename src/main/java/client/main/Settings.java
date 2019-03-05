package client.main;

import java.io.File;
import javafx.stage.Screen;
import shared.handlers.levelHandler.LevelHandler;

/**
 * @author Brett Saunders
 */
public class Settings {

  public static LevelHandler levelHandler;

  private String username;
  private int port;
  private double musicVolume;
  private double soundEffectVolume;
  private String s = File.separator;
  private String mapsPath;
  private String menuPath;
  private String musicPath;
  private String SFXPath;
  private int windowWidth = (int) Screen.getPrimary().getBounds().getWidth();
  private int windowHeight = (int) Screen.getPrimary().getBounds().getHeight();
  private int mapWidth;
  private int mapHeight;
  private int maxPlayers;

  /**
   * Default Constructor Music volume set to 100 and sound effects to 75
   */
  public Settings() {
    //settings that are set arbitrarily
    username = "TestAccount";
    port = 4446;
    musicVolume = 0;
    soundEffectVolume = 0;
    mapWidth = 1920;
    mapHeight = 1080;
    maxPlayers = 4;

    mapsPath = "src" + s + "main" + s + "resources" + s + "maps";
    menuPath = "src" + s + "main" + s + "resources" + s + "menus";
    musicPath = "src" + s + "main" + s + "resources" + s + "audio" + s + "music";
    SFXPath = "src" + s + "main" + s + "resources" + s + "audio" + s + "sound-effects";
  }

  public String getMapsPath() {
    return mapsPath;
  }

  public void setMapsPath(String mapsPath) {
    this.mapsPath = mapsPath;
  }

  public String getMenuPath() {
    return menuPath;
  }

  public void setMenuPath(String menuPath) {
    this.menuPath = menuPath;
  }

  public int getMapWidth() {
    return mapWidth;
  }

  public int getMapHeight() {
    return mapHeight;
  }

  public int getWindowWidth() {
    return windowWidth;
  }

  public int getWindowHeight() {
    return windowHeight;
  }

  public String getMusicPath() {
    return musicPath;
  }

  public String getSFXPath() {
    return SFXPath;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }
  /**
   * @return Current game music volume
   */
  public double getMusicVolume() {
    return musicVolume;
  }

  /**
   * Set volume level for game music with maximum of 100 and minimum of 0
   *
   * @param musicVolume Volume to set music too
   */
  public void setMusicVolume(double musicVolume) {
    this.musicVolume = musicVolume > 1.0 ? 1.0 : musicVolume;
    this.musicVolume = musicVolume < 0 ? 0 : musicVolume;
  }

  /**
   * @return Current game sound effect volume
   */
  public double getSoundEffectVolume() {
    return soundEffectVolume;
  }

  /**
   * Set volume level for game sound effects with maximum of 100 and minimum of 0
   *
   * @param soundEffectVolume Volume to set game sound effects too
   */
  public void setSoundEffectVolume(double soundEffectVolume) {
    this.soundEffectVolume = soundEffectVolume > 1.0 ? 1.0 : soundEffectVolume;
    this.soundEffectVolume = soundEffectVolume < 0 ? 0 : soundEffectVolume;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public LevelHandler getLevelHandler() {
    return levelHandler;
  }

  public void setLevelHandler(LevelHandler levelHandler) {
    Settings.levelHandler = levelHandler;
  }
}
