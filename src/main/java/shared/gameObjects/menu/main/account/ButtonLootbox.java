package shared.gameObjects.menu.main.account;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonLootbox extends ButtonObject {

  public ButtonLootbox(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "LOOTBOXES", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings
        .getLevelHandler()
        .changeMap(
            new Map("lootbox", Path.convert("src/main/resources/menus/lootbox.map")), false, false);
  }
}
