package shared.physics.data;

import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.EdgeCollider;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.CollisionDirection;
import shared.util.maths.Vector2;

import javax.jnlp.DownloadService;

public class Collision {

  private Rigidbody collidedObject;
  private Vector2 normalCollision, penDepth;
  private float penetrationDepth;
  private CollisionDirection direction;

  /**
   *
   */
  public Collision(Rigidbody collidedObject, CollisionDirection direction, Vector2 depth) {
    switch (direction) {
      case DOWN:
        this.normalCollision = Vector2.Up();
        break;
      case LEFT:
        this.normalCollision = Vector2.Left();
        break;
      case RIGHT:
        this.normalCollision = Vector2.Left();
        break;
      case UP:
        this.normalCollision = Vector2.Up();
        break;
    }
    this.collidedObject = collidedObject;
    this.direction = direction;
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
            Vector2 penDepth = getPenDepth(a, (BoxCollider) b);
            collision =
                new Collision(collidedBody, getDirection(a, (BoxCollider) b), penDepth);
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

  public static Vector2 getPenDepth(BoxCollider boxA, BoxCollider boxB) {
    Vector2 toRet;

    float A = boxA.getCorners()[0].magnitude(boxB.getCentre());
    float B = boxA.getCorners()[1].magnitude(boxB.getCentre());
    float C = boxA.getCorners()[2].magnitude(boxB.getCentre());
    float D = boxA.getCorners()[3].magnitude(boxB.getCentre());
    if (A <= B && A <= C && A <= D) {
      toRet = boxA.getCorners()[0].sub(boxB.getCorners()[2]);
    }
    else if (B <= C && B <= D) {
      toRet = boxA.getCorners()[1].sub(boxB.getCorners()[3]);
    }
    else if (C <= D) {
      toRet = boxA.getCorners()[2].sub(boxB.getCorners()[0]);
    }
    else {
      toRet = boxA.getCorners()[3].sub(boxB.getCorners()[1]);
    }

    return toRet;
  }

  public static CollisionDirection getDirection(BoxCollider boxA, BoxCollider boxB) {
    Vector2 toRet;
      float A = boxA.getCorners()[0].magnitude(boxB.getCentre());
      float B = boxA.getCorners()[1].magnitude(boxB.getCentre());
      float C = boxA.getCorners()[2].magnitude(boxB.getCentre());
      float D = boxA.getCorners()[3].magnitude(boxB.getCentre());
      if (A <= B && A <= C && A <= D) {
        toRet = boxA.getCorners()[0].sub(boxB.getCorners()[2]);
        return Math.abs(toRet.getX()) < Math.abs(toRet.getY()) ? CollisionDirection.LEFT : CollisionDirection.UP;
      } else if (B <= C && B <= D) {
        toRet = boxA.getCorners()[1].sub(boxB.getCorners()[3]);
        return Math.abs(toRet.getX()) < Math.abs(toRet.getY()) ? CollisionDirection.LEFT : CollisionDirection.DOWN;
      } else if (C <= D) {
        toRet = boxA.getCorners()[2].sub(boxB.getCorners()[0]);
        return Math.abs(toRet.getX()) < Math.abs(toRet.getY()) ? CollisionDirection.RIGHT : CollisionDirection.DOWN;
      } else {
        toRet = boxA.getCorners()[3].sub(boxB.getCorners()[1]);
        return Math.abs(toRet.getX()) < Math.abs(toRet.getY()) ? CollisionDirection.RIGHT : CollisionDirection.UP;
      }
    }

  public Rigidbody getCollidedObject() {
    return collidedObject;
  }

  public CollisionDirection getDirection() {
    return direction;
  }

  public Vector2 getNormalCollision() {
    return normalCollision;
  }

  public Vector2 getPointOfCollision() {
    return new Vector2(0, 0);
  }

  public float getPenetrationDepth() {
    float toRet = 0;
    switch(direction) {
      case UP:
      case DOWN:
        toRet = penDepth.getY();
        break;
      case LEFT:
      case RIGHT:
        toRet = penDepth.getX();
        break;
    }
    return toRet;
  }
  public Vector2 getDepth() {
    return penDepth;
  }
  @Override
  public String toString() {
    return "COLLISION DIR = " + direction.toString();
  }
}
