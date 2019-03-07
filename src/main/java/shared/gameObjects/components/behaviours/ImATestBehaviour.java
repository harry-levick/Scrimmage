package shared.gameObjects.components.behaviours;

import shared.gameObjects.GameObject;
import shared.gameObjects.components.Behaviour;
import shared.gameObjects.players.Player;
import shared.physics.data.Collision;

public class ImATestBehaviour extends Behaviour {

  public ImATestBehaviour(GameObject parent) {
    super(parent);
  }

  @Override
  public void OnCollisionEnter(Collision col) {
    if(col.getCollidedObject() instanceof Player) {
      System.out.println("I collided as a Behaviour!");
    }
  }
}
