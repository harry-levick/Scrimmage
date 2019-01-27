package shared.physics;

/**
 * @author fxa579 The singleton class respomsible for raycasting and physics constants/equations
 */
public class Physics {

  private static Physics ourInstance = new Physics();

  public static final double GRAVITY = -9.81;

  public static Physics getInstance() {
    return ourInstance;
  }

  private Physics() {
  }


  /*
  TODO RayCast, Core Force Equations, Collision Data Type?
   */
}
