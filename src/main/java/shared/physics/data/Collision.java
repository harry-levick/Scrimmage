package shared.physics.data;

import shared.gameObjects.components.Rigidbody;
import shared.util.maths.Vector2;

public class Collision {
  private Rigidbody collidedObject;
  private Vector2 pointOfCollision;

  public Collision(Rigidbody collidedObject, Vector2 pointOfCollision) {
    this.collidedObject = collidedObject;
    this.pointOfCollision = pointOfCollision;

  }
}
