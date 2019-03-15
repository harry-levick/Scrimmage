package client.handlers.inputHandler;

import client.main.Client;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.players.Player;

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
      Client.sendUpdate = true;
    }
    if (type == MouseEvent.MOUSE_PRESSED) {
      clientPlayer.click = true;
      Client.sendUpdate = true;
    } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
      clientPlayer.click = false;
      Client.sendUpdate = true;
    }
  }
}
