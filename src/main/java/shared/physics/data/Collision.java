package shared.physics.data;

import shared.gameObjects.GameObject;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.EdgeCollider;
import shared.util.maths.Vector2;

/**
 * Contains data on a collision with another object
 */
public class Collision {

  private GameObject collidedObject;
  private Vector2 normalCollision;
  private float penDepth;
  private Vector2 pointOfCollision;
  private boolean collided;

  /**
   * Container for collision class; holds data on the object collided with only
   * @param collidedObject The object collided with
   * @param colA The collider doing the colliding
   * @param colB The collider attached to the collidedObject
   */
  public Collision(GameObject collidedObject, Collider colA, Collider colB) {
    this.collidedObject = collidedObject;
    resolveCollision(colA, colB);
  }

  private void resolveCollision(Collider a, Collider b) {
    if (!(Collider.haveCollided(a, b))) {
      return;
    }
    collided = true;
    switch (a.getColliderType()) {
      case BOX:
        switch (b.getColliderType()) {
          case BOX:
            calculateData((BoxCollider) a, (BoxCollider) b);
            break;
          case CIRCLE:
            calculateData((BoxCollider) a, (CircleCollider) b);
            break;
        }
        break;
      case CIRCLE:
        switch (b.getColliderType()) {
          case BOX:
            calculateData((BoxCollider) b, (CircleCollider) a);
            break;
          case CIRCLE:
            calculateData((CircleCollider) a, (CircleCollider) b);
            break;
        }
        break;
      case EDGE:
        switch (b.getColliderType()) {
          case BOX:
            calculateData(
                ((EdgeCollider) a).findClosestPoint(((BoxCollider) b).getCentre()),
                (BoxCollider) b);
            break;
          case CIRCLE:
            calculateData(
                ((EdgeCollider) a).findClosestPoint(((CircleCollider) b).getCentre()),
                (CircleCollider) b);
            break;
        }
        break;
    }
  }

  //Assumes the boxes are not rotated
  private void calculateData(BoxCollider a, BoxCollider b) {
    Vector2 n = b.getCentre().sub(a.getCentre());
    float x_overlap = a.getSize().getX() * 0.5f + b.getSize().getX() * 0.5f - Math.abs(n.getX());
    float y_overlap = a.getSize().getY() * 0.5f + b.getSize().getY() * 0.5f - Math.abs(n.getY());

    Vector2 penetrationDistance = new Vector2(x_overlap, y_overlap);
    if (penetrationDistance.getX() < penetrationDistance.getY()) {
      if (n.getX() < 0) {
        normalCollision = Vector2.Left();
      } else {
        normalCollision = Vector2.Right();
      }
      penDepth = x_overlap;
    } else {
      if (n.getY() < 0) {
        normalCollision = Vector2.Up();
      } else {
        normalCollision = Vector2.Down();
      }
      penDepth = y_overlap;
    }
    pointOfCollision = b.getCentre().sub(normalCollision.mult(penDepth));
  }

  private void calculateData(BoxCollider a, CircleCollider b) {
    Vector2 n = b.getCentre().sub(b.getCentre());
    Vector2 extents = a.getSize().mult(0.5f);
    Vector2 closestPoint = n.clamp(extents.mult(-1), extents);
    boolean inside = false;

    if (n.equals(closestPoint)) {
      inside = true;
      if (Math.abs(n.getX()) > Math.abs(n.getY())) {
        closestPoint =
            new Vector2(
                closestPoint.getX() > 0 ? extents.getX() : extents.getX() * -1,
                closestPoint.getY());
      } else {
        closestPoint =
            new Vector2(
                closestPoint.getX(),
                closestPoint.getY() < 0 ? extents.getY() : extents.getY() * -1);
      }
    }

    Vector2 normal = n.sub(closestPoint);
    float d = normal.magnitude();
    if (inside) {
      normalCollision = n.mult(-1);
    } else {
      normalCollision = n;
    }
    penDepth = b.getRadius() - d;
    pointOfCollision = b.getCentre().add(penDepth);
  }

  private void calculateData(CircleCollider a, CircleCollider b) {
    normalCollision = b.getCentre().sub(a.getCentre());
    penDepth = a.getRadius() - normalCollision.magnitude();
    pointOfCollision = b.getCentre().add(penDepth);
  }

  private void calculateData(Vector2 point, BoxCollider b) {
    normalCollision = b.getCentre().sub(point);
    penDepth = normalCollision.magnitude();
    pointOfCollision = b.getCentre().add(penDepth);
  }

  private void calculateData(Vector2 point, CircleCollider b) {
    normalCollision = b.getCentre().sub(point);
    penDepth = normalCollision.magnitude();
    pointOfCollision = b.getCentre().add(penDepth);
  }

  public GameObject getCollidedObject() {
    return collidedObject;
  }

  public Vector2 getNormalCollision() {
    return normalCollision;
  }

  public Vector2 getPointOfCollision() {
    return pointOfCollision;
  }

  public boolean isCollided() {
    return collided;
  }
}
