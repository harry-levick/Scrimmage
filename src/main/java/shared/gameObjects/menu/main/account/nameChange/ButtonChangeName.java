package shared.gameObjects.menu.main.account.nameChange;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonChangeName extends ButtonObject {

  public ButtonChangeName(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "CHANGE NAME", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings.getLevelHandler().changeMap(
        new Map(
            "CHANGENAME",
            Path.convert("src/main/resources/menus/changename.map")),
        false, false);
  }
}
