package shared.gameObjects;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.physics.data.Collision;

public class TestObject extends GameObject {

  public int test;
  public int testStay;
  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public TestObject(int x, int y, ObjectType id, UUID testUUID) {
    super(x, y, 100, 100, id, testUUID);
  }

  public TestObject(int x, int y, int sizeX, int sizeY, ObjectType id, UUID testUUID) {
    super(x, y, sizeX, sizeY, id, testUUID);
    test = 0;
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
  }

  @Override
  public void initialiseAnimation() {
    // TODO Auto-generated method stub

  }

  @Override
  public void OnCollisionEnter(Collision col) {
    test = 1;
    System.out.println("Enter");
  }
  @Override
  public void OnCollisionStay(Collision col) {
    testStay++;
    System.out.println("Stay");
  }
  @Override
  public void OnCollisionExit(Collision col) {
    test = 2;
    System.out.println("Exit");
  }
}
