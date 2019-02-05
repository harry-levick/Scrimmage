package shared.physics;

import java.util.ArrayList;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/**
 * @author fxa579 The singleton class respomsible for raycasting and physics constants/equations
 */
public class Physics {

  public static final float GRAVITY = 9.81f;
  public static final float TIMESTEP = 1f / 60;
  private static Physics ourInstance = new Physics();

  private Physics() {
  }
  // TODO complete raycast methods

  /**
   * Casts a ray that interacts with colliders.
   *
   * @param sourcePos The point to start casting the ray
   * @param distance The length of the ray
   * @return The first collider hit in the path, null if nothing was hit.
   */
  public static Collision raycast(Vector2 sourcePos, float distance) {
    return null;
  }

  /**
   * Casts a ray that interacts with colliders.
   *
   * @param sourcePos The point to start casting the ray
   * @param distance The length of the ray
   * @return All colliders hit in the path, null if nothing was hit.
   */
  public static ArrayList<Collision> raycastAll(Vector2 sourcePos, float distance) {
    return null;
  }

  public static Collision boxcast(Vector2 sourcePos, float distance) {
    return null;
  }

  public static ArrayList<Collision> boxcastAll(Vector2 sourcePos, float distance) {
    return null;
  }

  public static Collision circlecast(Vector2 sourcePos, float distance) {
    return null;
  }

  public static ArrayList<Collision> circlecastAll(Vector2 sourcePos, float distance) {
    return null;
  }
}
