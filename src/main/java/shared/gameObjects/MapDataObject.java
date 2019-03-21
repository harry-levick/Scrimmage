package shared.gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.background.Background;
import shared.handlers.levelHandler.GameState;
import shared.util.maths.Vector2;

/**
 * Game object that stores the back end data of a map
 */
public class MapDataObject extends GameObject implements Serializable {

  private UUID mapUUID;
  private GameState gameState;
  private ArrayList<Vector2> spawnPoints;
  private Background background;

  /**
   * Constructs a Map Data container
   * @param mapUUID UUID of object
   * @param gameState Current game state
   */
  public MapDataObject(UUID mapUUID, GameState gameState) {
    super(0, 0, 100, 100, ObjectType.MapDataObject, mapUUID);
    this.mapUUID = mapUUID;
    this.gameState = gameState;
    spawnPoints = new ArrayList();
  }

  public void addSpawnPoint(double x, double y) {
    spawnPoints.add(new Vector2((float) x, (float) y));
  }

  public ArrayList<Vector2> getSpawnPoints() {
    return spawnPoints;
  }

  public void setSpawnPoints(ArrayList<Vector2> spawnPoints) {
    this.spawnPoints = spawnPoints;
  }

  public Background getBackground() {
    return background;
  }

  public void setBackground(Background background) {
    this.background = background;
  }

  public GameState getGameState() {
    return gameState;
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
  }

  @Override
  public void initialiseAnimation() {
  }
}
