package shared.gameObjects.components;

import java.io.Serializable;
import javafx.scene.Group;
import shared.gameObjects.GameObject;
import shared.physics.Physics;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/**
 * @author fxa579 Primary components responsible for collider info, such as collision state, size,
 *     shape
 */
public abstract class Collider extends Component implements Serializable {

  boolean trigger;
  ColliderType colliderType;
  ColliderLayer layer;

  /**
   * Collider constructor with DEFAULT Layer
   * @param parent The object the collider is attached to
   * @param colliderType The type of collider
   * @param trigger Whether this collider is a trigger or a collider
   */
  Collider(GameObject parent, ColliderType colliderType, boolean trigger) {
    super(parent, ComponentType.COLLIDER);
    this.colliderType = colliderType;
    this.trigger = trigger;
    this.layer = ColliderLayer.DEFAULT;
  }

  /**
   * Collider Contructor
   * @param parent The object the collider is attached to
   * @param colliderType The type of collider
   * @param layer The collision layer the collider is a part of
   * @param trigger Whether this collider is a trigger or a collider
   */
  Collider(GameObject parent, ColliderType colliderType, ColliderLayer layer, boolean trigger) {
    super(parent, ComponentType.COLLIDER);
    this.colliderType = colliderType;
    this.trigger = trigger;
    this.layer = layer;
  }

  // Static Collision Methods

  /**
   * Tests for a collision between a BoxCollider and a CircleCollider
   * @param box BoxCollider to test
   * @param circle CircleCollider to test
   * @return True if the colliders intersect
   */
  private static boolean boxCircleCollision(BoxCollider box, CircleCollider circle) {
    Vector2 n = box.getCentre().sub(circle.getCentre());
    Vector2 closestPoint = n.add(0);
    Vector2 extents = box.getSize().mult(0.5f);
    closestPoint = closestPoint.clamp(extents.mult(-1), extents);
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
    return !(d > circle.getRadius() && !inside);
  }

  /**
   * Tests for a collision between a CircleCollider and a CircleCollider
   * @param circleA CircleCollider to test
   * @param circleB CircleCollider to test
   * @return True if the colliders intersect
   */
  private static boolean circleCircleCollision(CircleCollider circleA, CircleCollider circleB) {
    if (circleA.getCentre().magnitude(circleB.getCentre())
        < circleA.getRadius() + circleB.getRadius()) {
      return true;
    }
    return false;
  }

  /**
   * Tests for a collision between a BoxCollider and a BoxCollider
   * @param boxA BoxCollider to test
   * @param boxB BoxCollider to test
   * @return True if the colliders intersect
   */
  private static boolean boxBoxCollision(BoxCollider boxA, BoxCollider boxB) {
    for (Vector2 axisOfProjection : boxA.getAxes()) {
      Vector2 pA = projectToAxis(boxA, axisOfProjection);
      Vector2 pB = projectToAxis(boxB, axisOfProjection);
      if (!pA.canOverlap(pB)) {
        return false;
      }
    }
    for (Vector2 axisOfProjection : boxB.getAxes()) {
      Vector2 pA = projectToAxis(boxA, axisOfProjection);
      Vector2 pB = projectToAxis(boxB, axisOfProjection);
      if (!pA.canOverlap(pB)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Projects a BoxCollider 2D shape to a given 1D axis
   * @param a BoxCollider to project
   * @param axis axis to project to
   * @return the segment the BoxCollider exists on in the axis
   */
  private static Vector2 projectToAxis(BoxCollider a, Vector2 axis) {
    // Project the shapes along the axis
    float min = axis.dot(a.getCorners()[0]); // Get the first min
    double max = min;
    for (int i = 1; i < a.getCorners().length; i++) {
      float temp = axis.dot(a.getCorners()[i]); // Get the dot product between the axis and the node
      if (temp < min) {
        min = temp;
      } else if (temp > max) {
        max = temp;
      }
    }
    return new Vector2(min, max);
  }
  //TODO Comments
  private static boolean pointBoxCollision(EdgeCollider edgeA, BoxCollider boxB) {
    for (Vector2 node : edgeA.getNodes()) {
      if (node.getX() >= boxB.getCorners()[0].getX()
          && node.getX() <= boxB.getCorners()[3].getX()
          && node.getY() >= boxB.getCorners()[0].getY()
          && node.getY() <= boxB.getCorners()[2].getY()) return true;
    }
    return false;
  }

  private static boolean pointCircleCollision(Vector2 pointA, CircleCollider circB) {
    return circB.getRadius() >= pointA.magnitude(circB.getCentre());
  }

  public static boolean canCollideWithLayer(ColliderLayer a, ColliderLayer b) {
    return Physics.COLLISION_LAYERS[a.toInt()][b.toInt()]
        && Physics.COLLISION_LAYERS[b.toInt()][a.toInt()];
  }

  /**
   * Tests for a collision between two colliders
   * @param colA Collider to test
   * @param colB Collider to test
   * @return True if the colliders intersect
   */
  public static boolean haveCollided(Collider colA, Collider colB) {
    if (colA == colB
        || !(canCollideWithLayer(colA.getLayer(), colB.getLayer()))
        || colB.isTrigger()) {
      return false;
    }
    boolean toRet = false;
    switch (colA.getColliderType()) {
      case BOX:
        switch (colB.getColliderType()) {
          case BOX:
            toRet = boxBoxCollision((BoxCollider) colA, (BoxCollider) colB);
            break;
          case CIRCLE:
            toRet = boxCircleCollision((BoxCollider) colA, (CircleCollider) colB);
            break;
        }
        break;
      case CIRCLE:
        switch (colB.getColliderType()) {
          case CIRCLE:
            toRet = circleCircleCollision((CircleCollider) colA, (CircleCollider) colB);
            break;
          case BOX:
            toRet = boxCircleCollision((BoxCollider) colB, (CircleCollider) colA);
            break;
        }
        break;
      case EDGE:
        switch (colB.getColliderType()) {
          case BOX:
            toRet =
                pointBoxCollision(
                    (EdgeCollider) colA,
                    (BoxCollider) colB);
            break;
          case CIRCLE:
            toRet =
                pointCircleCollision(
                    ((EdgeCollider) colA).findClosestPoint(((CircleCollider) colB).getCentre()),
                    (CircleCollider) colB);
            break;
        }
        break;
    }
    return toRet;
  }

  public ColliderType getColliderType() {
    return colliderType;
  }

  public void initialise(Group root) {}
  public abstract Vector2 getCentre();
  // Getters

  public ColliderLayer getLayer() {
    return layer;
  }

  public void setLayer(ColliderLayer layer) {
    this.layer = layer;
  }

  public boolean isTrigger() {
    return trigger;
  }
}
