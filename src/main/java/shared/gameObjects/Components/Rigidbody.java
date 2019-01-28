package shared.gameObjects.components;

import java.io.Serializable;
import shared.gameObjects.GameObject;
import shared.gameObjects.Utils.Transform;
import shared.physics.data.AngularData;
import shared.physics.data.Force;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigibodyType;
import shared.physics.types.RigidbodyUpdateType;
import shared.util.maths.Vector2;

/**
 * @author fxa579 The primary components responsible for all Physics updates; includes data and
 *     methods to process forces and gravity
 */
public class Rigidbody extends Component implements Serializable {

  private Vector2 velocity;
  private Force currentForce;

  private RigibodyType bodyType;
  private RigidbodyUpdateType updateMethod;
  private MaterialProperty material;
  private AngularData angularData;

  private float mass;
  private float gravityScale;

  public Rigidbody(
      RigibodyType bodyType,
      RigidbodyUpdateType updateMethod,
      float mass,
      float gravityScale,
      MaterialProperty material,
      AngularData angularData,
      GameObject parent) {
    super(parent, ComponentType.RIGIBODY);
    this.gravityScale = gravityScale;
    this.mass = mass;
    this.material = material;
    this.angularData = angularData;
    this.bodyType = bodyType;
    this.updateMethod = updateMethod;

    velocity = Vector2.Zero();
    currentForce = new Force();
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
