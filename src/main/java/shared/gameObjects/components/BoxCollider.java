package shared.gameObjects.components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/** Constructs an AABB or Squareshaped Collider */
public class BoxCollider extends Collider implements Serializable {

  private Vector2 size;
  private Vector2 centre;
  private Vector2[] corners;

  public BoxCollider(GameObject parent, boolean isTrigger) {
    super(parent, ColliderType.BOX, isTrigger);
    this.size = parent.getTransform().getSize();
    corners = new Vector2[4];
    update();
  }

  @Override
  public void update() {
    size = getParent().getTransform().getSize();
    centre = getParent().getTransform().getPos().add(size.mult(0.5f));
  }

  // Getters
  public Vector2 getSize() {
    return size;
  }

  public Vector2 getCentre() {
    return centre;
  }

  public Vector2[] getCorners() {
    return corners;
  }
}
