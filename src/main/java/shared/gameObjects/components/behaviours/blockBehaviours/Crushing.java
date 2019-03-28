package shared.gameObjects.components.behaviours.blockBehaviours;


import shared.gameObjects.GameObject;
import shared.gameObjects.components.Behaviour;
import shared.gameObjects.players.Player;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/**
 * Crushing is a behaviour attached to objects that will kill all grounded players it lands on
 */
public class Crushing extends Behaviour {

  /**
   * @param parent Object to apply "Crushing" behaviour to
   */
  public Crushing(GameObject parent) {
    super(parent);
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    if ((col.getCollidedObject() instanceof Player) && col.getNormalCollision()
        .equals(Vector2.Down())) {
      if (((Player) col.getCollidedObject()).isGrounded()) {
        ((Player) col.getCollidedObject()).deductHp(9999);
      }
    }
  }
}
