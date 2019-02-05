package client.main;

import java.io.File;

/**
 * @author Brett Saunders
 */
public class Settings {

  private String username = "TestAccount";
  private int port = 4446;
  private double musicVolume;
  private double soundEffectVolume;
  private String mapsPath =
      "src" + File.separator + "main" + File.separator + "resources" + File.separator + "menus";
  private String menuPath =
      "src" + File.separator + "main" + File.separator + "resources" + File.separator + "menus";

  /** Default Constructor Music volume set to 100 and sound effects to 75 */
  public Settings() {
    musicVolume = 0.5f;
    soundEffectVolume = 0.75f;
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

  /** @return Current game sound effect volume */
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
}
