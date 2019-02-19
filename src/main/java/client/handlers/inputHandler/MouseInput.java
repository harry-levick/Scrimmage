package client.handlers.inputHandler;

import client.main.Client;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;

public class MouseInput implements EventHandler<MouseEvent> {

  private Player clientPlayer;

  public MouseInput() {
    this.clientPlayer = Client.levelHandler.getClientPlayer();
  }

  @Override
  public void handle(MouseEvent event) {
    EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

    if (type == MouseEvent.MOUSE_MOVED || type == MouseEvent.MOUSE_DRAGGED) {
      clientPlayer.mouseX = event.getX();
      clientPlayer.mouseY = event.getY();
      sendInput();
    }
    if (type == MouseEvent.MOUSE_PRESSED) {
      clientPlayer.click = true;
      sendInput();
    } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
      clientPlayer.click = false;
      sendInput();
    }
  }

  public void sendInput() {
    if (Client.multiplayer) {
      PacketInput input =
          new PacketInput(
              clientPlayer.mouseX,
              clientPlayer.mouseY,
              clientPlayer.leftKey,
              clientPlayer.rightKey,
              clientPlayer.jumpKey,
              clientPlayer.click,
              clientPlayer.getUUID());
      Client.connectionHandler.send(input.getData());
    }
  }
}
