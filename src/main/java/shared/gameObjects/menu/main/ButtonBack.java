package shared.gameObjects.menu.main;

import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonBack extends ButtonObject {

  public ButtonBack(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Back", id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/back_unpressed.png", "images/buttons/back_pressed.png");
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.levelHandler.previousMap(false);
  }
}
