package shared.gameObjects.components;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/**
 * Constructs a Circle shaped Collider
 */
public class CircleCollider extends Collider implements Serializable {

  private float radius;
  private Vector2 centre;
  private transient Circle circle;

  public CircleCollider(GameObject parent, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, isTrigger);
    this.radius = radius;
    update();
  }

  public CircleCollider(GameObject parent, ColliderLayer layer, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, layer, isTrigger);
    this.radius = radius;
    update();
  }

  public CircleCollider(Vector2 sourcePos, float radius) {
    super(null, ColliderType.CIRCLE, false);
    this.radius = radius;
    this.centre = sourcePos;
  }

  @Override
  public void initialise(Group root) {
    circle = new Circle();
    circle.setCenterX(centre.getX());
    circle.setCenterY(centre.getY());
    circle.setRadius(radius);
    circle.setOpacity(0.5f);
    root.getChildren().add(circle);
  }

  @Override
  public void update() {
    centre =
        getParent().getTransform().getPos().add(getParent().getTransform().getSize().mult(0.5f));
    if (circle != null) {
      circle.setCenterX(centre.getX());
      circle.setCenterY(centre.getY());
      circle.setRadius(radius);
    }
  }

  // Getters

  public float getRadius() {
    return radius;
  }

  public Vector2 getCentre() {
    return centre;
  }
}
