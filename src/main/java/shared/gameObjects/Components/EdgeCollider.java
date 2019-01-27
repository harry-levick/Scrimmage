package shared.gameObjects.Components;

import java.io.Serializable;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.physics.types.ColliderType;
import shared.util.maths.Vector2;

/**
 * An edge collider, primarily for use with static obbjects (eg. floor)
 */
public class EdgeCollider extends Collider implements Serializable {

  ArrayList<Vector2> nodes;

  public EdgeCollider(GameObject parent, boolean isTrigger) {
    super(parent, ColliderType.CIRCLE, isTrigger);
    nodes = new ArrayList<Vector2>();
  }

  public void AddNode(Vector2 position) {
    nodes.add(position);
  }
}
