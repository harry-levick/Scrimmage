package shared.gameObjects.components;

import java.io.Serializable;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.physics.Physics;
import shared.physics.data.AngularData;
import shared.physics.data.Collision;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

/**
 * @author fxa579 The primary components responsible for all Physics updates; includes data and
 *     methods to process forces and gravity
 */
public class Rigidbody extends Component implements Serializable {

  private Vector2 deltaPos;
  private Vector2 deltaPosUpdate;

  private Vector2 velocity;

  private Vector2 currentForce;
  private Vector2 lastAcceleration;
  private Vector2 acceleration;

  private RigidbodyType bodyType;

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

    collisions = new ArrayList<>();
    forces = new ArrayList<>();
    velocity = acceleration = lastAcceleration = deltaPos = deltaPosUpdate = Vector2.Zero();
    currentForce = acceleration.mult(mass);
  }

  // Update Methods
  public void update() {
    // TODO
    applyCollisions();
    applyForces();
    lastAcceleration = acceleration;

    deltaPos =
        deltaPos.add(
            velocity
                .mult(Physics.TIMESTEP)
                .add(acceleration.mult(0.5f).mult(Physics.TIMESTEP * Physics.TIMESTEP)));
    deltaPos = deltaPosUpdate.add(deltaPos);
    deltaPosUpdate = Vector2.Zero();
    move(deltaPos, 0);

    acceleration = currentForce.div(mass);
    acceleration = lastAcceleration.add(acceleration).div(2);
    velocity = velocity.add(acceleration.mult(Physics.TIMESTEP));

    grounded = false;
  }
  // Force Methods

  /** Applies a force to be added on the next physics update */
  public void addForce(Vector2 force) {
    forces.add(force);
  }

  /** Moves the Object to the defined space over time, instant if 0 */
  public void move(Vector2 distance, float time) {
    if (time <= 0) {
      getParent().getTransform().translate(distance);
    } else {

    }
  }
  /**
   * Moves the Object a given distance on the next update. The object may end up on another space
   * due to external forces.
   */
  public void move(Vector2 distance) {
    move(distance, 0);
  }

  // Update Methods
  private void applyCollisions() {
    for (Collision c : collisions) {
      if (c.getCollidedObject().getBodyType() == RigidbodyType.STATIC) {
        switch (c.getDirection()) {
          case DOWN:
            grounded = true;
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
    forces.clear();
    if (!grounded) {
      currentForce = currentForce.add(Vector2.Up().mult(Physics.GRAVITY * mass * gravityScale));
    }
    // TODO add Friction and Drag
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
