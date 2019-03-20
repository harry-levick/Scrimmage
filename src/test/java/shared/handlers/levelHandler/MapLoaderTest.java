package shared.handlers.levelHandler;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectType;
import shared.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public class MapLoaderTest {

  ConcurrentLinkedHashMap<UUID, GameObject> gameObjects = new ConcurrentLinkedHashMap.Builder<UUID, GameObject>()
      .maximumWeightedCapacity(500).build();
  private TestObject g1 = new TestObject(6, 7, ObjectType.Bot, UUID.randomUUID());
  private TestObject g2 = new TestObject(934, 12312, ObjectType.Bot, UUID.randomUUID());
  private TestObject g3 = new TestObject(567560, 12, ObjectType.Player, UUID.randomUUID());
  private MapDataObject mapdata = new MapDataObject(UUID.randomUUID(), GameState.IN_GAME);
  private MapLoader map = new MapLoader();
  private String path = "B:/map.ser";

  @Before
  public void setUp() {
    gameObjects.put(g1.getUUID(), g1);
    gameObjects.put(g2.getUUID(), g2);
    gameObjects.put(g3.getUUID(), g3);
  }

  @Test
  public void saveMap() {
    map.saveMap(gameObjects, mapdata, path);
  }

  @Test
  public void loadMap() {
    map.saveMap(gameObjects, mapdata, path);
    map.loadMap(path);
  }

  @Test
  public void equalAfterLoadAndSave() {
    map.saveMap(gameObjects, mapdata, path);
    ConcurrentLinkedHashMap<UUID, GameObject> gameObjectsTest = map.loadMap(path);
    int i = 0;
    while (gameObjectsTest.size() > i) {
      assertEquals(gameObjectsTest.get(i).getX(), gameObjects.get(i).getX(), 0.0001);
      assertEquals(gameObjectsTest.get(i).getY(), gameObjects.get(i).getY(), 0.0001);
      assertEquals(gameObjectsTest.get(i).getId(), gameObjects.get(i).getId());
      i++;
    }
  }
}
