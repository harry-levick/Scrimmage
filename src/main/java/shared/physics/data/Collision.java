package shared.physics.data;

import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.EdgeCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

//TODO: Refactor and clean up; this is used only by Raycasts now
public class Collision {

  private Rigidbody collidedObject;
  private Vector2 normalCollision;
  private float penDepth;

  /**
   *
   */
  public Collision(Rigidbody collidedObject, Vector2 normal, float depth) {
    this.collidedObject = collidedObject;
    this.normalCollision = normal;
    this.penDepth = depth;
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
            float penDepth = getPenDepth(a, (BoxCollider) b);
            collision = new Collision(collidedBody, getDirection(a, (BoxCollider) b), penDepth);
          }
        }

        break;
      case EDGE:
        if (Collider.boxEdgeCollision(a, (EdgeCollider) b)) {}

        break;
      case CIRCLE:
        if (Collider.boxCircleCollision(a, (CircleCollider) b)) {}

        break;
    }

    return collision;
  }

  public static boolean haveCollided(Collider colA, Collider colB) {
    if (colA == colB) {
      return false;
    }
    boolean toRet = false;
    switch (colA.getColliderType()) {
      case BOX:
        switch (colB.getColliderType()) {
          case BOX:
            toRet = Collider.boxBoxCollision((BoxCollider) colA, (BoxCollider) colB);
            break;
        }
        break;
      case CIRCLE:
        switch (colB.getColliderType()) {
        }
        break;
      case EDGE:
        switch (colB.getColliderType()) {
        }
        break;
    }
    return toRet;
  }

  public static Collision resolveCollision(CircleCollider a, Collider b) {
    switch (b.getColliderType()) {
      case BOX:
        if (Collider.boxCircleCollision((BoxCollider) b, a)) {}

        break;
      case EDGE:
        break;
      case CIRCLE:
        if (Collider.circleCircleCollision(a, (CircleCollider) b)) {}

        break;
    }

    return null;
  }

  public static float getPenDepth(BoxCollider boxA, BoxCollider boxB) {
    Vector2 n = boxB.getCentre().sub(boxA.getCentre());
    float x_overlap =
        boxA.getSize().getX() * 0.5f + boxB.getSize().getX() * 0.5f - Math.abs(n.getX());
    float y_overlap =
        boxA.getSize().getY() * 0.5f + boxB.getSize().getY() * 0.5f - Math.abs(n.getY());

    Vector2 penetrationDistance = new Vector2(x_overlap, y_overlap);
    if (penetrationDistance.getX() < penetrationDistance.getY()) {
      return x_overlap;
    }
    else {
      return y_overlap;
    }
  }

  public static Vector2 getDirection(BoxCollider boxA, BoxCollider boxB) {
    Vector2 n = boxB.getCentre().sub(boxA.getCentre());
    Rigidbody bodyB = (Rigidbody) boxB.getParent().getComponent(ComponentType.RIGIDBODY);
    float x_overlap =
        boxA.getSize().getX() * 0.5f + boxB.getSize().getX() * 0.5f - Math.abs(n.getX());
    float y_overlap =
        boxA.getSize().getY() * 0.5f + boxB.getSize().getY() * 0.5f - Math.abs(n.getY());

    Vector2 penetrationDistance = new Vector2(x_overlap, y_overlap);
    if (penetrationDistance.getX() < penetrationDistance.getY()) {
      if (n.getX() < 0) {
        return bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Right() : Vector2.Left();
      } else {
        return bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Left() : Vector2.Zero();
      }
    } else {
      if (n.getY() < 0) {
        return bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Down() : Vector2.Up();
      } else {
        return bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Up() : Vector2.Down();
      }
    }
  }

  public Rigidbody getCollidedObject() {
    return collidedObject;
  }


  public Vector2 getNormalCollision() {
    return normalCollision;
  }

  public Vector2 getPointOfCollision() {
    return new Vector2(0, 0);
  }

  public float getPenetrationDepth() {
    return penDepth;
  }

}
