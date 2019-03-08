package shared.physics.data;

import shared.gameObjects.components.Rigidbody;
import shared.util.maths.Vector2;

public class SimulatedDynamicCollision extends DynamicCollision {

  public SimulatedDynamicCollision(Rigidbody bodyA, Rigidbody bodyB) {
    super(bodyA, bodyB);
  }

  @Override
  public void process() {
    Vector2 velocityCol = bodyB.getVelocity().sub(bodyA.getVelocity());
    float vOnNormal = velocityCol.dot(collisionNormal);
    if (vOnNormal > 0) {
      return;
    }
    float e = Math.max(bodyA.getMaterial().getRestitution(), bodyB.getMaterial().getRestitution());

    float j = -1 * (1 + e) * vOnNormal;
    j /= bodyA.getInv_mass() + bodyB.getInv_mass();

    Vector2 impulse = collisionNormal.mult(j);
    bodyA.setVelocity(bodyA.getVelocity().sub(impulse.mult(bodyA.getInv_mass())));

    Vector2 positionCorrection = positionCorrection();
    bodyA.correctPosition(positionCorrection.mult(-1 * bodyA.getInv_mass()));
  }
}
