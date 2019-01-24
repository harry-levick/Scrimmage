package shared.gameObjects;

import java.io.Serializable;
import java.util.UUID;
import shared.handlers.levelHandler.GameState;

public class MapDataObject implements Serializable {

  private UUID mapUUID;
  private GameState gameState;

  public MapDataObject(UUID mapUUID, GameState gameState) {
    this.mapUUID = mapUUID;
    this.gameState = gameState;
  }
}
