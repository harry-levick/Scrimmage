package shared.gameObjects.components;

import shared.gameObjects.GameObject;
import shared.physics.data.Collision;

/** @author fxa579 Base class for behaviours that can be added to GameObjects */
public class Behaviour extends Component {

  public Behaviour(GameObject parent) {
    super(parent, ComponentType.BEHAVIOUR);
  }

  public void OnCollisionEnter(Collision col) {}

  /**
   * Called on the every frame a specific collider is colliding with the object.
   *
   * @param col Collision data of the collision.
   */
  public void OnCollisionStay(Collision col) {}

  /**
   * Called on the first frame a specific collider stops colliding with the object.
   *
   * @param col Collision data of the collision.
   */
  public void OnCollisionExit(Collision col) {}

  /**
   * Called on the first frame a specific collider is colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerEnter(Collision col) {}

  /**
   * Called on the every frame a specific collider is colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerStay(Collision col) {}

  /**
   * Called on the first frame a specific collider stops colliding with the object; only works if
   * isTrigger is true.
   *
   * @param col Collision data of the collision.
   */
  public void OnTriggerExit(Collision col) {}
}
