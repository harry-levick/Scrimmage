package shared.gameObjects.menu.main;

import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonMultiplayer extends ButtonObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonMultiplayer(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "MULTIPLAYER", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.levelHandler.changeMap(
        new Map(
            "MULTIPLAYER",
            Path.convert("src/main/resources/menus/multiplayer.map")),
        false);
  }
}
