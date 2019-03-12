package shared.gameObjects;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;

public class PlayerSpawnpoint extends GameObject {


  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
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
