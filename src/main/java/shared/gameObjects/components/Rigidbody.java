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
  private Vector2 impactVelocity;

  private Vector2 currentForce;
  private Vector2 lastAcceleration;
  private Vector2 acceleration;

  private RigidbodyType bodyType;

  private MaterialProperty material;
  private AngularData angularData;
  private ArrayList<Collision> collisions;
  private ArrayList<Vector2> forces;
  private ArrayList<ForceTime> forceTimes;

  private float mass;
  private float inv_mass;
  private float gravityScale;
  private float airDrag;

  private boolean grounded;

  /**
   * The main component responsible for Physics calculations. Attach this to a GameObject to have it
   * affected by the Physics Engine.
   *
   * @param bodyType The Rigidbody Type. Dynamic moves, static does not.
   * @param mass The mass of the objects, affects things like gravity and friction.
   * @param gravityScale The scaling factor of how much gravity affects the object.
   * @param airDrag The friction coefficient of movement while in-midair
   * @param material The physics material the object is made of.
   * @param angularData The angular information of the object.
   * @param parent The GameObject the object is attached to.
   */
  public Rigidbody(
      RigidbodyType bodyType,
      float mass,
      float gravityScale,
      float airDrag,
      MaterialProperty material,
      AngularData angularData,
      GameObject parent) {
    super(parent, ComponentType.RIGIDBODY);
    this.gravityScale = gravityScale;
    this.mass = mass;
    this.inv_mass = 1 / mass;
    this.airDrag = airDrag;
    this.material = material;
    this.angularData = angularData;
    this.bodyType = bodyType;
    if (bodyType == RigidbodyType.STATIC) {
      this.mass = Integer.MAX_VALUE;
      this.inv_mass = 0;
    }

    collisions = new ArrayList<>();
    forces = new ArrayList<>();
    forceTimes = new ArrayList<>();

    impactVelocity = Vector2.Zero();
    velocity = Vector2.Zero();
    acceleration = Vector2.Zero();
    lastAcceleration = Vector2.Zero();
    deltaPos = Vector2.Zero();
    deltaPosUpdate = Vector2.Zero();
    currentForce = acceleration.mult(mass);
  }

  // Update Methods

  /**
   * Called every physics frame, manages the velocity, forces, position, etc.
   */
  public void update() {
    if (bodyType == RigidbodyType.DYNAMIC) {
      applyCollisions();
      applyForces();
      updateVelocity();
      grounded = false;
    } else {
      velocity = Vector2.Zero();
    }
  }
  // Force Methods

  /** */
  public void addForce(Vector2 force) {
    forces.add(force);
  }

  /**
   * Applies a force to be added over time; the force is automatically divided and added on each
   * update frame equally.
   *
   * @param force The total force to be applied
   * @param time The time to spread the force over
   */
  public void addForce(Vector2 force, float time) {
    float iterations = time / Physics.TIMESTEP;
    Vector2 forceToApply = force.div(iterations);
    forceTimes.add(new ForceTime(forceToApply, (int) iterations));
  }

  /**
   * Sets the velocity to match the desired distance / time needed. Could potentially be overtaken
   * by the physics engine.
   *
   * @param distance The distance required to be covered.
   * @param time The time to cover the distance.
   */
  public void move(Vector2 distance, float time) {
    if (time <= 0) {
      deltaPosUpdate = deltaPosUpdate.add(distance);
    } else {
      this.velocity = distance.div(time);
    }
  }

  /**
   * Moves the Object a given distance on the next update. The object may end up on another space
   * due to external forces.
   *
   * @param distance The distance to the cover.
   */
  public void move(Vector2 distance) {
    move(distance, 0);
  }

  public void moveX(float distance) {
    move(new Vector2(distance, 0));
  }

  public void moveX(float distance, float time) {
    move(new Vector2(distance, 0), time);
  }

  public void moveY(float distance) {
    move(new Vector2(0, distance));
  }

  public void moveY(float distance, float time) {
    move(new Vector2(0, distance), time);
  }

  // Update Methods

  /** An update method; all collision updates happen here */
  private void applyCollisions() {}

  public void correctPosition(Vector2 distance) {
    getParent().getTransform().translate(distance);
  }

  /** An update method; all force updates happen here. */
  private void applyForces() {

    currentForce = Vector2.Zero();
    for (ForceTime force : forceTimes) {
      if (force.iterate()) {
        currentForce = currentForce.add(force.getForce());
      } else {

      }
    }
    for (Vector2 force : forces) {
      currentForce = currentForce.add(force);
    }
    forces.clear();
    // Gravity and Friction
    float gravityForce = Physics.GRAVITY * mass * gravityScale;
    if (!grounded) {
      currentForce = currentForce.add(Vector2.Down().mult(gravityForce));
      currentForce =
          currentForce.add(Vector2.Down().mult(airDrag).mult(velocity).mult(-0.5f * mass));
    } else {
      if (currentForce.getX() > gravityForce * material.getStaticFriction()) {
        currentForce =
            currentForce.add(
                Vector2.Right()
                    .mult(Physics.GRAVITY * mass * gravityScale)
                    .mult(currentForce.getX() > 0 ? -1 : 1 * material.getKineticFriction()));
      } else {
        currentForce.setX(0);
      }
    }
  }

  /** An update method; all velocity and acceleration updates happen here */
  private void updateVelocity() {
    lastAcceleration = acceleration;

    acceleration = currentForce.div(mass);
    acceleration = lastAcceleration.add(acceleration).div(2);
    velocity = velocity.add(acceleration.mult(Physics.TIMESTEP));

    deltaPos = deltaPos.add(deltaPosUpdate);
    deltaPos =
        deltaPos.add(
            velocity
                .mult(Physics.TIMESTEP)
                .add(acceleration.mult(0.5f).mult(Physics.TIMESTEP * Physics.TIMESTEP)));
    checkForLegalMovement();
    getParent().getTransform().translate(deltaPos);
    deltaPosUpdate = Vector2.Zero();
    deltaPos = Vector2.Zero();
  }

  private void checkForLegalMovement() {
    /* TODO: Very Jittery
    float percent = -0.8f;
    ArrayList<Collision> collisions = Physics.boxcastAll(getParent().getTransform().getPos().add(deltaPos), getParent().getTransform().getSize());
    for (Collision c : collisions) {
        if(!(c.getCollidedObject() == this) && c.getCollidedObject().getBodyType() == RigidbodyType.STATIC) {
          deltaPos = deltaPos.add(c.getNormalCollision().mult(c.getPenetrationDepth()*percent));
        }
    }
    */
    return;
  }
  // Getters and Setters
  public Vector2 getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2 velocity) {
    this.velocity = velocity;
    acceleration = Vector2.Zero();
    // System.out.println(this.velocity);
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

  public float getInv_mass() {
    return inv_mass;
  }

  public float getGravityScale() {
    return gravityScale;
  }

  public void setGravityScale(float gravityScale) {
    this.gravityScale = gravityScale;
  }

  public boolean isGrounded() {
    return grounded;
  }

  public void setGrounded(boolean grounded) {
    this.grounded = grounded;
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

  public float getAirDrag() {
    return airDrag;
  }

  public void setAirDrag(float airDrag) {
    this.airDrag = airDrag;
  }

  public ArrayList<Collision> getCollisions() {
    return collisions;
  }
}

/** Helper class to apply force over time without needed to thread/coroutine */
class ForceTime implements Serializable {

  private Vector2 force;
  private int iterations;

  public ForceTime(Vector2 force, int iterations) {
    this.force = force;
    this.iterations = iterations;
  }

  public Vector2 getForce() {
    return force;
  }

  public boolean iterate() {
    if (iterations <= 0) {
      return false;
    }
    iterations--;
    return true;
  }
}
