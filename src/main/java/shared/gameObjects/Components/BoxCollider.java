package shared.gameObjects.Components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/** Constructs an AABB or Squareshaped Collider */
public class BoxCollider extends Collider implements Serializable {
  private Vector2 size;

  public BoxCollider(GameObject parent, Vector2 size, boolean isTrigger) {
    super(parent, ColliderType.BOX, isTrigger);
    this.size = size;
  }
}
