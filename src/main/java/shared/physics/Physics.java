package shared.physics;

/** @author fxa579 The singleton class respomsible for raycasting and physics constants/equations */
public class Physics {

  public static final float GRAVITY = 9.81f;
  public static final float TIMESTEP = 1f / 60;
  private static Physics ourInstance = new Physics();

  private Physics() {}

  public static Physics getInstance() {
    return ourInstance;
  }

  /*
  TODO RayCast, Core Force Equations, Collision Data Type?
   */
}
