package shared.gameObjects.components.behaviours.blockBehaviours;

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
    // Temp for Testing
    speed = 150f;
    endpointA = parent.getTransform().getPos().add(new Vector2(-40, 0));
    endpointB = parent.getTransform().getPos().add(new Vector2(+280, 0));
    movementFactor = endpointB.sub(endpointA).div(speed);
  }

  @Override
  public void update() {
    getParent().getTransform().translate(movementFactor);
    if (getParent().getTransform().getPos().getX() >= endpointB.getX()
        || getParent().getTransform().getPos().getX() <= endpointA.getX())
      movementFactor = movementFactor.mult(-1);
  }

  @Override
  public void OnCollisionStay(Collision col) {
    if (((Rigidbody) col.getCollidedObject().getComponent(ComponentType.RIGIDBODY)).getBodyType()
            == RigidbodyType.DYNAMIC
        && col.getNormalCollision().equals(Vector2.Up())) {
      col.getCollidedObject().getTransform().translate(movementFactor);
    }
  }

  public void setEndpointA(Vector2 endpointA) {
    this.endpointA = endpointA;
    movementFactor = endpointB.sub(endpointA).div(speed);
  }

  public void setEndpointB(Vector2 endpointB) {
    this.endpointB = endpointB;
    movementFactor = endpointB.sub(endpointA).div(speed);
  }

  public void setSpeed(float speed) {
    this.speed = speed;
    movementFactor = endpointB.sub(endpointA).div(speed);
  }
}
