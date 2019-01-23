package shared.gameObjects.Utils;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.util.maths.Rotation;
import shared.util.maths.Vector2;

public class Transform implements Serializable {

  private Vector2 pos;
  private Rotation rot;
  private GameObject gameObject;

  public Transform(GameObject parent) {
    this.pos = Vector2.Zero();
    this.rot = new Rotation(0);
    gameObject = parent;
  }

  public void translate(Vector2 vector) {
    pos.add(vector);
  }

  public void rotate(float rotationAmoint) {
    // TODO Add Rotation Methods
  }

  public float distance(Transform transform) {
    return getPos().magnitude(transform.getPos());
  }

  public Rotation getRot() {
    return rot;
  }

  public void setRot(Rotation rot) {
    this.rot = rot;
  }

  public Vector2 getPos() {
    return pos;
  }

  public void setPos(Vector2 pos) {
    this.pos = pos;
  }
}
