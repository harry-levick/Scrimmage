package shared.gameObjects.components;

import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

/** An edge collider, primarily for use with static obbjects (eg. floor) */
public class EdgeCollider extends Collider implements Serializable {

  ArrayList<Vector2> nodes;

  public EdgeCollider(GameObject parent, boolean isTrigger) {
    super(parent, ColliderType.EDGE, isTrigger);
    nodes = new ArrayList<>();
  }

  public void addNode(Vector2 position) {
    nodes.add(position);
  }

  public ArrayList<Vector2> getNodes() {
    return nodes;
  }
}
