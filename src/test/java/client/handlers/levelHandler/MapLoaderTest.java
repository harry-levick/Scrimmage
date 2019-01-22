package client.handlers.levelHandler;

import org.junit.Before;
import org.junit.Test;
import shared.gameObjects.GameObject;
import shared.gameObjects.TestObject;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.Utils.Version;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MapLoaderTest {
    private TestObject g1 = new TestObject(6,7, ObjectID.Bot, Version.CLIENT);
    private TestObject g2 = new TestObject(934,12312, ObjectID.Bot, Version.SERVER);
    private TestObject g3 = new TestObject(567560,12, ObjectID.Player, Version.CLIENT);
    ArrayList<GameObject> gameObjects = new ArrayList<>();

    private MapLoader map = new MapLoader();
    private String path = "B:/map.ser";

    @Before
    public void setUp() throws Exception {
        gameObjects.add(g1);
        gameObjects.add(g2);
        gameObjects.add(g3);
    }

    @Test
    public void saveMap() {
        map.saveMap(gameObjects,path);
    }

    @Test
    public void loadMap() {
        map.saveMap(gameObjects,path);
        map.loadMap(path);
    }

    @Test
    public void equalAfterLoadAndSave() {
        map.saveMap(gameObjects,path);
        ArrayList<GameObject> gameObjectsTest = map.loadMap(path);
        int i = 0;
        while (gameObjectsTest.size() > i) {
            assertEquals(gameObjectsTest.get(i).getX(),gameObjects.get(i).getX());
            assertEquals(gameObjectsTest.get(i).getY(),gameObjects.get(i).getY());
            assertEquals(gameObjectsTest.get(i).getId(),gameObjects.get(i).getId());
            assertEquals(gameObjectsTest.get(i).getVersion(),gameObjects.get(i).getVersion());
            i++;
        }
    }
}