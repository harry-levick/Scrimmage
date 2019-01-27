package shared.gameObjects.Components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;

/**
 * Constructs a Circle shaped Collider
 */
public class CircleCollider extends Collider implements Serializable {

  float radius;

  public CircleCollider(GameObject parent, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, isTrigger);
    this.radius = radius;
  }
}
