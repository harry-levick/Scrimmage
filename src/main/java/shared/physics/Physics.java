package shared.physics;

import client.main.Client;
import client.main.Settings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import javafx.application.Platform;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import server.ai.Bot;
import shared.gameObjects.GameObject;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.CircleCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.EdgeCollider;
import shared.gameObjects.players.Limb;
import shared.gameObjects.weapons.Bullet;
import shared.gameObjects.weapons.Weapon;
import shared.handlers.levelHandler.LevelHandler;
import shared.physics.data.Collision;
import shared.physics.data.DynamicCollision;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import shared.util.maths.Vector2;

/** @author fxa579 The singleton class respomsible for raycasting and physics constants/equations */
public class Physics {

  /**
   * The base gravity constant used for speed, collisions and forces
   */
  public static final float GRAVITY = 90f;
  /**
   * The constant difference in time between update frames.
   */
  public static final float TIMESTEP = 1f / 60;
  /**
   * The number of increments that exist in a raycast's edge collider.
   */
  private static final int RAYCAST_INC = 100;
  /**
   * Global setting to show colliders while in game (For Debugging)
   */
  public static boolean showColliders = false;
  /**
   * Settings objects of client (For Debugging)
   */
  public static Settings settings;
  /*
   * Order: DEFAULT, PLAYER, OBJECT, PLATFORM, PARTICLE, COLLECTABLE, LIMBS
   */
  private static boolean[] DEFAULT = {true, true, true, true, false, false, true, true};
  private static boolean[] PLAYER = {true, false, true, true, false, false, false, false};
  private static boolean[] OBJECT = {true, true, true, true, false, false, true, true};
  private static boolean[] PLATFORM = {true, true, true, true, false, true, true, true};
  private static boolean[] PARTICLES = {false, false, false, false, false, false, false, false};
  private static boolean[] COLLECTABLE = {false, false, false, true, false, false, false, false};
  private static boolean[] LIMBS = {true, false, true, true, false, false, false, true};
  private static boolean[] PROJECTILE = {true, false, true, true, false, false, true, false};
  /**
   * The collision layers ordered as specified by their int value. 2D Matrix format, used by the Collider tests.
   */
  public static boolean[][] COLLISION_LAYERS = {DEFAULT, PLAYER, OBJECT, PLATFORM, PARTICLES,
      COLLECTABLE, LIMBS, PROJECTILE};
  public static ConcurrentLinkedHashMap<UUID, GameObject> gameObjects;
  /**
   * A concurrent map used to synchronize collision updates.
   */


  private static ArrayList<DynamicCollision> collisions = new ArrayList<>();

  private Physics(Settings settings) {
    gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    this.settings = settings;
  }

  /**
   * Casts a ray that interacts with colliders, returning the first collider it hits.
   *
   * @param sourcePos The point to start casting the ray
   * @param lengthAndDirection The length and direction of the ray
   * @return The first collider hit in the path, null if nothing was hit.
   */
  public static Collision raycast(Vector2 sourcePos, Vector2 lengthAndDirection,
      boolean showCollider) {
    EdgeCollider castCollider = new EdgeCollider(false);
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    Vector2 incrementVal = lengthAndDirection.div(RAYCAST_INC);
    for (int i = 0; i <= RAYCAST_INC; i++) {
      castCollider.addNode(sourcePos.add(incrementVal.mult(i)));
    }

    if (showCollider) {
      Platform.runLater(
          () -> {
            drawCast(castCollider.getNodes().get(0).getX(), castCollider.getNodes().get(0).getY(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getX(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getY(), "#00ff00");
          }
      );

    }

    Iterator<GameObject> iter = gameObjects.values().iterator();
    while (iter.hasNext()) {
      GameObject object = iter.next();
      if(object instanceof Bullet) continue;

      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          collisions.add(collision);
        }
      }
    }
    if (collisions.size() > 0) {
      Collision toRet = collisions.get(0);
      for (Collision c : collisions) {
        toRet = c.getPointOfCollision().sub(sourcePos).magnitude() <= toRet.getPointOfCollision()
            .sub(sourcePos).magnitude() ? c : toRet;
      }
      return toRet;
    } else {
      return null;
    }
  }

