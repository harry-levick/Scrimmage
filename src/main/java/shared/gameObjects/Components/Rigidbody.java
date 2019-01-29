package shared.gameObjects.components;

import java.io.Serializable;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.physics.types.RigidbodyUpdateType;
import shared.util.maths.Vector2;

/**
 * @author fxa579 The primary components responsible for all Physics updates; includes data and
 *     methods to process forces and gravity
 */
public class Rigidbody extends Component implements Serializable {

  private Vector2 deltaPos;
  private Vector2 velocity;
  private Vector2 currentForce;
  private Vector2 lastAcceleration;
  private Vector2 acceleration;

  private RigidbodyType bodyType;
  private RigidbodyUpdateType updateMethod;
  private MaterialProperty material;
  private AngularData angularData;
  private ArrayList<Collision> collisions;
  private ArrayList<Vector2> forces;

  private float mass;
  private float gravityScale;
  private float airDrag;

  private boolean grounded;

  public Rigidbody(
      RigidbodyType bodyType,
      RigidbodyUpdateType updateMethod,
      float mass,
      float gravityScale,
      float airDrag,
      MaterialProperty material,
      AngularData angularData,
      GameObject parent) {
    super(parent, ComponentType.RIGIBODY);
    this.gravityScale = gravityScale;
    this.mass = mass;
    this.airDrag = airDrag;
    this.material = material;
    this.angularData = angularData;
    this.bodyType = bodyType;
    this.updateMethod = updateMethod;

    collisions = new ArrayList<>();
    forces = new ArrayList<>();
    velocity = acceleration = lastAcceleration = deltaPos = Vector2.Zero();
    currentForce = acceleration.mult(mass);
  }

  // Update Methods
  public void update() {
    // TODO Add Physics Updates Here
    applyCollisions();
    applyForces();
    // Update Velocity
    // Move Body
    lastAcceleration = acceleration;
    deltaPos =
        deltaPos.add(
            velocity
                .mult(Physics.TIMESTEP)
                .add(acceleration.mult(0.5f).mult(Physics.TIMESTEP * Physics.TIMESTEP)));
    acceleration = currentForce.div(mass);
    acceleration = lastAcceleration.add(acceleration).div(2);
    velocity = velocity.add(acceleration.mult(Physics.TIMESTEP));
  }
  // Force Methods

  /** Applies a force of a defined from a defined source direction (Instantaneous Force) */
  void addForce(Vector2 force) {
    forces.add(force);
  }

  /** Moves the Rigibody to the defined space over time, instant if 0 */
  void move(Vector2 distance, float time) {
    if (time <= 0) {
      getParent().getTransform().translate(distance);
    } else {

    }
  }

  // Update Methods
  private void applyCollisions() {
    for (Collision c : collisions) {
      if (c.getCollidedObject().getBodyType() == RigidbodyType.STATIC) {
        switch (c.getDirection()) {
          case DOWN:
          case UP:
            setVelocity(getVelocity().mult(Vector2.Right()));
            break;
          case LEFT:
          case RIGHT:
            setVelocity(getVelocity().mult(Vector2.Up()));
            break;
        }
      } else if (c.getCollidedObject().getBodyType() == RigidbodyType.DYNAMIC) {
        Vector2 collisionForce;
        // TODO Momentum and Impulse Calculation
      }
    }
  }

  private void applyForces() {
    currentForce = Vector2.Zero();
    for (Vector2 force : forces) {
      currentForce = currentForce.add(force);
    }
    if (!grounded) {
      currentForce = currentForce.add(Vector2.Up().mult(Physics.GRAVITY * mass * gravityScale));
    }
  }
  // Getters and Setters
  public Vector2 getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2 velocity) {
    this.velocity = velocity;
  }

  public RigidbodyType getBodyType() {
    return bodyType;
  }

  public void setType(RigidbodyType bodyType) {
    this.bodyType = bodyType;
  }

  public RigidbodyUpdateType getUpdateMethod() {
    return updateMethod;
  }

  public void setUpdateMethod(RigidbodyUpdateType updateMethod) {
    this.updateMethod = updateMethod;
  }

  public float getMass() {
    return mass;
  }

  public void setMass(float mass) {
    this.mass = mass;
  }

  public float getGravityScale() {
    return gravityScale;
  }

  public void setGravityScale(float gravityScale) {
    this.gravityScale = gravityScale;
  }

  public MaterialProperty getMaterial() {
    return material;
  }

  public void setMaterial(MaterialProperty material) {
    this.material = material;
  }

  public AngularData getAngularData() {
    return angularData;
  }

  public void setAngularData(AngularData angularData) {
    this.angularData = angularData;
  }
}
