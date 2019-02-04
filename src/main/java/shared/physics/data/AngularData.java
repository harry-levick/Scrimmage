package shared.physics.data;

/**
 * @author fxa579 The class contains data required for rotational kinematics/dynamics
 */
public class AngularData {

  private float angularRadius;
  private float angularCoefficient;
  private float angularVelocity;
  private float inertia;

  public AngularData(
      float angularRadius, float angularCoefficient, float angularVelocity, float inertia) {
    this.angularRadius = angularRadius;
    this.angularCoefficient = angularCoefficient;
    this.angularVelocity = angularVelocity;
    this.inertia = inertia;
  }

  public float getAngularRadius() {
    return angularRadius;
  }

  public float getAngularCoefficient() {
    return angularCoefficient;
  }

  public float getAngularVelocity() {
    return angularVelocity;
  }

  public float getInertia() {
    return inertia;
  }
}
