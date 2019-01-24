package shared.gameObjects.Components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;

/**
 * @author fxa579 Primary component responsible for collider info, such as collision state, size,
 *     shape
 */
public abstract class Collider extends Component implements Serializable {

  boolean collisionEnter, collisionExit, collisionStay, triggerEnter, triggerExit, triggerStay, trigger;


  public ColliderType getColliderType() {
    return colliderType;
  }

  ColliderType colliderType;

  Collider(GameObject parent, ColliderType colliderType, boolean trigger) {
    super(parent, ComponentType.COLLIDER);
    this.colliderType = colliderType;
    this.trigger = trigger;
  }

  public void collision() {
    if(trigger) {
      if(!triggerStay) {
        triggerEnter = triggerStay = true;
        return;
      }
      triggerEnter = false;
    }
    else {
      if(!collisionStay) {
        collisionEnter = collisionStay = true;
        return;
      }
      collisionEnter = false;
    }
  }

  public void noCollision() {
    if (trigger) {
      if(triggerStay) {
        triggerExit = true;
        triggerStay = false;
        return;
      }
      collisionExit = true;
    }
    else {
      if(collisionStay) {
        collisionExit = true;
        collisionStay = false;
        return;
      }
      collisionExit = true;
    }
  }

  //Getters

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
