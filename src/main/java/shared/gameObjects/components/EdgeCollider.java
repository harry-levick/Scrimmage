package shared.gameObjects.components;

import java.io.Serializable;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/** An edge collider; used only for Raycasts */
public class EdgeCollider extends Collider {

  ArrayList<Vector2> nodes;

  /**
   * EdgeCollider constructor with DEFAULT Layer and no parent
   * @param isTrigger Whether this collider is a trigger or a collider
   */
  public EdgeCollider(boolean isTrigger) {
    super(null, ColliderType.EDGE, isTrigger);
    nodes = new ArrayList<>();
  }

  /**
   * EdgeCollider constructor
   * @param parent The object the collider is attached to
   * @param layer The collision layer the collider is a part of
   * @param isTrigger Whether this collider is a trigger or a collider
   */
  public EdgeCollider(GameObject parent, ColliderLayer layer, boolean isTrigger) {
    super(parent, ColliderType.EDGE, layer, isTrigger);
    nodes = new ArrayList<>();
  }

  /**
   * Adds a new node on the EdgeCollider
   * @param position
   */
  public void addNode(Vector2 position) {
    nodes.add(position);
  }

  public ArrayList<Vector2> getNodes() {
    return nodes;
  }

  /**
   * Find the node on the edge collider that is closest to a point passed in
   * @param point
   * @return
   */
  public Vector2 findClosestPoint(Vector2 point) {

    Vector2 toRet = nodes.get(0);
    for (Vector2 vec : nodes) {
      toRet = vec.magnitude(point) <= toRet.magnitude(point) ? vec : toRet;
    }
    return toRet;
  }

  @Override
  public Vector2 getCentre() {
    return nodes.get(nodes.size()/2);
  }
}
