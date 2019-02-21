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
  }

  private void calculateCollisionValues() {
    Collider colA = (Collider) bodyA.getParent().getComponent(ComponentType.COLLIDER);
    Collider colB = (Collider) bodyB.getParent().getComponent(ComponentType.COLLIDER);
    switch(colA.getColliderType()) {
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
    float A = boxA.getCorners()[0].magnitude(boxB.getCentre());
    float B = boxA.getCorners()[1].magnitude(boxB.getCentre());
    float C = boxA.getCorners()[2].magnitude(boxB.getCentre());
    float D = boxA.getCorners()[3].magnitude(boxB.getCentre());
    if (A <= B && A <= C && A <= D) {
      penetrationDistance = boxA.getCorners()[0].sub(boxB.getCorners()[2]);
      dir = Math.abs(penetrationDistance.getX()) < Math.abs(penetrationDistance.getY()) ? CollisionDirection.LEFT : CollisionDirection.UP;
    } else if (B <= C && B <= D) {
      penetrationDistance = boxA.getCorners()[1].sub(boxB.getCorners()[3]);
      dir = Math.abs(penetrationDistance.getX()) < Math.abs(penetrationDistance.getY()) ? CollisionDirection.LEFT : CollisionDirection.DOWN;
    } else if (C <= D) {
      penetrationDistance = boxA.getCorners()[2].sub(boxB.getCorners()[0]);
      dir = Math.abs(penetrationDistance.getX()) < Math.abs(penetrationDistance.getY()) ? CollisionDirection.RIGHT : CollisionDirection.DOWN;
    } else {
      penetrationDistance = boxA.getCorners()[3].sub(boxB.getCorners()[1]);
      dir = Math.abs(penetrationDistance.getX()) < Math.abs(penetrationDistance.getY()) ? CollisionDirection.RIGHT : CollisionDirection.UP;
    }

    switch(dir) {
      case UP:
        if(bodyB.getBodyType() == RigidbodyType.STATIC) {
          collisionNormal = Vector2.Down();
          pentrationDepth = penetrationDistance.getY();
        }
        else {
          collisionNormal = Vector2.Up();
          pentrationDepth = penetrationDistance.getY()*1.2f;
        }
        break;
      case DOWN:
        if(bodyB.getBodyType() == RigidbodyType.STATIC) {
          collisionNormal = Vector2.Up();
          pentrationDepth = penetrationDistance.getY();
        }
        else {
          collisionNormal = Vector2.Down();
          pentrationDepth = penetrationDistance.getY()*1.2f;
        }
        break;
      case RIGHT:
        collisionNormal = Vector2.Left();
        pentrationDepth = penetrationDistance.getX();
        break;
      case LEFT:
        collisionNormal = Vector2.Right();
        pentrationDepth = penetrationDistance.getX()*-1;
        break;
    }
  }

  public void process() {
    if (bodyB.getBodyType() == RigidbodyType.STATIC) {
      float vOnNormal = bodyA.getVelocity().dot(collisionNormal);
      if(vOnNormal > 0) {
        return;
      }

      float e = Math.max(bodyA.getMaterial().getRestitution(), bodyB.getMaterial().getRestitution());

      float j = -1*(1 + e) * vOnNormal;
      j /= bodyA.getInv_mass() + bodyB.getInv_mass();


      Vector2 impulse = collisionNormal.mult(j);
      bodyA.setVelocity(bodyA.getVelocity().add(impulse.mult(bodyA.getInv_mass())));

      Vector2 positionCorrection = positionCorrection();
      bodyA.correctPosition(positionCorrection.mult(bodyA.getInv_mass()));
    } else {
      Vector2 velocityCol = bodyB.getVelocity().sub(bodyA.getVelocity());
      float vOnNormal = velocityCol.dot(collisionNormal);
      if(vOnNormal > 0) {
        return;
      }

      float e = Math.min(bodyA.getMaterial().getRestitution(), bodyB.getMaterial().getRestitution());

      float j = -1*(1 + e) * vOnNormal;
      j /= bodyA.getInv_mass() + bodyB.getInv_mass();

      Vector2 impulse = collisionNormal.mult(j);
      bodyA.setVelocity(bodyA.getVelocity().sub(impulse.mult(bodyA.getInv_mass())));
      bodyB.setVelocity(bodyB.getVelocity().add(impulse.mult(bodyB.getInv_mass())));

      Vector2 positionCorrection = positionCorrection();
      bodyA.correctPosition(positionCorrection.mult(bodyB.getInv_mass()));
      bodyB.correctPosition(positionCorrection.mult(-1*bodyA.getInv_mass()));
    }
    switch (dir) {
      case UP:
        bodyB.setGrounded(true);
        break;
      case DOWN:
        bodyA.setGrounded(true);
        break;
      case RIGHT:
      case LEFT:
        break;
    }
  }

  private Vector2  positionCorrection() {
    float percent = 0.8f;
    float slop = 0.005f;
    //System.out.println(c);
     Vector2 correction = collisionNormal.mult(Math.max(pentrationDepth - slop, 0.0f ) / (bodyA.getInv_mass() + bodyB.getInv_mass()) * percent);
    //  Vector2 correction = collisionNormal.mult(pentrationDepth*percent/(bodyA.getInv_mass()+bodyB.getInv_mass()));
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


