package shared.physics.data;

import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.physics.types.CollisionDirection;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class DynamicCollision {
  private Rigidbody bodyA;
  private Rigidbody bodyB;
  private Vector2 collisionNormal;
  private Vector2 penetrationDistance;
  private CollisionDirection dir;
  private float pentrationDepth;

  public DynamicCollision(Rigidbody bodyA, Rigidbody bodyB) {
    this.bodyA = bodyA;
    this.bodyB = bodyB;
    calculateCollisionValues();
    process();
  }

  private void calculateCollisionValues() {
    Collider colA = (Collider) bodyA.getParent().getComponent(ComponentType.COLLIDER);
    Collider colB = (Collider) bodyB.getParent().getComponent(ComponentType.COLLIDER);
    switch (colA.getColliderType()) {
      case BOX:
        switch (colB.getColliderType()) {
          case BOX:
            resolveCollision((BoxCollider) colA, (BoxCollider) colB);
            break;
        }
        break;
      case CIRCLE:
        switch (colB.getColliderType()) {
        }
        break;
      case EDGE:
        switch (colB.getColliderType()) {
        }
        break;
    }
  }

  private void resolveCollision(BoxCollider boxA, BoxCollider boxB) {
    Vector2 n = boxB.getCentre().sub(boxA.getCentre());
    float x_overlap =  boxA.getSize().getX()*0.5f + boxB.getSize().getX()*0.5f - Math.abs(n.getX());
    float y_overlap =  boxA.getSize().getY()*0.5f + boxB.getSize().getY()*0.5f - Math.abs(n.getY());

    penetrationDistance = new Vector2(x_overlap, y_overlap);
    if(penetrationDistance.getX() < penetrationDistance.getY()) {
      if(n.getX() < 0) {
        collisionNormal =  bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Right() : Vector2.Left();
      }
      else {
        collisionNormal =  bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Left() : Vector2.Zero();
      }
      pentrationDepth = x_overlap;
    }
    else {
      if (n.getY() < 0) {
        collisionNormal =  bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Down() : Vector2.Up();
        bodyB.setGrounded(true);
      }
      else {
        collisionNormal =  bodyB.getBodyType() == RigidbodyType.STATIC ? Vector2.Up() : Vector2.Down();
        bodyA.setGrounded(true);
      }
      pentrationDepth = y_overlap;
    }
  }

  public void process() {
    if (bodyB.getBodyType() == RigidbodyType.STATIC) {
      float vOnNormal = bodyA.getVelocity().dot(collisionNormal);
      if (vOnNormal > 0) {
        return;
      }

      float e =
          Math.max(bodyA.getMaterial().getRestitution(), bodyB.getMaterial().getRestitution());

      float j = -1 * (1 + e) * vOnNormal;
      j /= bodyA.getInv_mass() + bodyB.getInv_mass();

      Vector2 impulse = collisionNormal.mult(j);
      bodyA.setVelocity(bodyA.getVelocity().add(impulse.mult(bodyA.getInv_mass())));

      Vector2 positionCorrection = positionCorrection();
      bodyA.correctPosition(positionCorrection.mult(bodyA.getInv_mass()));
    } else {
      Vector2 velocityCol = bodyB.getVelocity().sub(bodyA.getVelocity());
      float vOnNormal = velocityCol.dot(collisionNormal);
      if (vOnNormal > 0) {
        return;
      }

      float e =
          Math.min(bodyA.getMaterial().getRestitution(), bodyB.getMaterial().getRestitution());

      float j = -1 * (1 + e) * vOnNormal;
      j /= bodyA.getInv_mass() + bodyB.getInv_mass();

      Vector2 impulse = collisionNormal.mult(j);
      bodyA.setVelocity(bodyA.getVelocity().sub(impulse.mult(bodyA.getInv_mass())));
      bodyB.setVelocity(bodyB.getVelocity().add(impulse.mult(bodyB.getInv_mass())));

      Vector2 positionCorrection = positionCorrection();
      bodyA.correctPosition(positionCorrection.mult(-1 * bodyA.getInv_mass()));
      bodyB.correctPosition(positionCorrection.mult(bodyB.getInv_mass()));
    }
  }

  private Vector2 positionCorrection() {
    float percent = 0.8f;
    float slop = 0.03f;
    // System.out.println(c);
    Vector2 correction =
        collisionNormal.mult(
            Math.max(pentrationDepth - slop, 0.0f)
                / (bodyA.getInv_mass() + bodyB.getInv_mass())
                * percent);
    //  Vector2 correction =
    // collisionNormal.mult(pentrationDepth*percent/(bodyA.getInv_mass()+bodyB.getInv_mass()));
    return correction;
    // return penetrationDistance;
  }

  public Rigidbody getBodyA() {
    return bodyA;
  }

  public Rigidbody getBodyB() {
    return bodyB;
  }

  public float getPentrationDepth() {
    return pentrationDepth;
  }

  public Vector2 getCollisionNormal() {
    return collisionNormal;
  }

  public Vector2 getPenetrationDistance() {
    return penetrationDistance;
  }
}
