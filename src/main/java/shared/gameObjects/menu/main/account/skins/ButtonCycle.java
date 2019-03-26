package shared.gameObjects.menu.main.account.skins;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonCycle extends ButtonObject {

  public ButtonCycle(double x, double y, double sizeX, double sizeY, boolean right, ObjectType id, UUID uuid) {
    super(x, y, sizeX, sizeY, right ? "|]" : "[|" , id, uuid);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    ((CycleManager)parent).triggerClick(this);
  }
}
