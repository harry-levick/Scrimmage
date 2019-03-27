package shared.gameObjects.menu.main.controls;

import client.main.Client;
import client.main.Settings.KEY_CONTROL;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonInputRight extends ButtonObject {

  public ButtonInputRight(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "RIGHT: ", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.keyInput.setChangeListening(KEY_CONTROL.RIGHT);
  }

  @Override
  public void render() {
    super.render();
    super.button.setFont(settings.getFont(20));
    super.setText("RIGHT: " + Client.settings.getKeyMap(KEY_CONTROL.RIGHT));
    if (Client.keyInput.getChangeListening() && Client.keyInput.getHotChangeKey() == KEY_CONTROL.RIGHT) {
      super.button.setTextFill(Color.YELLOW);
    } else {
      super.button.setTextFill(Color.WHITE);
    }
  }

}
