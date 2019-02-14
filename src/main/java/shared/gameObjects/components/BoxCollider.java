package shared.gameObjects.components;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/**
 * Constructs an AABB or Squareshaped Collider
 */
public class BoxCollider extends Collider implements Serializable {

  private Vector2 size;
  private Vector2 centre;
  private Vector2[] corners;

  private transient Rectangle polygon;

  public BoxCollider(GameObject parent, boolean isTrigger) {
    super(parent, ColliderType.BOX, isTrigger);
    corners = new Vector2[4];
    update();
  }

  public BoxCollider(Vector2 sourcePos, Vector2 size) {
    super(null, ColliderType.BOX, false);
    this.size = size;
    centre = sourcePos.add(size.mult(0.25f));
    corners = new Vector2[4];

    corners[0] = sourcePos;
    corners[1] = sourcePos.add(Vector2.Up().mult(size));
    corners[2] = sourcePos.add(size);
    corners[3] = sourcePos.add(Vector2.Right().mult(size));
  }

  public void initialise(Group root) {
    polygon = new Rectangle();

    polygon.setX(getParent().getX());
    polygon.setY(getParent().getY());
    polygon.setWidth(size.getX());
    polygon.setHeight(size.getY());

    polygon.setOpacity(0.5);
    root.getChildren().add(polygon);
  }

  @Override
  public void update() {
    size = getParent().getTransform().getSize();
    centre = getParent().getTransform().getPos().add(size.mult(0.5f));

    corners[0] = getParent().getTransform().getPos();
    corners[1] = getParent().getTransform().getPos().add(Vector2.Up().mult(size));
    corners[2] = getParent().getTransform().getBotPos();
    corners[3] = getParent().getTransform().getPos().add(Vector2.Right().mult(size));

    if (polygon != null) {
      polygon.setX(getParent().getX());
      polygon.setY(getParent().getY());
      polygon.setWidth(size.getX());
      polygon.setHeight(size.getY());
    }

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
