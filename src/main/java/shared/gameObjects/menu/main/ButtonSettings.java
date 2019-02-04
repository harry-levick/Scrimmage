package shared.gameObjects.menu.main;

import java.util.UUID;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.ButtonObject;

public class ButtonSettings extends ButtonObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonSettings(double x, double y, ObjectID id, UUID objectUUID) {
    super(x, y, id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation("images/buttons/settings_unpressed.png","images/buttons/settings_pressed.png");
  }


}
