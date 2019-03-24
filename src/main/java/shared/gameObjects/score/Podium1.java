package shared.gameObjects.score;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.ColliderLayer;

public class Podium1 extends GameObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   */
  public Podium1(
      double x, double y, UUID exampleUUID) {
    super(x, y, 310, 543, ObjectType.Podium, exampleUUID);
    addComponent(new BoxCollider(this, ColliderLayer.PLATFORM, false));
    addComponent(new Rigidbody(0, this));
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/scoreScreen/Podium1.png");
  }

}

