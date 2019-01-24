package shared.physics;

/**
 * @author fxa579 The singleton class respomsible for raycasting and physics constants/equations
 */
public class Physics {

  public static final double GRAVITY = -9.81;
  private static Physics ourInstance = new Physics();

  private Physics() {
  }

  public static Physics getInstance() {
    return ourInstance;
  }

  /*
  TODO RayCast, Core Force Equations, Collision Data Type?
   */
}
