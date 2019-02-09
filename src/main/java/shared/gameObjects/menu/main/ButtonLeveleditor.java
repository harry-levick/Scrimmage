package shared.gameObjects.menu.main;

import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.ButtonObject;

import java.util.UUID;

public class ButtonLeveleditor extends ButtonObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonLeveleditor(
      double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/leveleditor_unpressed.png", "images/buttons/leveleditor_pressed.png");
  }
}
