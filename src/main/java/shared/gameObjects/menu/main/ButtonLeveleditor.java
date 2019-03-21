package shared.gameObjects.menu.main;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

/**
 * Button to load the Level Editor to the client
 */
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
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Level Editor", id, objectUUID);
  }
}
