package shared.gameObjects.Blocks.Wood;

import java.util.UUID;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;

public class WoodFloorObject extends GameObject {

  private int health;

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public WoodFloorObject(
      double x, double y, double sizeX, double sizeY, ObjectID id, UUID exampleUUID) {
    super(x, y, sizeX, sizeY, id, exampleUUID);
    health = 100;
    addComponent(
        new Rigidbody(
            RigidbodyType.STATIC,
            0,
            1,
            0,
            new MaterialProperty(0.8f, 1, 1),
            new AngularData(0, 0, 0, 0),
            this));
    addComponent(new BoxCollider(this, false));
  }

  // Initialise the animation
  public void initialiseAnimation() {
    this.animation.supplyAnimation("default", "images/platforms/wood/elementWood012.png");
  }

  @Override
  public void update() {
    super.update();
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  @Override
  public void render() {
    super.render();
    imageView.relocate(getX(), getY());
  }

  @Override
  public void interpolatePosition(float alpha) {
  }
}