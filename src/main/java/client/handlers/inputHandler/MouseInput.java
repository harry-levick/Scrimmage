package client.handlers.inputHandler;

import client.main.Client;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import shared.gameObjects.players.Player;

/**
 * Records mouse input of the player
 */
public class MouseInput implements EventHandler<MouseEvent> {

  private Player clientPlayer;

  /**
   * Default constructor
   */
  public MouseInput() {
    this.clientPlayer = Client.levelHandler.getClientPlayer();
  }

  @Override
  public void handle(MouseEvent event) {
    EventType<MouseEvent> type = (EventType<MouseEvent>) event.getEventType();

    if (type == MouseEvent.MOUSE_MOVED || type == MouseEvent.MOUSE_DRAGGED) {
      clientPlayer.mouseX = (event.getX() / (Client.scaleRatio != null ? Client.scaleRatio.getX()
          : 1));
      clientPlayer.mouseY = (event.getY() / (Client.scaleRatio != null ? Client.scaleRatio.getY()
          : 1));
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
