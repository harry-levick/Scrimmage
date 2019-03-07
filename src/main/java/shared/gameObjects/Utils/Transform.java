package shared.gameObjects.Utils;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.util.maths.Vector2;

public class Transform implements Serializable {

  private Vector2 topPos;
  private Vector2 botPos;
  private Vector2 rotatedPos;
  private Vector2 rotatedSize;
  private Vector2 size;
  private float rot;
  private GameObject gameObject;

  public Transform(GameObject parent) {
    this.topPos = this.size = Vector2.Zero();
    this.botPos = this.topPos.add(this.size);
    rotatedPos = topPos;
    rotatedSize = size;
    this.rot = 0;
    gameObject = parent;
  }

  public Transform(GameObject parent, Vector2 topPos) {
    this.topPos = topPos;
    this.size = Vector2.Unit();
    this.botPos = this.topPos.add(this.size);
    rotatedPos = topPos;
    rotatedSize = size;
    this.rot = 0;
    gameObject = parent;
  }

  public Transform(GameObject parent, Vector2 topPos, Vector2 size) {
    this.topPos = topPos;
    this.size = size;
    this.botPos = this.topPos.add(this.size);
    rotatedPos = topPos;
    rotatedSize = size;
    this.rot = 0;
    gameObject = parent;
  }

  /** Moves the attached gameObject to the desired position. */
  public void translate(Vector2 translateFactor) {
    topPos = topPos.add(translateFactor);
    botPos = botPos.add(translateFactor);
  }

  /** [Does not do anything currently] */
  public void rotate(float rotation) {
    rot += rotation;
    if (rot > 180) {
      rot -= 180;
    }
    if (rot < -180) {
      rot += 180;
    }
    float angle = (float) Math.toRadians(rot);
    float posX = topPos.getX(), posY = topPos.getY();
    this.rotatedPos =
        new Vector2(
            posX * Math.cos(angle) - posY * Math.sin(angle),
            posX * Math.sin(angle) + posY * Math.cos(angle));
    posX = size.getX();
    posY = size.getY();
    this.rotatedSize =
        new Vector2(
            posX * Math.cos(angle) - posY * Math.sin(angle),
            posX * Math.sin(angle) + posY * Math.cos(angle));
  }

  /**
   * Scales an object's size with a given scale factor. It's JavaFX position remains the same.
   *
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

  public float getRot() {
    return rot;
  }

  public void setRot(float rot) {
    this.rot = rot;
    rotate(0);
    Component rb = gameObject.getComponent(ComponentType.RIGIDBODY);
    if (rb != null) {
      ((Rigidbody) rb).setOrientation((float) Math.toRadians(rot));
    }
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
