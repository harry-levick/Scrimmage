package shared.gameObjects.menu.main;

import client.main.Client;
import client.main.Settings;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonQuit extends ButtonObject {

  public ButtonQuit(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "QUIT", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);

    //action
    // SINGLEPLAYER -> MAIN MENU
    // MULTIPLAYER -> MAIN MENU
    // MAIN MENU -> CLOSE
    switch (Client.levelHandler.getMap().getGameState()) {
      case IN_GAME:
      case LOBBY:
      case START_CONNECTION:
      case MULTIPLAYER:
        Client.levelHandler.changeMap(Settings.getMainMenu(), true);
        break;
      case MAIN_MENU:
        System.exit(0);
        break;
      default:
        break;
    }
  }
}
