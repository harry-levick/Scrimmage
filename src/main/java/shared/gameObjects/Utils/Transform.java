package shared.gameObjects.Utils;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.util.maths.Rotation;
import shared.util.maths.Vector2;

public class Transform implements Serializable {

  private Vector2 topPos;
  private Vector2 botPos;
  private Vector2 size;
  private Rotation rot;
  private GameObject gameObject;

  public Transform(GameObject parent) {
    this.topPos = this.size = Vector2.Zero();
    this.botPos = this.topPos.add(this.size);
    this.rot = new Rotation(0);
    gameObject = parent;
  }

  public Transform(GameObject parent, Vector2 topPos) {
    this.topPos = topPos;
    this.size = Vector2.Unit();
    this.botPos = this.topPos.add(this.size);
    this.rot = new Rotation(0);
    gameObject = parent;
  }

  public Transform(GameObject parent, Vector2 topPos, Vector2 size) {
    this.topPos = topPos;
    this.size = size;
    this.botPos = this.topPos.add(this.size);
    this.rot = new Rotation(0);
    gameObject = parent;
  }

  /**
   * Moves the attached gameObject to the desired position.
   */
  public void translate(Vector2 translateFactor) {
    topPos = topPos.add(translateFactor);
    botPos = botPos.add(translateFactor);
  }

  /**
   * [Does not do anything currently]
   * @param rotation
   */
  public void rotate(Vector2 rotation) {
    // TODO Add Rotation Methods
  }

  /**
   * Scales an object's size with a given scale factor. It's JavaFX position remains the same.
   * @param scaleFactor The amount to scale by per axis.
   */
  public void scale(Vector2 scaleFactor) {
    this.size = size.mult(scaleFactor);
    botPos = topPos.add(size);
  }

  /**
   * Computes the (approximated) distance between two objects
   *
   * @param transform The object comparing to
   * @return The distance between the two objects
   */
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
    return topPos;
  }

  public void setPos(Vector2 pos) {
    this.topPos = pos;
  }

  public Vector2 getBotPos() {
    return botPos;
  }

  public Vector2 getSize() {
    return size;
  }
}
