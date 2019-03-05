package shared.gameObjects.components;

import java.io.Serializable;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderLayer;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/**
 * An edge collider; used only for Raycasts
 */
public class EdgeCollider extends Collider implements Serializable {

  ArrayList<Vector2> nodes;

  public EdgeCollider(boolean isTrigger) {
    super(null, ColliderType.EDGE, isTrigger);
    nodes = new ArrayList<>();
  }

  public EdgeCollider(GameObject parent, ColliderLayer layer, boolean isTrigger) {
    super(parent, ColliderType.EDGE, layer, isTrigger);
    nodes = new ArrayList<>();
  }

  public void addNode(Vector2 position) {
    nodes.add(position);
  }

  public ArrayList<Vector2> getNodes() {
    return nodes;
  }

  public Vector2 findClosestPoint(Vector2 point) {
    Vector2 toRet = nodes.get(0);
    for (Vector2 vec : nodes) {
      toRet = vec.magnitude(point) <= toRet.magnitude() ? vec : toRet;
    }
    return toRet;
  }
}
