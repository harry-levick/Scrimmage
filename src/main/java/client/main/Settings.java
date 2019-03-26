package client.main;

import client.handlers.accountHandler.AccountData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.text.Font;
import shared.handlers.levelHandler.LevelHandler;

/**
 * Global settings config of the game. Defines game settings and resource paths.
 */
public class Settings {

  private transient LevelHandler levelHandler;
  private transient Group gameRoot;
  private String username;
  private AccountData data;
  private int port;
  private double musicVolume;
  private double soundEffectVolume;
  private String s = File.separator;
  private String resourcesPath;
  private String mapsPath;
  private String menuPath;
  private String musicPath;
  private String SFXPath;
  private String achivementPath;
  private int windowWidth;// = (int) Screen.getPrimary().getBounds().getWidth();
  private int windowHeight;// = (int) Screen.getPrimary().getBounds().getHeight();
  private int mapWidth;
  private int mapHeight;
  private int maxPlayers;
  private String fontPath;
  private int defaultFontSize;
  private Font font;
  private int gridSize;
  private int playersDead;

  /**
   * Default Constructor Music volume set to 100 and sound effects to 75
   */
  public Settings(LevelHandler levelHandler, Group gameRoot) {
    //settings that are set arbitrarily
    username = "TestAccount";
    port = 4446;
    musicVolume = 0;
    soundEffectVolume = 0.75;
    mapWidth = 1920;
    mapHeight = 1080;
    maxPlayers = 4;
    defaultFontSize = 20;
    gridSize = 40;
    playersDead = 0;
    this.levelHandler = levelHandler;
    this.gameRoot = gameRoot;
    resourcesPath = "src" + s + "main" + s + "resources";
    mapsPath = resourcesPath + s + "maps";
    menuPath = resourcesPath + s + "menus";
    musicPath = resourcesPath + s + "audio" + s + "music";
    SFXPath = resourcesPath + s + "audio" + s + "sound-effects";
    fontPath = resourcesPath + s + "Kenney Future.ttf";
    data = new AccountData(UUID.randomUUID().toString(), "newuser", new boolean[30], new boolean[30], 1, 0);
  }

  /**
   * The path without an ending file separator to the resources
   *
   * @return The path containing File.Separator
   */
  public String getResourcesPath() {
    return resourcesPath;
  }

  /**
   * Scales a grid position to its pixel position (top left)
   * @param gridPos The grid square count
   * @return Scales grid squares to the pixels
   */
  public int getGrisPos(int gridPos) {
    return gridPos * gridSize;
  }

  /**
   * The path of the maps without an ending file separator to the resources
   * @return The relative path containing File.Separator
   */
  public String getMapsPath() {
    return mapsPath;
  }

  /**
   * Sets the full relative path of the maps directory
   * @param mapsPath The new directory of the maps
   */
  public void setMapsPath(String mapsPath) {
    this.mapsPath = mapsPath;
  }

  /**
   * The path of the menus without an ending file separator to the resources
   * @return The relative path containing File.Separator
   */
  public String getMenuPath() {
    return menuPath;
  }

  /**
   * Sets the full relative path of the menu directory
   * @param menuPath The new directory of the menus
   */
  public void setMenuPath(String menuPath) {
    this.menuPath = menuPath;
  }

  /**
   * Gets the width of the size of the map
   * @return The number of units the map is wide
   */
  public int getMapWidth() {
    return mapWidth;
  }

  /**
   * Gets the height of the size of the map
   * @return The number of units the map is high
   */
  public int getMapHeight() {
    return mapHeight;
  }

  /**
   * Gets the number of pixels of the client window
   * @return The number of pixels the window is wide
   */
  public int getWindowWidth() {
    return windowWidth;
  }

  /**
   * Gets the number of pixels of the client window
   * @return The number of pixels the window is high
   */
  public int getWindowHeight() {
    return windowHeight;
  }

  /**
   * The path of the music without an ending file separator
   * @return The relative path containing File.Separator
   */
  public String getMusicPath() {
    return musicPath;
  }

  /**
   * The path of the sound effects without an ending file separator
   * @return The relative path containing File.Separator
   */
  public String getSFXPath() {
    return SFXPath;
  }

  /**
   * The maximum number of players allowed in the game
   * @return The maximum number of players allowed in the game
   */
  public int getMaxPlayers() {
    return maxPlayers;
  }

  /**
   * The global font
   * @return The global font at the default size
   */
  public Font getFont() {
    return getFont(defaultFontSize);
  }

  /**
   * The global font
   * @param size The size of the font returned
   * @return The global font at the specified size
   */
  public Font getFont(int size) {
    try {
      font = Font
          .loadFont(new FileInputStream(
              new File(fontPath)), size);
    } catch (FileNotFoundException e) {
      font = Font.font("Consolas", 20);
    }
    return font;
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

  /**
   * Gets the username of the client's player
   * @return The name chosen by the client
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the client's player
   * @param username The new name chosen by the player
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * The port of the client/server connection
   * @return The port of the client/server connection
   */
  public int getPort() {
    return port;
  }

  /**
   * The port of the clinet/server connection
   * @param port The port of the client/server conneciton
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * The global levelhandler controlling map/level changes
   * @return Instance of LevelHandler
   */
  public LevelHandler getLevelHandler() {
    return levelHandler;
  }

  /**
   *  The global levelhandler controlling map/level changes
   * @param levelHandler Instance of LevelHandler
   */
  public void setLevelHandler(LevelHandler levelHandler) {
    this.levelHandler = levelHandler;
  }

  /**
   * The JavaFX Group which all elements are added to
   * @return The group root
   */
  public Group getGameRoot() {
    return gameRoot;
  }

  /**
   * The JavaFX Group which all elements are added to
   * @param gameRoot The group root
   */
  public void setGameRoot(Group gameRoot) {
    this.gameRoot = gameRoot;
  }

  public AccountData getData() {
    return data;
  }

  public void setData(AccountData data) {
    this.data = data;
  }

  public void playerDied() {
    this.playersDead++;
  }

  public void resetDeaths() {
    this.playersDead = 0;
  }

  public int getPlayersDead() {
    return playersDead;
  }
}
