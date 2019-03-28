package shared.physics.data;

import java.io.Serializable;

/**
 * @author fxa579 Contains friction data affecting collisions
 */
public class MaterialProperty implements Serializable {

  private float restitution;
  private float staticFriction;
  private float kineticFriction;

  /**
   * Properties of a material dealing with friction and bouncing
   *
   * @param restitution A 'bounciness' factor. At 0, the object velocity is set to 0 on impact. At
   * 1, the object speed does not change on impact, only directio.
   * @param staticFriction The friction coefficient that is used to calculate whether or not a force
   * moves. The higher the friction, the more force it takes to start pushing an object.
   * @param kineticFriction The friction coefficent that is used to calculate how much resistive
   * force is applied while pushing an object. The higher the friction, the more force it takes to
   * move an object.
   */
  public MaterialProperty(float restitution, float staticFriction, float kineticFriction) {
    this.kineticFriction = kineticFriction;
    this.restitution = restitution;
    this.staticFriction = staticFriction;
  }

  public float getRestitution() {
    return restitution;
  }

  public float getStaticFriction() {
    return staticFriction;
  }

  public float getKineticFriction() {
    return kineticFriction;
  }
}
