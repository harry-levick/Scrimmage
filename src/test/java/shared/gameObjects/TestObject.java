package shared.gameObjects;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.components.ComponentType;

public class TestObject extends GameObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public TestObject(int x, int y, ObjectID id) {
    super(x, y, id, "testobjectimagepath");
  }

  @Override
  public void update() {
    GetComponent(ComponentType.RIGIBODY).update();
  }

  @Override
  public void render() {}
}
