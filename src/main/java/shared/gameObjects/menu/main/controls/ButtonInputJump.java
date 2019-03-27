package shared.gameObjects.menu.main.controls;

import client.main.Client;
import client.main.Settings.KEY_CONTROL;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;

public class ButtonInputJump extends ButtonObject {

  public ButtonInputJump(
      double x, double y, double sizeX, double sizeY, ObjectType id, UUID objectUUID) {
    super(x, y, sizeX, sizeY, "JUMP: ", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.keyInput.setChangeListening(KEY_CONTROL.JUMP);
  }

  @Override
  public void render() {
    super.render();
    super.button.setFont(settings.getFont(20));
    super.setText("JUMP: " + Client.settings.getKeyMap(KEY_CONTROL.JUMP));
    if (Client.keyInput.getChangeListening() && Client.keyInput.getHotChangeKey() == KEY_CONTROL.JUMP) {
      super.button.setTextFill(Color.YELLOW);
    } else {
      super.button.setTextFill(Color.WHITE);
    }
  }
}
