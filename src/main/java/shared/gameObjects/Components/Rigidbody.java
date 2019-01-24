package shared.gameObjects.Components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.Transform;
import shared.physics.types.RigibodyType;
import shared.physics.types.RigidbodyUpdateType;
import shared.util.maths.Vector2;

/**
 * @author fxa579 The primary component responsible for all Physics updates; includes data and
 *     methods to process forces and gravity
 */
public class Rigidbody extends Component implements Serializable {

  private Vector2 velocity;
  // private Force force

  private RigibodyType bodyType;
  private RigidbodyUpdateType updateMethod;

  private float frictionCoefficient;
  private float mass;
  private float gravityScale;
  private float momentOfInertia;
  private float angularCoefficient;
  private float angularVelocity;
  private float angularRadius;

  public Rigidbody(
      RigibodyType bodyType,
      RigidbodyUpdateType updateMethod,
      float frictionCoefficient,
      float mass,
      float gravityScale,
      float momentOfInertia,
      float angularCoefficient,
      float angularVelocity,
      float angularRadius,
      GameObject parent) {
    super(parent, ComponentType.RIGIBODY);
    this.angularCoefficient = angularCoefficient;
    this.angularRadius = angularRadius;
    this.angularVelocity = angularVelocity;
    this.frictionCoefficient = frictionCoefficient;
    this.gravityScale = gravityScale;
    this.momentOfInertia = momentOfInertia;
    this.mass = mass;
    this.bodyType = bodyType;
    this.updateMethod = updateMethod;
    this.velocity = Vector2.Zero();
  }

  // Update Methods
  public void update() {
    // TODO Add Physics Updates Here
  }
  // Force Methods

  /** Applies a force of a defined from a defined source direction (Instantaneous Force) */
  void addForce(float force, Transform source) {}

  /** Moves the Rigibody to the defined space over time, instant if 0 */
  void move(Vector2 position, float time) {}

  // Getters and Setters
  public Vector2 getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2 velocity) {
    this.velocity = velocity;
  }

  public RigibodyType getBodyType() {
    return bodyType;
  }

  public void setType(RigibodyType bodyType) {
    this.bodyType = bodyType;
  }

  public RigidbodyUpdateType getUpdateMethod() {
    return updateMethod;
  }

  public void setUpdateMethod(RigidbodyUpdateType updateMethod) {
    this.updateMethod = updateMethod;
  }

  public float getFrictionCoefficient() {
    return frictionCoefficient;
  }

  public void setFrictionCoefficient(float frictionCoefficient) {
    this.frictionCoefficient = frictionCoefficient;
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

  public float getMomentOfInertia() {
    return momentOfInertia;
  }

  public void setMomentOfInertia(float momentOfInertia) {
    this.momentOfInertia = momentOfInertia;
  }

  public float getAngularCoefficient() {
    return angularCoefficient;
  }

  public void setAngularCoefficient(float angularCoefficient) {
    this.angularCoefficient = angularCoefficient;
  }

  public float getAngularVelocity() {
    return angularVelocity;
  }

  public void setAngularVelocity(float angularVelocity) {
    this.angularVelocity = angularVelocity;
  }

  public float getAngularRadius() {
    return angularRadius;
  }

  public void setAngularRadius(float angularRadius) {
    this.angularRadius = angularRadius;
  }
}
