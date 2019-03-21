package shared.gameObjects.menu.multiplayer;

import client.Menu;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;

/**
 * Button to take the client to the host menu
 */
public class ButtonHost extends ButtonObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonHost(double x, double y, ObjectType id, UUID objectUUID) {
    super(x, y, 50, 50, "Host", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings.getLevelHandler().changeMap(
        new Map("Host", Menu.HOST.getMenuPath()), false, false);
  }
}
