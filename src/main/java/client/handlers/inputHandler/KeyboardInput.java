package client.handlers.inputHandler;

import client.main.Client;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import shared.gameObjects.players.Player;
import shared.packets.PacketInput;

public class KeyboardInput implements EventHandler<KeyEvent> {

  private Player clientPlayer;

  public KeyboardInput() {
    this.clientPlayer = Client.levelHandler.getClientPlayer();
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
      switch (event.getCode()) {
        case A:
          clientPlayer.leftKey = true;
          sendInput();
          break;
        case D:
          clientPlayer.rightKey = true;
          sendInput();
          break;
        case W:
          clientPlayer.jumpKey = true;
          sendInput();
          break;
        default:
      }
    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      switch (event.getCode()) {
        case A:
          clientPlayer.leftKey = false;
          sendInput();
          break;
        case D:
          clientPlayer.rightKey = false;
          sendInput();
          break;
        case W:
          clientPlayer.jumpKey = false;
          sendInput();
          break;
        default:
      }
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
