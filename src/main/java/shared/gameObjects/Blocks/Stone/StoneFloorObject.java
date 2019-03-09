package shared.gameObjects.Blocks.Stone;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.ColliderLayer;
import shared.physics.types.RigidbodyType;

public class StoneFloorObject extends GameObject {


  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public StoneFloorObject(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    addComponent(
        new Rigidbody(
            RigidbodyType.STATIC,
            0,
            1,
            0,
            new MaterialProperty(0f, 0.2f, 0.1f),
            new AngularData(0, 0, 0, 0),
            this));
    addComponent(new BoxCollider(this, ColliderLayer.PLATFORM,false));
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/platforms/stone/elementStone013.png");
  }


}
