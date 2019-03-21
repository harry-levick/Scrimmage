package shared.gameObjects;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;

/**
 * Container class for the position of a Player's Spawn
 */
public class PlayerSpawnpoint extends GameObject {


  /**
   * Constructs player spawnpoint
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   */
  public PlayerSpawnpoint(double x, double y, UUID exampleUUID) {
    super(x, y, 80, 110, ObjectType.Player, exampleUUID);
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/player_spawnpoint.png");
  }

}
