package shared.gameObjects.components;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/** Constructs an AABB Collider */
public class BoxCollider extends Collider implements Serializable {

  private Vector2 size;
  private Vector2 centre;
  private Vector2[] corners;
  private float rotation;
  private Double[] polygonCoordinates;

  private transient Polygon polygon;
  private Vector2[] axes;

  /**
   * BoxCollider constructor with DEFAULT Layer. Maintains same size as parent
   * @param parent The object the collider is attached to
   * @param isTrigger Whether this collider is a trigger or a collider
   */
  public BoxCollider(GameObject parent, boolean isTrigger) {
    super(parent, ColliderType.BOX, isTrigger);
    rotation = getParent().getTransform().getRot();
    corners = new Vector2[4];
    axes = new Vector2[2];
    polygonCoordinates = new Double[8];
    update();
  }

  /**
   * BoxCollider constructor. Maintains same size as parent
   * @param parent The object the collider is attached to
   * @param layer The collision layer the collider is a part of
   * @param isTrigger Whether this collider is a trigger or a collider
   */

  public BoxCollider(GameObject parent, ColliderLayer layer, boolean isTrigger) {
    super(parent, ColliderType.BOX, layer, isTrigger);
    rotation = getParent().getTransform().getRot();
    corners = new Vector2[4];
    axes = new Vector2[2];
    polygonCoordinates = new Double[8];
    update();
  }

  /**
   * BoxCollider constructor with no parent.
   * @param sourcePos The top-left position from which the collider is sourced at
   * @param size Size of the Collider
   */
  public BoxCollider(Vector2 sourcePos, Vector2 size) {
    super(null, ColliderType.BOX, false);
    this.size = size;
    rotation = 0f;
    centre = sourcePos.add(size.mult(0.25f));
    corners = new Vector2[4];
    axes = new Vector2[2];
    polygonCoordinates = new Double[8];

    corners[0] = sourcePos.applyRotation(rotation);
    corners[1] = sourcePos.add(Vector2.Down().mult(size)).applyRotation(rotation);
    corners[2] = sourcePos.add(size).applyRotation(rotation);
    corners[3] = sourcePos.add(Vector2.Right().mult(size)).applyRotation(rotation);

    axes[0] = corners[0].sub(corners[3]).normal().normalize();
    axes[1] = corners[0].sub(corners[1]).normal().normalize();

    polygonCoordinates[0] = (double) corners[0].getX();
    polygonCoordinates[1] = (double) corners[0].getY();
    polygonCoordinates[2] = (double) corners[1].getX();
    polygonCoordinates[3] = (double) corners[1].getY();
    polygonCoordinates[4] = (double) corners[2].getX();
    polygonCoordinates[5] = (double) corners[2].getY();
    polygonCoordinates[6] = (double) corners[3].getX();
    polygonCoordinates[7] = (double) corners[3].getY();
  }

  @Override
  public void initialise(Group root) {
    polygon = new Polygon();
    polygon.getPoints().addAll(polygonCoordinates);
    polygon.setOpacity(0.5);
    root.getChildren().add(polygon);
  }

  @Override
  public void update() {
    size = getParent().getTransform().getSize();
    centre = getParent().getTransform().getPos().add(size.mult(0.5f));
    rotation = getParent().getTransform().getRot();
    Vector2 sourcePos = getParent().getTransform().getSize().div(-2);

    corners[0] = sourcePos.applyRotation(rotation).add(centre);
    corners[1] = sourcePos.add(Vector2.Down().mult(size)).applyRotation(rotation).add(centre);
    corners[2] = sourcePos.add(size).applyRotation(rotation).add(centre);
    corners[3] = sourcePos.add(Vector2.Right().mult(size)).applyRotation(rotation).add(centre);

    axes[0] = corners[0].sub(corners[3]).normal().normalize();
    axes[1] = corners[0].sub(corners[1]).normal().normalize();

    if (polygon != null) {
      polygon.getPoints().clear();
      polygonCoordinates[0] = (double) corners[0].getX();
      polygonCoordinates[1] = (double) corners[0].getY();
      polygonCoordinates[2] = (double) corners[1].getX();
      polygonCoordinates[3] = (double) corners[1].getY();
      polygonCoordinates[4] = (double) corners[2].getX();
      polygonCoordinates[5] = (double) corners[2].getY();
      polygonCoordinates[6] = (double) corners[3].getX();
      polygonCoordinates[7] = (double) corners[3].getY();
      polygon.getPoints().addAll(polygonCoordinates);
    }
  }

  // Getters
  public Vector2 getSize() {
    return size;
  }

  @Override
  public Vector2 getCentre() {
    return centre;
  }

  public Vector2[] getCorners() {
    return corners;
  }

  public Vector2[] getAxes() {
    return axes;
  }
}
