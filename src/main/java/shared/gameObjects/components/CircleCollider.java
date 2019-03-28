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

  /**
   * CircleCollider constructor with DEFAULT Layer
   *
   * @param parent The object the collider is attached to
   * @param radius The radius of the circle collider projected from the centre of the object
   * @param isTrigger Whether this collider is a trigger or a collider
   */
  public CircleCollider(GameObject parent, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, isTrigger);
    this.radius = radius;
    update();
  }

  /**
   * CircleCollider constructor
   *
   * @param parent The object the collider is attached to
   * @param radius The radius of the circle collider projected from the centre of the object
   * @param layer The collision layer the collider is a part of
   * @param isTrigger Whether this collider is a trigger or a collider
   */
  public CircleCollider(GameObject parent, ColliderLayer layer, float radius, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, layer, isTrigger);
    this.radius = radius;
    update();
  }

  /**
   * CircleCollider constructor with no parent
   *
   * @param sourcePos The centre position of the circle
   * @param radius The projected radius from sourcePos
   */
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

  @Override
  public Vector2 getCentre() {
    return centre;
  }
}
