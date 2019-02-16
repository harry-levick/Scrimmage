package shared.physics.data;

import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.EdgeCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.CollisionDirection;
import shared.util.maths.Vector2;

public class Collision {

  private Rigidbody collidedObject;
  private Vector2 normalCollision;
  private float penetrationDepth;
  private CollisionDirection direction;

  /**
   *
   */
  public Collision(Rigidbody collidedObject, CollisionDirection direction) {
    this.collidedObject = collidedObject;
    this.direction = direction;
  }

  public static Collision resolveCollision(BoxCollider a, Collider b) {
    Rigidbody collidedBody = (Rigidbody) b.getParent().getComponent(ComponentType.RIGIDBODY);
    Collision collision = null;
    switch (b.getColliderType()) {
      case BOX:
        if (Collider.boxBoxCollision(a, (BoxCollider) b)) {
          if (a.isTrigger()) {
            a.collision();
            break;
          } else {
            if (b.isTrigger()) {
              break;
            }
            collision =
                new Collision(
                    collidedBody,
                    CollisionDirection.getDirection(
                        a.getCentre().sub(((BoxCollider) b).getCentre()).normalize()));
          }
        }

        break;
      case EDGE:
        if (Collider.boxEdgeCollision(a, (EdgeCollider) b)) {
        }

        break;
      case CIRCLE:
        if (Collider.boxCircleCollision(a, (CircleCollider) b)) {
        }

        break;
    }

    return collision;
  }

  public static Collision resolveCollision(CircleCollider a, Collider b) {
    switch (b.getColliderType()) {
      case BOX:
        if (Collider.boxCircleCollision((BoxCollider) b, a)) {
        }

        break;
      case EDGE:
        break;
      case CIRCLE:
        if (Collider.circleCircleCollision(a, (CircleCollider) b)) {
        }

        break;
    }

    return null;
  }

  public Rigidbody getCollidedObject() {
    return collidedObject;
  }

  public CollisionDirection getDirection() {
    return direction;
  }

  public Vector2 getPointOfCollision() {
    return new Vector2(0, 0);
  }
  @Override
  public String toString() {
    return "COLLISION DIR = " + direction.toString();
  }
}
