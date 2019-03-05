package shared.physics;

import client.main.Client;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;
import javafx.scene.shape.Line;
import shared.gameObjects.GameObject;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.EdgeCollider;
import shared.physics.data.Collision;
import shared.physics.data.DynamicCollision;
import shared.util.maths.Vector2;

/** @author fxa579 The singleton class respomsible for raycasting and physics constants/equations */
public class Physics {

  public static final float GRAVITY = 100f;
  public static final float TIMESTEP = 1f / 60;
  public static final int RAYCAST_INC = 100;
  public static boolean showColliders = false;
  public static boolean showCasts = true;
  /*
   * Order: DEFAULT, PLAYER, OBJECT, WALL, PARTICLE
   */
  public static boolean[] DEFAULT = {true, true, true, true, false};
  public static boolean[] PLAYER = {true, false, true, true, false};
  public static boolean[] OBJECT = {true, true, true, true, false};
  public static boolean[] WALL = {true, true, true, true, false};
  public static boolean[] PARTICLES = {false, false, false, false, false};
  public static boolean[][] COLLISION_LAYERS = {DEFAULT, PLAYER, OBJECT, WALL, PARTICLES};
  public static LinkedHashMap<UUID, GameObject> gameObjects;
  private static ArrayList<DynamicCollision> collisions = new ArrayList<>();

  private Physics() {
    gameObjects = new LinkedHashMap<>();
  }

