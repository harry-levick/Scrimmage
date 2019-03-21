package shared.gameObjects;

import static org.junit.Assert.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.swing.Box;
import org.junit.Before;
import org.junit.Test;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.components.BoxCollider;
import shared.gameObjects.components.Collider;
import shared.gameObjects.components.Component;
import shared.gameObjects.components.ComponentType;
import shared.gameObjects.components.Rigidbody;
import shared.gameObjects.components.behaviours.ObjectShake;
import shared.gameObjects.components.behaviours.blockBehaviours.MovingPlatform;
import shared.physics.Physics;
import shared.physics.types.ColliderLayer;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import shared.util.maths.Vector2;

public class GameObjectTest {

  static TestObject object1, object2;
  @Before
  public void setUp() throws Exception {
    object1 = new TestObject(10, 10, ObjectType.Bot, UUID.randomUUID());
    object2 = new TestObject(10, 10, ObjectType.Bot, UUID.randomUUID());

    object1.addComponent(new BoxCollider(object1, false));
    object2.addComponent(new BoxCollider(object2, false));
    object1.addComponent(new Rigidbody(0, object1));
    object2.addComponent(new Rigidbody(0, object2));

    ConcurrentLinkedHashMap<UUID, GameObject> objects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
        .maximumWeightedCapacity(500).build();
    objects.put(UUID.randomUUID(), object2);
    Physics.gameObjects = objects;
  }

  @Test
  public void updateCollisionEnter() {
      object1.update();
      object2.update();
      object1.updateCollision();
      assertEquals(1, object1.test);
  }
  @Test
  public void updateCollisionStay() {
    object1.update();
    object2.update();
    object1.updateCollision();
    object1.updateCollision();
    object1.updateCollision();
    assertEquals(3, object1.testStay);
  }
  @Test
  public void updateCollisionExit() {
    object1.update();
    object2.update();
    object1.updateCollision();
    ((Collider) object1.getComponent(ComponentType.COLLIDER)).setLayer(ColliderLayer.PARTICLE);
    object1.update();
    object1.update();
    object1.updateCollision();
    object1.updateCollision();
    assertEquals(2, object1.test);
  }

  @Test
  public void getComponent() {
    Component comp = new ObjectShake(object1);
    object1.addComponent(comp);
    assertTrue(object1.getComponent(ComponentType.BEHAVIOUR).equals(comp));
  }

  @Test
  public void getComponents() {
    assertEquals(1, object1.getComponents(ComponentType.COLLIDER).size());
  }
}