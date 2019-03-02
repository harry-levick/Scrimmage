package shared.handlers.levelHandler;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import shared.gameObjects.GameObject;
import shared.gameObjects.MapDataObject;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectID;

public class MapLoaderTest {

  LinkedHashMap<UUID, GameObject> gameObjects = new LinkedHashMap<>();
  private TestObject g1 = new TestObject(6, 7, ObjectID.Bot, UUID.randomUUID());
  private TestObject g2 = new TestObject(934, 12312, ObjectID.Bot, UUID.randomUUID());
  private TestObject g3 = new TestObject(567560, 12, ObjectID.Player, UUID.randomUUID());
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
    LinkedHashMap<UUID, GameObject> gameObjectsTest = map.loadMap(path);
    int i = 0;
    while (gameObjectsTest.size() > i) {
      assertEquals(gameObjectsTest.get(i).getX(), gameObjects.get(i).getX(), 0.0001);
      assertEquals(gameObjectsTest.get(i).getY(), gameObjects.get(i).getY(), 0.0001);
      assertEquals(gameObjectsTest.get(i).getId(), gameObjects.get(i).getId());
      i++;
    }
  }
}