  /**
   * Casts a ray that interacts with colliders, returning the first collider it hits, ignoring Limbs.
   *
   * @param sourcePos The point to start casting the ray
   * @param lengthAndDirection The length and direction of the ray
   * @return The first collider hit in the path, null if nothing was hit.
   */
  public static Collision raycastAi(Vector2 sourcePos, Vector2 lengthAndDirection,
      ArrayList<GameObject> objects, Bot bot, boolean showCollider) {

    Iterator<GameObject> iter;
    if (objects == null) {
      iter = gameObjects.values().iterator();
    } else {
      iter = objects.iterator();
    }

    EdgeCollider castCollider = new EdgeCollider(false);
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    Vector2 incrementVal = lengthAndDirection.div(RAYCAST_INC);
    for (int i = 0; i <= RAYCAST_INC; i++) {
      castCollider.addNode(sourcePos.add(incrementVal.mult(i)));
    }

    if (showCollider) {
      Platform.runLater(
          () -> {
            drawCast(castCollider.getNodes().get(0).getX(), castCollider.getNodes().get(0).getY(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getX(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getY(), "#00ff00");
          }
      );

    }

    while (iter.hasNext()) {
      GameObject object = iter.next();

      if (object instanceof Limb || object instanceof Bullet) {
        continue;
      }

      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));

        boolean botHolder = false;
        if (object instanceof Weapon) {
          Weapon tempWeap = (Weapon) object;
          botHolder = tempWeap.getHolder() == bot;
        }

        if (collision.isCollided() && !(object == bot || object.getParent() == bot || botHolder)) {
          collisions.add(collision);
        }
      }
    }
    if (collisions.size() > 0) {
      Collision toRet = collisions.get(0);
      for (Collision c : collisions) {
        toRet = c.getPointOfCollision().sub(sourcePos).magnitude() <= toRet.getPointOfCollision()
            .sub(sourcePos).magnitude() ? c : toRet;
      }
      return toRet;
    } else {
      return null;
    }
  }

  /**
   * Draws a raycast for debugging
   */
  public static void drawCast(double xStart, double yStart, double xFinish, double yFinish,
      String colour) {
    Platform.runLater(
        () -> {
          Line line = new Line();
          line.setStartX(xStart);
          line.setStartY(yStart);
          line.setEndX(xFinish);
          line.setEndY(yFinish);

          line.setStyle(String.format("-fx-stroke-width: 4; -fx-stroke: %s;", colour));
          Client.levelHandler.getGameRoot().getChildren().add(line);
        }
    );
  }

  /**
   * Draws a boxcast for debugging
   */
  public static void drawBoxCast(Vector2 sourcePos, Vector2 size) {
    Platform.runLater(
        () -> {
          Rectangle r2 = new Rectangle(sourcePos.getX(), sourcePos.getY(), size.getX(),
              size.getY());
          r2.setStyle("-fx-stroke-width: 4; -fx-stroke: #00ff00;");
          Client.levelHandler.getGameRoot().getChildren().add(r2);
        });
  }

  public static void drawCircleCast(Vector2 centre, float radius) {

  }

  /**
   * Casts a ray that interacts with colliders, returning all colliders hit.
   *
   * @param sourcePos The point to start casting the ray
   * @param lengthAndDirection The length and direction of the ray
   * @return All colliders hit in the path, empty if nothing was hit.
   */
  public static ArrayList<Collision> raycastAll(Vector2 sourcePos, Vector2 lengthAndDirection, boolean showCast) {
    EdgeCollider castCollider = new EdgeCollider(false);
    Collision collision = null;
    ArrayList<Collision> collisions = new ArrayList<>();
    Vector2 incrementVal = lengthAndDirection.div(RAYCAST_INC);
    for (int i = 0; i <= RAYCAST_INC; i++) {
      castCollider.addNode(sourcePos.add(incrementVal.mult(i)));
    }

    if (showCast) {
      Platform.runLater(
          () -> {
            drawCast(
                castCollider.getNodes().get(0).getX(),
                castCollider.getNodes().get(0).getY(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getX(),
                castCollider.getNodes().get(castCollider.getNodes().size() - 1).getY(),
                "#00ff00");
          });
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
    ArrayList<Collision> collisions = new ArrayList<>();

    Iterator<GameObject> iter = gameObjects.values().iterator();
    while (iter.hasNext()) {
      GameObject object = iter.next();

      if (object instanceof Limb) {
        continue;
      }

      if (object.getComponent(ComponentType.COLLIDER) != null) {
        collision =
            new Collision(
                object, castCollider, (Collider) object.getComponent(ComponentType.COLLIDER));
        if (collision.isCollided()) {
          collisions.add(collision);
        }
      }
    }
    if (collisions.size() > 0) {
      Collision toRet = collisions.get(0);
      for (Collision c : collisions) {
        toRet = c.getPointOfCollision().sub(sourcePos).magnitude() <= toRet.getPointOfCollision()
            .sub(sourcePos).magnitude() ? c : toRet;
      }
      return toRet;
    } else {
      return null;
    }
  }

  /**
   * Creates a box collider that returns all collisions it hits
   *
   * @param sourcePos The top-right corner of the box
   * @param size The extents of the box
   * @return All colliders hit in the path, empty if nothing was hit
   */
  public static ArrayList<Collision> boxcastAll(Vector2 sourcePos, Vector2 size, boolean showCast) {
    BoxCollider castCollider = new BoxCollider(sourcePos, size);
    Collision collision;
    ArrayList<Collision> collisions = new ArrayList<>();

    if (showCast) {
      drawBoxCast(sourcePos, size);
    }

    Iterator<GameObject> iter = gameObjects.values().iterator();
    while (iter.hasNext()) {
      GameObject object = iter.next();

      if (object instanceof Limb) {
        continue;
      }

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
     * Used by collision system to add a DynamicCollision if no duplicate existss
     */
  public static boolean addCollision(DynamicCollision dcol) {
    for (DynamicCollision c : collisions) {
      if (c.getBodyA() == dcol.getBodyB() && c.getBodyB() == dcol.getBodyA()) {
        return false;
      }
    }
    collisions.add(dcol);
    return true;
  }

  /**
   * Method to safely clear the collisions list.
   */
  public static void clearCollisions() {
    collisions.clear();
  }
}
