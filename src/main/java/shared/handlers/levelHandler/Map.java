package shared.handlers.levelHandler;

import javafx.scene.image.Image;

/**
 * Container class for the game's map file
 */
public class Map {

  private String name;
  private Image mapIcon;
  private String playlist;
  private String path;
  private GameState gameState;

  /**
   * Constructs map from a file
   *
   * @param name Name of the map file in the directory
   * @param path File path to the directory containing the map file
   */
  public Map(String name, String path) {
    this.name = name;
    this.playlist = "N/A";
    this.path = path;
    this.gameState = gameState;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setGameState(GameState state) {
    this.gameState = state;
  }

  public Image getMapIcon() {

    return mapIcon;
  }

  public String getPlaylist() {
    return playlist;
  }
}
