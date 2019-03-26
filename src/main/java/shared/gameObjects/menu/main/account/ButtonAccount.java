package shared.gameObjects.menu.main.account;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

/** Clickable Button to enter Account management screen */
public class ButtonAccount extends ButtonObject {

  public ButtonAccount(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "ACCOUNT", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings
        .getLevelHandler()
        .changeMap(
            new Map("ACCOUNT", Path.convert("src/main/resources/menus/account.map")), false, false);
  }
}
