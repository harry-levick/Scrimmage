package shared.gameObjects;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;

public class TestObject extends GameObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public TestObject(int x, int y, ObjectID id, UUID testUUID) {
    super(x, y, 100, 100, id, testUUID);
  }

  public TestObject(int x, int y, int sizeX, int sizeY, ObjectID id, UUID testUUID) {
    super(x, y, sizeX, sizeY, id, testUUID);
  }

  @Override
  public void update() {}

  @Override
  public void render() {}

  @Override
  public String getState() {
    return null;
  }

  @Override
  public void initialiseAnimation() {
    // TODO Auto-generated method stub

  }
}