  /**
   * Casts a ray that interacts with colliders.
   *
   * @param sourcePos The point to start casting the ray
   * @param lengthAndDirection The length and direction of the ray
   * @return The first collider hit in the path, null if nothing was hit.
   */
  public static Collision raycast(Vector2 sourcePos, Vector2 lengthAndDirection) {
    EdgeCollider castCollider = new EdgeCollider(false);
    Collision collision = null;
    Vector2 incrementVal = lengthAndDirection.div(RAYCAST_INC);
    for (int i = 0; i <= RAYCAST_INC; i++) {
      castCollider.addNode(sourcePos.add(incrementVal.mult(i)));
    }

    if (showCasts) {
      Line line = new Line();
      line.setStartX(castCollider.getNodes().get(0).getX());
      line.setStartY(castCollider.getNodes().get(0).getY());
      line.setEndX(castCollider.getNodes().get(castCollider.getNodes().size() - 1).getX());
      line.setEndY(castCollider.getNodes().get(castCollider.getNodes().size() - 1).getY());
      line.setStyle("-fx-stroke-width: 4; -fx-stroke: #00FF00;");
      Client.gameRoot.getChildren().add(line);
    }

    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          return collision;
        }
      }
    }
    return collision;
  }

  /**
   * Casts a ray that interacts with colliders.
   *
   * @param sourcePos The point to start casting the ray
   * @param lengthAndDirection The length and direction of the ray
   * @return All colliders hit in the path, empty if nothing was hit.
   */
  public static ArrayList<Collision> raycastAll(Vector2 sourcePos, Vector2 lengthAndDirection) {
    EdgeCollider castCollider = new EdgeCollider(false);
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    Vector2 incrementVal = lengthAndDirection.div(RAYCAST_INC);
    for (int i = 0; i <= RAYCAST_INC; i++) {
      castCollider.addNode(sourcePos.add(incrementVal.mult(i)));
    }

    if (showCasts) {
      Line line = new Line();
      line.setStartX(castCollider.getNodes().get(0).getX());
      line.setStartY(castCollider.getNodes().get(0).getY());
      line.setEndX(castCollider.getNodes().get(castCollider.getNodes().size() - 1).getX());
      line.setEndY(castCollider.getNodes().get(castCollider.getNodes().size() - 1).getY());
      line.setStyle("-fx-stroke-width: 4; -fx-stroke: #324401;");
      Client.gameRoot.getChildren().add(line);
    }
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          collisions.add(collision);
        }
      }
    }
    return collisions;
  }

  /**
   * Creates a box collider that returns the first collision it hits
   *
   * @param sourcePos The top-right corner of the box
   * @param size The extents of the box
   * @return The first collider hit in the path, null if nothing was hit
   */
  public static Collision boxcast(Vector2 sourcePos, Vector2 size) {
    BoxCollider castCollider = new BoxCollider(sourcePos, size);
    Collision collision;
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          return collision;
        }
      }
    }
    return null;
  }

  /**
   * Creates a box collider that returns all collisions it hits
   *
   * @param sourcePos The top-right corner of the box
   * @param size The extents of the box
   * @return All colliders hit in the path, empty if nothing was hit
   */
  public static ArrayList<Collision> boxcastAll(Vector2 sourcePos, Vector2 size) {
    BoxCollider castCollider = new BoxCollider(sourcePos, size);
    Collision collision;
    ArrayList<Collision> collisions = new ArrayList<>();
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          collisions.add(collision);
        }
      }
    }
    return collisions;
  }

  /**
   * Creates a circle collider that returns the first collision it hits
   *
   * @param sourcePos The centre of the circle
   * @param radius The radius of the circle stretched from its centre
   * @return The first collider hit in the path. null if nothing was hit
   */
  public static Collision circlecast(Vector2 sourcePos, float radius) {
    Collision collision = null;
    CircleCollider castCollider = new CircleCollider(sourcePos, radius);
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          return collision;
        }
      }
    }
    return null;
  }

  /**
   * Creates a circle collider that returns the all collisions it hits
   *
   * @param sourcePos The centre of the circle
   * @param radius The radius of the circle stretched from its centre
   * @return All colliders hit in the path. empty if nothing was hit
   */
  public static ArrayList<Collision> circlecastAll(Vector2 sourcePos, float radius) {
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    CircleCollider castCollider = new CircleCollider(sourcePos, radius);
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        if (object.getComponent(ComponentType.COLLIDER) != null) {
          collision =
              new Collision(
                  object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
          if (collision.isCollided()) {
            collisions.add(collision);
          }
        }
      }
    }
    return collisions;
  }

  /**
   * Creates a semi-circle collider that returns the first collision it hits
   *
   * @param sourcePos The centre of the circle
   * @param radius The radius of the circle stretched from its centre
   * @param angleOfCentre The amount to rotate the centre Vector (originally pointing right)
   * @param angleOfArc The angle at how far the arc extends from the centre (+ and -)
   * @return The first collider hit in the path. null if nothing was hit
   */
  public static Collision arccast(
      Vector2 sourcePos, float radius, float angleOfCentre, float angleOfArc) {
    Collision collision = null;
    CircleCollider castCollider = new CircleCollider(sourcePos, radius);
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          float angle = Math.abs(collision.getNormalCollision().angle()) + angleOfCentre;
          if (angle <= angleOfArc) {
            return collision;
          }
        }
      }
    }
    return null;
  }

  /**
   * Creates a semi-circle collider that returns the first collision it hits
   *
   * @param sourcePos The centre of the circle
   * @param radius The radius of the circle stretched from its centre
   * @param angleOfCentre The amount to rotate the centre Vector (originally pointing right)
   * @param angleOfArc The angle at how far the arc extends from the centre (+ and -)
   * @return All colliders hit in the path, empty if nothing was hit
   */
  public static ArrayList<Collision> arccastAll(
      Vector2 sourcePos, float radius, float angleOfCentre, float angleOfArc) {
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    CircleCollider castCollider = new CircleCollider(sourcePos, radius);
    for (GameObject object : gameObjects.values()) {
      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          float angle = Math.abs(collision.getNormalCollision().angle()) + angleOfCentre;
          if (angle <= angleOfArc) {
            collisions.add(collision);
          }
        }
      }
    }
    return collisions;
  }

  public static boolean addCollision(DynamicCollision dcol) {
    for (DynamicCollision c : collisions) {
      if (c.getBodyA() == dcol.getBodyB() && c.getBodyB() == dcol.getBodyA()) {
        return false;
      }
    }
    collisions.add(dcol);
    return true;
  }

  public static void processCollisions() {
    collisions.clear();
  }
}
