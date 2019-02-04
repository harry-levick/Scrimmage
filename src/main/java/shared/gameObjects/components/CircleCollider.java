package shared.gameObjects.components;

import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

import java.io.Serializable;

/** Constructs a Circle shaped Collider */
public class CircleCollider extends Collider implements Serializable {

  private float radius;
  private Vector2 centre;

  public CircleCollider(GameObject parent, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, isTrigger);
    this.radius = radius;
    update();
  }

  @Override
  public void update() {
    centre = getParent().getTransform().getPos().add(radius);
  }

  // Getters

  public float getRadius() {
    return radius;
  }

  public Vector2 getCentre() {
    return centre;
  }
}
