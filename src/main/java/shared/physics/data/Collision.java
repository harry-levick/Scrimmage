package shared.physics.data;

import shared.gameObjects.components.Rigidbody;
import shared.physics.types.CollisionDirection;
import shared.util.maths.Vector2;

public class Collision {

  private Rigidbody collidedObject;
  private Vector2 pointOfCollision;
  private CollisionDirection direction;

  public Collision(
      Rigidbody collidedObject, Vector2 pointOfCollision, CollisionDirection direction) {
    this.collidedObject = collidedObject;
    this.pointOfCollision = pointOfCollision;
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
}
