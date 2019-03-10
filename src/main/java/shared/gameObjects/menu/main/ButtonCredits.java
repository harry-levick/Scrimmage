package shared.gameObjects.menu.main;

import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonCredits extends ButtonObject {

  public ButtonCredits(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Credits", id, objectUUID);
  }

  @Override
  public void initialiseAnimation() {
    super.initialiseAnimation(
        "images/buttons/credits_unpressed.png", "images/buttons/credits_pressed.png");
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.showCredits();
  }
}
