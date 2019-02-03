package shared.gameObjects.components;

import java.util.UUID;
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

  @BeforeClass
  public static void InitColliders() {
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
    a.AddComponent(boxA);
    a.AddComponent(rbA);
    c.AddComponent(boxC);
    c.AddComponent(rbC);
  }

  @Test
  public void gravityInAir() {}

  @Test
  public void gravityGrounded() {}

  @Test
  public void dragForceGravity() {}

  @Test
  public void addForceNoTime() {}

  @Test
  public void addForceTime() {}

  @Test
  public void addDistanceNoTime() {}

  @Test
  public void addDistanceTime() {}

  @Test
  public void collisionDynamicDynamic() {}

  @Test
  public void collisionDynamicStatic() {}

  @Test
  public void collisionStaticStatic() {}

  @Test
  public void staticFriction() {}

  @Test
  public void kineticFriction() {}

  @Test
  public void multipleDynamicCollisions() {}

  @Test
  public void multipleStaticCollisions() {}

  @Test
  public void terminalVelocities() {}
}
