package shared.gameObjects.Utils;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import shared.gameObjects.TestObject;
import shared.util.maths.Vector2;

public class TransformTest {

  static TestObject object;
  @BeforeClass
  public static void initTransform() {
    object = new TestObject();
  }
  @Test
  public void translate() {
    object.getTransform().translate(new Vector2(10, 10));
    assertTrue(object.getTransform().getPos().equals(new Vector2(10, 10)));
  }

  @Test
  public void rotate() {
    object.getTransform().rotate(100);
    assertEquals(100f, object.getTransform().getRot(), 0.000000000000001f);
  }

  @Test
  public void scale() {
    object.getTransform().scale(new Vector2(2, 2));
    assertTrue(object.getTransform().getSize().equals(new Vector2(20, 20)));
  }
}