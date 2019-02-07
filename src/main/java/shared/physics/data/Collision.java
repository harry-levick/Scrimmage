package shared.physics.data;

import shared.gameObjects.components.Rigidbody;
import shared.physics.types.CollisionDirection;
import shared.util.maths.Vector2;

public class Collision {

  private Rigidbody collidedObject;
  private Vector2 pointOfCollision;
  private CollisionDirection direction;

  /**
   * @param collidedObject
   * @param direction
   */
  public Collision(Rigidbody collidedObject, CollisionDirection direction) {
    this.collidedObject = collidedObject;
    this.direction = direction;
  }

  public Rigidbody getCollidedObject() {
    return collidedObject;
  }

  public Vector2 getPointOfCollision() {
    return pointOfCollision;
  }

  public CollisionDirection getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return "COLLISION DIR = " + direction.toString();
  }
}
