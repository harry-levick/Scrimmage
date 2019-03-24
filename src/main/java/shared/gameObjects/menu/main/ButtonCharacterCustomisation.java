package shared.gameObjects.menu.main;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonCharacterCustomisation extends ButtonObject {

  public ButtonCharacterCustomisation(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "USER DATA", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings.getLevelHandler().changeMap(
        new Map(
            "CHARACTER",
            Path.convert("src/main/resources/menus/character.map")),
        false, false);
  }
}
