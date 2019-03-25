package shared.gameObjects.menu.main.account;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.handlers.levelHandler.Map;
import shared.util.Path;

public class ButtonAchievements extends ButtonObject {

  public ButtonAchievements(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "ACHIEVEMENTS", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    settings.getLevelHandler().changeMap(
        new Map(
            "achievements",
            Path.convert("src/main/resources/menus/achievements.map")),
        false, false);
  }
}
