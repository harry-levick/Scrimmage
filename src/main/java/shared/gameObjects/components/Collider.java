package shared.gameObjects.components;

import shared.gameObjects.GameObject;
import shared.physics.data.Collision;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

import java.io.Serializable;

/**
 * @author fxa579 Primary components responsible for collider info, such as collision state, size,
 *     shape
 */
public abstract class Collider extends Component implements Serializable {

  boolean collisionEnter,
      collisionExit,
      collisionStay,
      triggerEnter,
      triggerExit,
      triggerStay,
      trigger;
  ColliderType colliderType;

  Collider(GameObject parent, ColliderType colliderType, boolean trigger) {
    super(parent, ComponentType.COLLIDER);
    this.colliderType = colliderType;
    this.trigger = trigger;
  }

  public Collision resolveCollision(BoxCollider a, Collider b) {
    switch (b.getColliderType()) {
      case BOX:
        if (boxBoxCollision(a, (BoxCollider) b)) {}

        break;
      case EDGE:
        if (boxEdgeCollision(a, (EdgeCollider) b)) {}

        break;
      case CIRCLE:
        if (boxCircleCollision(a, (CircleCollider) b)) {}

        break;
    }

    return null;
  }
  // Static Collision Methods
  public static boolean boxCircleCollision(BoxCollider box, CircleCollider circle) {
    float clampDist =
        box.getCentre()
            .sub(circle.getCentre())
            .clamp(box.getSize().mult(0.5f), box.getSize().mult(-0.5f))
            .add(box.getCentre())
            .magnitude(circle.getCentre());

    return clampDist < circle.getRadius();
  }

  public static boolean circleCircleCollision(CircleCollider circleA, CircleCollider circleB) {
    if (circleA.getCentre().magnitude(circleB.getCentre())
        < circleA.getRadius() + circleB.getRadius()) {
      return true;
    }
    return false;
  }

  public static boolean boxBoxCollision(BoxCollider boxA, BoxCollider boxB) {
    Vector2[] cornersA = boxA.getCorners();
    Vector2[] cornersB = boxB.getCorners();
    if (cornersA[3].getX() > cornersB[1].getX() && cornersA[0].getX() < cornersB[2].getX()) {
      if (cornersA[3].getY() < cornersB[1].getY() && cornersA[0].getY() < cornersB[2].getY()) {
        return true;
      }
    }
    return false;
  }

  public static boolean boxEdgeCollision(BoxCollider box, EdgeCollider edge) {
    Vector2[] corners = box.getCorners();
    for (Vector2 node : edge.getNodes()) {
      if (corners[0].getY() < node.getY()
          && corners[1].getY() > node.getY()
          && corners[0].getX() < node.getX()
          && corners[2].getX() > node.getX()) {
        return true;
      }
    }
    return false;
  }

  public static boolean circleEdgeCollision(CircleCollider circle, EdgeCollider edge) {

    return false;
  }

  public ColliderType getColliderType() {
    return colliderType;
  }

  public void collision() {
    if (trigger) {
      if (!triggerStay) {
        triggerEnter = triggerStay = true;
        return;
      }
      triggerEnter = false;
    } else {
      if (!collisionStay) {
        collisionEnter = collisionStay = true;
        return;
      }
      collisionEnter = false;
    }
  }

  public void noCollision() {
    if (trigger) {
      if (triggerStay) {
        triggerExit = true;
        triggerStay = false;
        return;
      }
      collisionExit = true;
    } else {
      if (collisionStay) {
        collisionExit = true;
        collisionStay = false;
        return;
      }
      collisionExit = true;
    }
  }

  // Getters

  public boolean onCollisionEnter() {
    return collisionEnter;
  }

  public boolean onCollisionExit() {
    return collisionExit;
  }

  public boolean onCollisionStay() {
    return collisionStay;
  }

  public boolean onTriggerEnter() {
    return triggerEnter;
  }

  public boolean onTriggerExit() {
    return triggerExit;
  }

  public boolean onTriggerStay() {
    return triggerStay;
  }

  public boolean onTrigger() {
    return trigger;
  }
}
