package client.main;

/** @author Brett Saunders */
public class Settings {

  private int musicVolume;
  private int soundEffectVolume;
  private String mapsPath =
      "C:\\Users\\brett\\projects\\aloneinthedark\\src\\main\\resources\\menus";
  private String menuPath =
      "C:\\Users\\brett\\projects\\aloneinthedark\\src\\main\\resources\\menus";

  /** Default Constructor Music volume set to 100 and sound effects to 75 */
  public Settings() {
    musicVolume = 100;
    soundEffectVolume = 75;
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

  /** @return Current game music volume */
  public int getMusicVolume() {
    return musicVolume;
  }

  /**
   * Set volume level for game music with maximum of 100 and minimum of 0
   *
   * @param musicVolume Volume to set music too
   */
  public void setMusicVolume(int musicVolume) {
    this.musicVolume = musicVolume > 100 ? 100 : musicVolume;
    this.musicVolume = musicVolume < 0 ? 0 : musicVolume;
  }

  /** @return Current game sound effect volume */
  public int getSoundEffectVolume() {
    return soundEffectVolume;
  }

  /**
   * Set volume level for game sound effects with maximum of 100 and minimum of 0
   *
   * @param soundEffectVolume Volume to set game sound effects too
   */
  public void setSoundEffectVolume(int soundEffectVolume) {
    this.soundEffectVolume = soundEffectVolume > 100 ? 100 : soundEffectVolume;
    this.soundEffectVolume = soundEffectVolume < 0 ? 0 : soundEffectVolume;
  }
}
