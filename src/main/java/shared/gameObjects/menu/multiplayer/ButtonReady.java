package shared.gameObjects.menu.multiplayer;

import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonReady extends ButtonObject {

  public ButtonReady(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "You Must Kill them All to begin", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
  }
}
