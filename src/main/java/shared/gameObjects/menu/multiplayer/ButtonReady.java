package shared.gameObjects.menu.multiplayer;

import client.main.Client;
import java.util.UUID;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.Utils.ObjectType;
import shared.gameObjects.menu.ButtonObject;
import shared.packets.PacketReady;

public class ButtonReady extends ButtonObject {

  public ButtonReady(double x, double y, double sizeX, double sizeY, ObjectType id,
      UUID objectUUID) {
    super(x, y, sizeX, sizeY, "Ready", id, objectUUID);
  }

  public void doOnClick(MouseEvent e) {
    super.doOnClick(e);
    Client.connectionHandler.send(
        (new PacketReady(settings.getLevelHandler().getClientPlayer().getUUID(),
            settings.getData().getUsername())).getString());
  }
}
