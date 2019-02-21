package shared.gameObjects.menu.main;

import client.Menu;
import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectID;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.GameState;
import shared.handlers.levelHandler.Map;

public class ButtonSettings extends ButtonObject {

  /**
   * Base class used to create an object in game. This is used on both the client and server side to
   * ensure actions are calculated the same
   *
   * @param x X coordinate of object in game world
   * @param y Y coordinate of object in game world
   * @param id Unique Identifier of every game object
   */
  public ButtonSettings(
      double x, double y, double sizeX, double sizeY, ObjectID id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/settings_unpressed.png", "images/buttons/settings_pressed.png");
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    //System.out.println("test");
    Client.levelHandler.changeMap(
        new Map("Settings", Menu.SETTINGS.getMenuPath(), GameState.MAIN_MENU), false);
  }
}
