package shared.gameObjects.components;

import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

import java.io.Serializable;

/** Constructs an AABB or Squareshaped Collider */
public class BoxCollider extends Collider implements Serializable {

  private Vector2 size;
  private Vector2 centre;
  private Vector2[] corners;

  public BoxCollider(GameObject parent, Vector2 size, boolean isTrigger) {
    super(parent, ColliderType.BOX, isTrigger);
    this.size = size;
    corners = new Vector2[4];
    update();
  }

  @Override
  public void update() {
    centre = getParent().getTransform().getPos().add(size.mult(0.5f));
    corners[0] = getParent().getTransform().getPos();
    corners[1] = getParent().getTransform().getPos().add(Vector2.Up().mult(size.getY()));
    corners[2] = getParent().getTransform().getPos().add(size);
    corners[3] = getParent().getTransform().getPos().add(Vector2.Right().mult(size.getX()));
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
