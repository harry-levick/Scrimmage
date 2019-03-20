package shared.gameObjects.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.GameObject;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.Physics;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import shared.util.maths.Vector2;

public class ColliderTest {

  private static BoxCollider boxA, boxB, boxC;
  private static CircleCollider circleD, circleE, circleF;
  private static TestObject a, b, c, d, e, f;

  @BeforeClass
  public static void InitColliders() {
    a = new TestObject(2, 2, 2, 2, ObjectType.Player, UUID.randomUUID());
    b = new TestObject(3, 1, 2, 2, ObjectType.Player, UUID.randomUUID());
    c = new TestObject(10, 10, 2, 2, ObjectType.Player, UUID.randomUUID());
    d = new TestObject(11, 11, 1, 1, ObjectType.Player, UUID.randomUUID());
    e = new TestObject(20, 20, 1, 1, ObjectType.Player, UUID.randomUUID());
    f = new TestObject(21, 21, 1, 1, ObjectType.Player, UUID.randomUUID());

    boxA = new BoxCollider(a, false);
    boxB = new BoxCollider(b, false);
    boxC = new BoxCollider(c, false);
    circleD = new CircleCollider(d, 1, false);
    circleE = new CircleCollider(e, 2, false);
    circleF = new CircleCollider(f, 2, false);

    ConcurrentLinkedHashMap<UUID, GameObject> objects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    a.addComponent(boxA);
    b.addComponent(boxB);
    c.addComponent(boxC);
    d.addComponent(circleD);
    e.addComponent(circleE);
    f.addComponent(circleF);

    objects.put(UUID.randomUUID(), a);
    objects.put(UUID.randomUUID(), b);
    objects.put(UUID.randomUUID(), c);
    objects.put(UUID.randomUUID(), d);
    objects.put(UUID.randomUUID(), e);
    objects.put(UUID.randomUUID(), f);
    Physics.gameObjects = objects;
  }

  @Test
  public void boxBoxCollide() {
    assertTrue(Collider.haveCollided(boxA, boxB));
  }

  @Test
  public void BoxBoxNoCollide() {
    assertTrue(!Collider.haveCollided(boxA, boxC));
    assertTrue(!Collider.haveCollided(boxB, boxC));
  }

  @Test
  public void circleBoxCollide() {
    assertTrue(Collider.haveCollided(boxC, circleD));
  }

  @Test
  public void circleBoxNoCollide() {
    assertTrue(!Collider.haveCollided(boxA, circleD));
    assertTrue(!Collider.haveCollided(boxB, circleE));
    assertTrue(!Collider.haveCollided(boxC, circleF));
  }

  @Test
  public void CircleCircleCollide() {
    assertTrue(Collider.haveCollided(circleE, circleF));
  }

  @Test
  public void circleCircleNoCollide() {
    assertTrue(!Collider.haveCollided(circleE, circleD));
    assertTrue(!Collider.haveCollided(circleD, circleF));
  }

  @Test
  public void raycast() {
    assertTrue(Physics.raycast(Vector2.Zero(), new Vector2(12, 12), false) != null);
  }

  @Test
  public void raycastClosest() {
    assertTrue(
        Physics.raycast(Vector2.Zero(), new Vector2(12, 12), false).getCollidedObject().equals(a));
  }

  @Test
  public void raycastAll() {
    assertEquals(4, Physics.raycastAll(Vector2.Zero(), new Vector2(12, 12), false).size(), 0);
  }

  @Test
  public void boxcast() {
    assertEquals(2, Physics.boxcastAll(new Vector2(0, 0), new Vector2(4, 4), false).size(), 0);
  }

  @Test
  public void circlecast() {
    assertEquals(2, Physics.circlecastAll(new Vector2(0, 0), 4).size(), 0);
  }
}
