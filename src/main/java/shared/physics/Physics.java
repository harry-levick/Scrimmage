package shared.physics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import shared.gameObjects.GameObject;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.physics.data.Collision;
import shared.util.maths.Vector2;

/** @author fxa579 The singleton class respomsible for raycasting and physics constants/equations */
public class Physics {

  public static final float GRAVITY = 100f;
  public static final float TIMESTEP = 1f / 60;
  public static ArrayList<GameObject> gameObjects;
  private static Physics ourInstance = new Physics();

  private Physics() {}
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

  public static Collision boxcast(Vector2 sourcePos, Vector2 size, Vector2 direction, float distance) {
    direction = direction.normalize().mult(distance);
    BoxCollider castCollider = new BoxCollider(sourcePos, size.mult(direction));
    Collision collision;
    for (GameObject object : gameObjects) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision = Collision.resolveCollision(castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if(collision != null) return collision;
      }
    }
    return null;
  }

  public static ArrayList<Collision> boxcastAll(Vector2 sourcePos, Vector2 size, Vector2 direction, float distance) {
    direction = direction.normalize().add(distance);
    BoxCollider castCollider = new BoxCollider(sourcePos, size);
    Collision collision;
    ArrayList<Collision> collisions = new ArrayList<>();
    for (GameObject object : gameObjects) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision = Collision.resolveCollision(castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if(collision != null)  {
          collisions.add(collision);
        }
      }
    }
    return collisions;
  }

  public static Collision circlecast(Vector2 sourcePos, float distance) {
    return null;
  }

  public static ArrayList<Collision> circlecastAll(Vector2 sourcePos, float distance) {
    return null;
  }
}
