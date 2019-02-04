package shared.gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.handlers.levelHandler.GameState;
import shared.util.maths.Vector2;

public class MapDataObject extends GameObject implements Serializable {

  private UUID mapUUID;
  private GameState gameState;
  private ArrayList<Vector2> spawnPoints;

  public MapDataObject(UUID mapUUID, GameState gameState) {
    super(0, 0, ObjectID.MapDataObject, mapUUID);
    this.mapUUID = mapUUID;
    this.gameState = gameState;
    spawnPoints = new ArrayList();
  }

  public void addSpawnPoint(double x, double y) {
    spawnPoints.add(new Vector2((float) x, (float) y));
  }

  @Override
  public void update() {

  }

  @Override
  public void render() {

  }

  @Override
  public void interpolatePosition(float alpha) {

  }

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    
  }
}
