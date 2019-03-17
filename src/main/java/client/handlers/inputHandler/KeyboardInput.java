package client.handlers.inputHandler;

import client.main.Client;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import shared.gameObjects.players.Player;

public class KeyboardInput implements EventHandler<KeyEvent> {

  private Player clientPlayer;

  public KeyboardInput() {
    this.clientPlayer = Client.levelHandler.getClientPlayer();
  }

  @Override
  public void handle(KeyEvent event) {
    Client.sendUpdate = true;
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      switch (event.getCode()) {
        case A:
          clientPlayer.leftKey = true;
          break;
        case D:
          clientPlayer.rightKey = true;
          break;
        case W:
          clientPlayer.jumpKey = true;
          break;
        case H:
          clientPlayer.deattach = true;
          break;
        case ESCAPE:
          Client.settingsToggle();
        default:
          Client.sendUpdate = false;
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      switch (event.getCode()) {
        case A:
          clientPlayer.leftKey = false;
          break;
        case D:
          clientPlayer.rightKey = false;
          break;
        case W:
          clientPlayer.jumpKey = false;
          break;
        case H:
          clientPlayer.deattach = false;
          break;
        default:
          Client.sendUpdate = false;
      }
    }
  }
}
