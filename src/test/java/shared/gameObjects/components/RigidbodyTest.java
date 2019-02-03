package shared.gameObjects.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectID;
import shared.physics.data.AngularData;
import shared.physics.data.MaterialProperty;
import shared.physics.types.RigidbodyType;
import shared.util.maths.Vector2;

public class RigidbodyTest {
  private static BoxCollider boxA, boxB, boxC;
  private static CircleCollider circleD, circleE, circleF;
  private static Rigidbody rbA, rbB, rbC, rbD, rbE, rbF;
  private static TestObject a, b, c, d, e, f;
  private static MaterialProperty material;
  private static AngularData angularData;

  @Before
  public void InitColliders() {
    a = new TestObject(2, 2, ObjectID.Player, UUID.randomUUID());
    b = new TestObject(3, 1, ObjectID.Player, UUID.randomUUID());
    c = new TestObject(10, 10, ObjectID.Player, UUID.randomUUID());
    d = new TestObject(11, 11, ObjectID.Player, UUID.randomUUID());
    e = new TestObject(20, 20, ObjectID.Player, UUID.randomUUID());
    f = new TestObject(21, 21, ObjectID.Player, UUID.randomUUID());

    boxA = new BoxCollider(a, new Vector2(2, 2), false);
    boxB = new BoxCollider(b, new Vector2(2, 2), false);
    boxC = new BoxCollider(c, new Vector2(2, 2), false);
    circleD = new CircleCollider(d, 1, false);
    circleE = new CircleCollider(e, 2, false);
    circleF = new CircleCollider(f, 2, false);

    material = new MaterialProperty(1, 0, 0);
    angularData = new AngularData(0, 0, 0, 0);
    rbA = new Rigidbody(RigidbodyType.DYNAMIC, 1, 1, 0, material, angularData, a);
    rbC = new Rigidbody(RigidbodyType.DYNAMIC, 1, 1, 0, material, angularData, c);
    a.addComponent(boxA);
    a.addComponent(rbA);
    c.addComponent(boxC);
    c.addComponent(rbC);
  }

  @Test
  public void gravityInAir() {
      assertEquals(a.getTransform().getPos().getY(), 2, 0);
      for (int i = 0; i < 60; i++)
        rbA.update();
      assertEquals(6.9, a.getY(), 0.5);
  }

  @Test
  public void gravityGrounded() {}

  @Test
  public void dragForceGravity() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
    rbA.setAirDrag(1);
    for (int i = 0; i < 60; i++)
      rbA.update();
    assertTrue(a.getY() < 6.6 && a.getY() > 5.9);
  }

  @Test
  public void addForceNoTime() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
    for (int i = 0; i < 60; i++) {
      rbA.addForce(new Vector2(100, 0));
      rbA.update();
    }
    assertEquals(52, a.getX(), 4);
  }

  @Test
  public void addForceTime() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
    rbA.addForce(new Vector2(6000, 0), 1);
    for (int i = 0; i < 60; i++) {
      rbA.update();
    }
    assertEquals(52, a.getX(), 4);
  }

  @Test
  public void addDistanceNoTime() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
    rbA.move(new Vector2(10, 10));
    assertEquals(12, a.getX(), 0.5);
  }

  @Test
  public void addDistanceTime() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
    rbA.move(new Vector2(0, 5), 1);
    for (int i = 0; i < 60; i++) {
      rbA.update();
    }
    assertEquals(11.7, a.getY(), 1);
  }

  @Test
  public void collisionDynamicDynamic() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void collisionDynamicStatic() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void collisionStaticStatic() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void staticFriction() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void kineticFriction() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void multipleDynamicCollisions() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void multipleStaticCollisions() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }

  @Test
  public void terminalVelocities() {
    assertEquals(a.getTransform().getPos().getY(), 2, 0);
  }
}
