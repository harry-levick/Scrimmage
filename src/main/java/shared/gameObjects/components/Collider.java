package shared.gameObjects.components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

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
        < circleA.getRadius() + circleB.getRadius()) return true;
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

  // Getters

  public boolean isCollisionEnter() {
    return collisionEnter;
  }

  public boolean isCollisionExit() {
    return collisionExit;
  }

  public boolean isCollisionStay() {
    return collisionStay;
  }

  public boolean isTriggerEnter() {
    return triggerEnter;
  }

  public boolean isTriggerExit() {
    return triggerExit;
  }

  public boolean isTriggerStay() {
    return triggerStay;
  }

  public boolean isTrigger() {
    return trigger;
  }
}
