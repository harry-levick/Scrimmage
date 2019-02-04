package shared.physics.data;

/** @author fxa579 Contains data affect */
public class MaterialProperty {

  private float restitution;
  private float staticFriction;
  private float kineticFriction;

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
