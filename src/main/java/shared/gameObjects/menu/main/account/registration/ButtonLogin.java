package shared.gameObjects.menu.main.account.registration;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonLogin extends ButtonObject {

  public ButtonLogin(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "LOGIN", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings.getLevelHandler().changeMap(
        new Map(
            "LOGIN",
            Path.convert("src/main/resources/menus/login.map")),
        false, false);
  }
}
