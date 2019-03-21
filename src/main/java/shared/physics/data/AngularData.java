package shared.physics.data;

import java.io.Serializable;

/**
 * @author fxa579 Cotains data required for rotational kinematics/dynamics
 */
public class AngularData implements Serializable {

  private float angularRadius;
  private float angularCoefficient;
  private float inertia;
  private float invInertia;

  /**
   * Creates AngularData container
   * @param angularRadius The radius the object will be rotating at
   * @param angularCoefficient The friction of angular motion
   * @param angularVelocity The angular velocitu
   * @param inertia The "angular" mass that determines the impulse forces
   */
  public AngularData(
      float angularRadius, float angularCoefficient, float angularVelocity, float inertia) {
    this.angularRadius = angularRadius;
    this.angularCoefficient = angularCoefficient;
    this.inertia = inertia;
    invInertia = inertia == 0 ? 0 : 1 / inertia;
  }

  public float getAngularRadius() {
    return angularRadius;
  }

  public void setAngularRadius(float angularRadius) {
    this.angularRadius = angularRadius;
  }

  public float getAngularCoefficient() {
    return angularCoefficient;
  }

  public float getInertia() {
    return inertia;
  }

  public float getInvInertia() {
    return invInertia;
  }
}
