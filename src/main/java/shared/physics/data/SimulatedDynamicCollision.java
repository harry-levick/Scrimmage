package shared.physics.data;

import shared.gameObjects.components.Rigidbody;
import shared.util.maths.Vector2;

/**
 * Processes and manages collisions happening with two Dynamic Rigidbodies, but only alters the Physics data on the first body. Used for simulating an object.
 */
public class SimulatedDynamicCollision extends DynamicCollision {
  /**
   * Constructs and processes a Dynamic Collision
   * @param bodyA First body involved in Collision; affected by the collision
   * @param bodyB Second body involved in Collision; will not be affected by the collision
   */
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
