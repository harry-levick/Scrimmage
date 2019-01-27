package shared.gameObjects.components;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectID;
import shared.util.maths.Vector2;

public class ColliderTest {
  private static BoxCollider boxA, boxB, boxC;
  private static CircleCollider circleD, circleE, circleF;
  private static TestObject a, b, c, d, e, f;

  @BeforeClass
  public static void InitColliders() {
    a = new TestObject(2, 2, ObjectID.Player);
    b = new TestObject(3, 1, ObjectID.Player);
    c = new TestObject(10, 10, ObjectID.Player);
    d = new TestObject(11, 11, ObjectID.Player);
    e = new TestObject(20, 20, ObjectID.Player);
    f = new TestObject(21, 21, ObjectID.Player);

    boxA = new BoxCollider(a, new Vector2(2, 2), false);
    boxB = new BoxCollider(b, new Vector2(2, 2), false);
    boxC = new BoxCollider(c, new Vector2(2, 2), false);
    circleD = new CircleCollider(d, 1, false);
    circleE = new CircleCollider(e, 2, false);
    circleF = new CircleCollider(f, 2, false);
  }

  @Test
  public void boxBoxCollide() {
    assertTrue(Collider.boxBoxCollision(boxA, boxB));
  }

  @Test
  public void BoxBoxNoCollide() {
    assertTrue(!Collider.boxBoxCollision(boxA, boxC));
    assertTrue(!Collider.boxBoxCollision(boxB, boxC));
  }

  @Test
  public void circleBoxCollide() {
    assertTrue(Collider.boxCircleCollision(boxC, circleD));
  }

  @Test
  public void circleBoxNoCollide() {
    assertTrue(!Collider.boxCircleCollision(boxA, circleD));
    assertTrue(!Collider.boxCircleCollision(boxB, circleE));
    assertTrue(!Collider.boxCircleCollision(boxC, circleF));
  }

  @Test
  public void CircleCircleCollide() {
    assertTrue(Collider.circleCircleCollision(circleE, circleF));
  }

  @Test
  public void circleCircleNoCollide() {
    assertTrue(!Collider.circleCircleCollision(circleE, circleD));
    assertTrue(!Collider.circleCircleCollision(circleD, circleF));
  }
}
