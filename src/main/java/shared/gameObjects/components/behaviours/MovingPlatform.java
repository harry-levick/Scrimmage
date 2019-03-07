package shared.gameObjects.components.behaviours;

import shared.gameObjects.GameObject;
import shared.gameObjects.components.Behaviour;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.physics.data.Collision;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class MovingPlatform extends Behaviour {

  private float speed;
  private Vector2 endpointA;
  private Vector2 endpointB;
  private Vector2 movementFactor;
  private boolean movingRight;

  public MovingPlatform(GameObject parent) {
    super(parent);
    //Temp for Testing
    speed = 10f;
    endpointA = new Vector2(300,300);
    endpointB = new Vector2(600, 300);
    movementFactor = endpointB.sub(endpointA).div(speed);
  }

  @Override
  public void OnCollisionStay(Collision col) {
    if(((Rigidbody) col.getCollidedObject().getComponent(
        ComponentType.RIGIDBODY)).getBodyType() == RigidbodyType.DYNAMIC) {
      col.getCollidedObject().getTransform().translate(movementFactor);
    }
  }
}
